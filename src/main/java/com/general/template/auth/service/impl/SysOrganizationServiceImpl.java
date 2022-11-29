package com.general.template.auth.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.assembler.SysOrganizationAssembler;
import com.general.template.auth.dto.SysOrganizationDTO;
import com.general.template.auth.service.SysOrganizationService;
import com.general.template.core.PageDTO;
import com.general.template.core.PaginationUtils;
import com.general.template.core.exception.ValidationDataException;
import com.general.template.entity.SysOrganization;
import com.general.template.enums.OrganizationType;
import com.general.template.mapper.SysOrganizationMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 机构 Service 实现
 *
 * @author quantumtso
 */
@Service
@DS("slave")
public class SysOrganizationServiceImpl implements SysOrganizationService {

    @Resource
    SysOrganizationMapper organizationMapper;

    @Resource
    SysOrganizationAssembler organizationAssembler;

    @Override
    public Page<SysOrganizationDTO> getDTOList(PageDTO page, String name) {
        List<SysOrganization> list = getListWithChildren(name);

        Page<SysOrganizationDTO> dtoPage = new Page(page.getPageNo(), page.getPageSize());
        List<SysOrganizationDTO> records = new ArrayList<>();
        list.forEach(entity -> records.add(toDTORecursively(entity)));

        PaginationUtils.pagination(records, dtoPage, Comparator.comparing(SysOrganizationDTO::getCreateTime).reversed());

        return dtoPage;
    }

    @Override
    public List<SysOrganizationDTO> getAllDTOList(String name) {
        List<SysOrganization> nameMatched = getListWithChildren(name);

        List<SysOrganizationDTO> records = new ArrayList<>();
        nameMatched.forEach(entity -> records.add(toDTORecursively(entity)));

        return records;
    }

    @Override
    public SysOrganizationDTO getDTOById(Long id) {
        SysOrganization organization = organizationMapper.selectById(id);
        if (Objects.isNull(organization)) {
            throw new ValidationDataException(String.format("未找到机构：%d", id));
        }
        SysOrganizationDTO dto = organizationAssembler.toDTO(organization);

        SysOrganization organizationByPid = organizationMapper.selectOne(Wrappers.<SysOrganization>lambdaQuery()
                .eq(SysOrganization::getOrgId, dto.getPid()));
        if (Objects.nonNull(organizationByPid)) {
            dto.setPOrgName(organizationByPid.getOrgName());
        }
        return dto;
    }

    @Override
    public void create(SysOrganizationDTO dto, Long currentUserId) {
        validateUniqueOrgName(dto);
        validatePid(dto);

        SysOrganization org = organizationAssembler.toPO(dto);
        org.setOrgType(OrganizationType.DEPARTMENT).setCreateTime(null).setUpdateTime(null)
                .setCreateUser(currentUserId).setUpdateUser(currentUserId);
        organizationMapper.insert(org);
    }

    @Override
    public void update(SysOrganizationDTO dto, Long currentUserId) {
        SysOrganization organizationById = organizationMapper.selectById(dto.getOrgId());
        if (Objects.isNull(organizationById)) {
            throw new ValidationDataException(String.format("未找到机构：%d", dto.getOrgId()));
        }

        validateUniqueOrgName(dto);
        validatePid(dto);

        organizationById.setOrgName(dto.getOrgName())
                .setPid(dto.getPid())
                .setNote(dto.getNote())
                .setUpdateUser(currentUserId);

        organizationMapper.updateById(organizationById);
    }

    @Override
    @Transactional
    public void delete(Long id, Long currentUserId) {
        SysOrganization orgById = organizationMapper.selectById(id);
        if (Objects.isNull(orgById)) {
            throw new ValidationDataException(String.format("未找到系统设置：%d", id));
        }
        orgById.setUpdateUser(currentUserId);
        organizationMapper.deleteById(orgById);
        deleteChildrenRecursively(id);
    }

    private void validateUniqueOrgName(SysOrganizationDTO dto) {
        SysOrganization orgName = organizationMapper.selectOne(Wrappers.<SysOrganization>lambdaQuery()
                .eq(SysOrganization::getOrgName, dto.getOrgName())
                .eq(SysOrganization::getPid, Objects.isNull(dto.getPid()) ? 0L : dto.getPid())
                .ne(Objects.nonNull(dto.getOrgId()), SysOrganization::getOrgId, dto.getOrgId()));

        if (Objects.nonNull(orgName)) {
            throw new ValidationDataException("机构名称已存在");
        }
    }

    private void validatePid(SysOrganizationDTO dto) {
        if (Objects.nonNull(dto.getPid())) {
            SysOrganization organizationByPid = organizationMapper.selectOne(Wrappers.<SysOrganization>lambdaQuery()
                    .eq(SysOrganization::getOrgId, dto.getPid()));
            if (Objects.isNull(organizationByPid)) {
                throw new ValidationDataException(String.format("未找到父级机构：%d", dto.getOrgId()));
            }
        }

        if (Objects.nonNull(dto.getOrgId()) && dto.getOrgId().equals(dto.getPid())) {
            throw new ValidationDataException("父级机构不能为自身");
        }
    }

    private void setChildrenRecursively(SysOrganization organization) {
        List<SysOrganization> children = organizationMapper.selectList(Wrappers.<SysOrganization>lambdaQuery()
                .eq(SysOrganization::getPid, organization.getOrgId()));

        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        organization.setChildren(children);

        //递归
        children.forEach(res -> setChildrenRecursively(res));
    }

    private SysOrganizationDTO toDTORecursively(SysOrganization organization) {
        SysOrganizationDTO organizationDTO = organizationAssembler.toDTO(organization);

        if (CollectionUtils.isEmpty(organization.getChildren())) {
            return organizationDTO;
        }

        //递归
        List<SysOrganizationDTO> children = new ArrayList<>();
        organization.getChildren().forEach(res -> children.add(toDTORecursively(res)));
        organizationDTO.setChildren(children);

        return organizationDTO;
    }

    private List<SysOrganization> getListWithChildren(String name) {
        List<SysOrganization> topOrgs = organizationMapper.selectList(
                Wrappers.<SysOrganization>lambdaQuery().eq(SysOrganization::getPid, 0L));
        List<SysOrganization> nameMatched = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(topOrgs)) {
            topOrgs.forEach(org -> setChildrenRecursively(org));
            for (SysOrganization topOrg : topOrgs) {
                if (isNameMatched(Arrays.asList(topOrg), name)) {
                    nameMatched.add(topOrg);
                }
            }
        }

        return nameMatched;
    }

    private boolean isNameMatched(List<SysOrganization> organizations, String name) {

        if (StringUtils.isBlank(name)) {
            return true;
        }

        for (SysOrganization organization : organizations) {
            if (StringUtils.contains(organization.getOrgName(), name)) {
                return true;
            }
        }

        for (SysOrganization organization : organizations) {
            if (CollectionUtils.isEmpty(organization.getChildren())) {
                continue;
            }
            if (isNameMatched(organization.getChildren(), name)) {
                return true;
            }
        }

        return false;
    }

    private void deleteChildrenRecursively(Long id) {
        List<SysOrganization> children = organizationMapper.selectList(
                Wrappers.<SysOrganization>lambdaQuery().eq(SysOrganization::getPid, id));

        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(org -> deleteChildrenRecursively(org.getOrgId()));
            organizationMapper.deleteBatchIds(children.stream()
                    .map(SysOrganization::getOrgId).collect(Collectors.toList()));
        }
    }
}
