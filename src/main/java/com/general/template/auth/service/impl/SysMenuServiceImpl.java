package com.general.template.auth.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.assembler.SysMenuAssembler;
import com.general.template.auth.dto.SysApiDTO;
import com.general.template.auth.dto.SysMenuDTO;
import com.general.template.auth.service.SysApiService;
import com.general.template.auth.service.SysMenuService;
import com.general.template.core.PageDTO;
import com.general.template.core.PaginationUtils;
import com.general.template.core.exception.ValidationDataException;
import com.general.template.entity.SysMenu;
import com.general.template.entity.SysMenuApiRelation;
import com.general.template.enums.MenuType;
import com.general.template.mapper.SysMenuApiRelationMapper;
import com.general.template.mapper.SysMenuMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@DS("slave")
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    SysMenuMapper sysMenuMapper;

    @Resource
    SysMenuApiRelationMapper menuApiRelationMapper;

    @Resource
    SysMenuAssembler menuAssembler;

    @Resource
    SysApiService apiService;

    @Override
    public Page<SysMenuDTO> getDTOList(PageDTO page, String keyword) {
        List<SysMenu> list = getListWithChildren(keyword, false);

        Page<SysMenuDTO> dtoPage = new Page(page.getPageNo(), page.getPageSize());
        List<SysMenuDTO> records = new ArrayList<>();
        list.forEach(entity -> records.add(toDTORecursively(entity)));

        PaginationUtils.pagination(records, dtoPage, Comparator.comparing(SysMenuDTO::getSort));

        return dtoPage;
    }

    @Override
    public List<SysMenuDTO> getAllMenuDTOList() {
        List<SysMenu> list = getListWithChildren(null, true);
        List<SysMenuDTO> dtos = new ArrayList<>();
        list.forEach(entity -> dtos.add(toDTORecursively(entity)));
        return dtos;
    }

    @Override
    public SysMenuDTO getDTOById(Long id) {
        SysMenu sysMenus = sysMenuMapper.selectById(id);

        if (Objects.isNull(sysMenus)) {
            throw new ValidationDataException(String.format("找不到菜单：%d", id));
        }

        setChildrenRecursively(sysMenus, false);
        SysMenuDTO dto = toDTORecursively(sysMenus);
        if (MenuType.FUNCTION.equals(dto.getResType())) {
            List<SysMenuApiRelation> relations = menuApiRelationMapper.selectList(
                    Wrappers.<SysMenuApiRelation>lambdaQuery().eq(SysMenuApiRelation::getMenuId, dto.getMenuId()));
            if (CollectionUtils.isNotEmpty(relations)) {
                List<Long> apiIds = relations.stream().map(SysMenuApiRelation::getApiId).collect(Collectors.toList());
                dto.setApis(apiService.getListByMenuId(apiIds));
            }
        }
        return dto;
    }

    @Override
    public void create(SysMenuDTO dto, Long currentUserId) {
        validateUniqueResCode(dto);
        validateUniqueResName(dto);
        validatePid(dto);

        SysMenu sysMenus = menuAssembler.toPO(dto);
        sysMenus.setCreateUser(currentUserId).setUpdateUser(currentUserId);
        sysMenuMapper.insert(sysMenus);
    }

    @Override
    public void update(SysMenuDTO dto, Long currentUserId) {
        SysMenu sysMenus = sysMenuMapper.selectById(dto.getMenuId());
        if (Objects.isNull(sysMenus)) {
            throw new ValidationDataException(String.format("未找到菜单：%d", dto.getMenuId()));
        }

        validateUniqueResCode(dto);
        validateUniqueResName(dto);
        validatePid(dto);

        SysMenu resource = sysMenuMapper.selectById(dto.getMenuId());
        resource.setResName(dto.getResName()).setResCode(dto.getResCode()).setMenuType(dto.getResType())
                .setPid(dto.getPid()).setPath(dto.getPath()).setSort(dto.getSort()).setUpdateUser(currentUserId);
        sysMenuMapper.updateById(resource);
    }

    @Override
    public void delete(Long id) {
        sysMenuMapper.deleteById(id);
        menuApiRelationMapper.delete(Wrappers.<SysMenuApiRelation>lambdaUpdate()
                .eq(SysMenuApiRelation::getMenuId, id));
    }

    @Override
    public void relateApi(Long menuId, Long apiId, Long currentUserId) {
        SysMenu resource = sysMenuMapper.selectById(menuId);
        if (Objects.isNull(resource)) {
            throw new ValidationDataException(String.format("未找到菜单：%d", menuId));
        }

        if (!MenuType.FUNCTION.equals(resource.getMenuType())) {
            throw new ValidationDataException("当前菜单不是功能，不能分配接口");
        }

        SysMenuApiRelation relation = menuApiRelationMapper.selectOne(
                Wrappers.<SysMenuApiRelation>lambdaQuery()
                        .eq(SysMenuApiRelation::getMenuId, menuId).eq(SysMenuApiRelation::getApiId, apiId));
        if (Objects.nonNull(relation)) {
            throw new ValidationDataException("关联已存在");
        }

        saveRelation(menuId, apiId, currentUserId);
    }

    @Override
    public void unrelateApi(Long menuId, Long apiId, Long currentUserId) {
        SysMenu resource = sysMenuMapper.selectById(menuId);
        if (Objects.isNull(resource)) {
            throw new ValidationDataException(String.format("未找到菜单：%d", menuId));
        }

        if (!MenuType.FUNCTION.equals(resource.getMenuType())) {
            throw new ValidationDataException("当前菜单不是功能，不能分配接口");
        }

        deleteRelation(menuId, apiId, currentUserId);
    }

    @Override
    public List<SysMenu> getMenusByApiId(Long apiId) {
        List<SysMenuApiRelation> relations = menuApiRelationMapper.selectList(
                Wrappers.<SysMenuApiRelation>lambdaQuery().eq(SysMenuApiRelation::getApiId, apiId));

        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }

        List<Long> resourceIds = relations.stream().map(SysMenuApiRelation::getMenuId).collect(Collectors.toList());
        return sysMenuMapper.selectBatchIds(resourceIds);
    }

    @Override
    public List<String> getMenuCodesByIds(List<Long> resourceIds) {
        List<SysMenu> resources = sysMenuMapper.selectBatchIds(resourceIds);

        if (CollectionUtils.isEmpty(resources)) {
            return Collections.emptyList();
        }

        return resources.stream().map(SysMenu::getResCode).collect(Collectors.toList());
    }

    @Override
    public Page<SysApiDTO> getApiDTOList(Long menuId, Boolean related, PageDTO page, String method, String url, String keyword) {
        Page<SysApiDTO> result = new Page(page.getPageNo(), page.getPageSize());
        result.setTotal(0L).setRecords(Collections.emptyList());

        if (Objects.isNull(sysMenuMapper.selectById(menuId))) {
            return result;
        }

        List<Long> ids;
        if (related) {
            ids = menuApiRelationMapper.selectList(Wrappers.<SysMenuApiRelation>lambdaQuery()
                            .eq(SysMenuApiRelation::getMenuId, menuId))
                    .stream().map(SysMenuApiRelation::getApiId).collect(Collectors.toList());
        } else {
            List<Long> relatedIds = menuApiRelationMapper.selectList(
                            Wrappers.<SysMenuApiRelation>lambdaQuery().eq(SysMenuApiRelation::getMenuId, menuId))
                    .stream().map(SysMenuApiRelation::getApiId).collect(Collectors.toList());

            List<Long> allIds = apiService.getAllIds();
            ids = allIds.stream().filter(id -> !relatedIds.contains(id)).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(ids)) {
            return result;
        }

        return apiService.getList(page, ids, method, url, keyword);
    }

    private List<SysMenu> getListWithChildren(String name, boolean noFunction) {
        List<SysMenu> topMenus = sysMenuMapper.selectList(
                Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getPid, 0L));
        List<SysMenu> nameMatched = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(topMenus)) {
            topMenus.forEach(org -> setChildrenRecursively(org, noFunction));
            for (SysMenu topOrg : topMenus) {
                if (isNameMatched(Arrays.asList(topOrg), name)) {
                    nameMatched.add(topOrg);
                }
            }
        }

        return nameMatched;
    }

    private boolean isNameMatched(List<SysMenu> organizations, String name) {

        if (StringUtils.isBlank(name)) {
            return true;
        }

        for (SysMenu organization : organizations) {
            if (StringUtils.contains(organization.getResName(), name)) {
                return true;
            }
        }

        for (SysMenu organization : organizations) {
            if (CollectionUtils.isEmpty(organization.getChildren())) {
                continue;
            }
            if (isNameMatched(organization.getChildren(), name)) {
                return true;
            }
        }

        return false;
    }

    private void setChildrenRecursively(SysMenu resource, boolean noFunction) {
        List<SysMenu> children = sysMenuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getPid, resource.getMenuId())
                .eq(noFunction, SysMenu::getMenuType, MenuType.MENU));

        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        resource.setChildren(children);

        //递归
        children.forEach(res -> setChildrenRecursively(res, noFunction));
    }

    private SysMenuDTO toDTORecursively(SysMenu resource) {
        SysMenuDTO resourceDTO = menuAssembler.toDTO(resource);

        if (CollectionUtils.isEmpty(resource.getChildren())) {
            return resourceDTO;
        }

        //递归
        List<SysMenuDTO> children = new ArrayList<>();
        resource.getChildren().forEach(res -> children.add(toDTORecursively(res)));
        resourceDTO.setChildren(children);

        return resourceDTO;
    }

    private void saveRelation(Long menuId, Long apiId, Long currentUserId) {
        SysMenuApiRelation relation = new SysMenuApiRelation();
        relation.setMenuId(menuId).setApiId(apiId).setCreateUser(currentUserId).setUpdateUser(currentUserId);
        menuApiRelationMapper.insert(relation);
    }

    private void deleteRelation(Long menuId, Long apiId, Long currentUserId) {
        SysMenuApiRelation relation = menuApiRelationMapper.selectOne(
                Wrappers.<SysMenuApiRelation>lambdaQuery()
                        .eq(SysMenuApiRelation::getMenuId, menuId).eq(SysMenuApiRelation::getApiId, apiId));
        if (Objects.isNull(relation)) {
            throw new ValidationDataException("关联不存在");
        }

        relation.setUpdateUser(currentUserId);
        menuApiRelationMapper.deleteById(relation);
    }

    private void validateUniqueResCode(SysMenuDTO dto) {
        SysMenu settingByCode = sysMenuMapper.selectOne(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getResCode, dto.getResCode())
                .ne(Objects.nonNull(dto.getMenuId()), SysMenu::getMenuId, dto.getMenuId()));

        if (Objects.nonNull(settingByCode)) {
            throw new ValidationDataException("编码已存在");
        }
    }

    private void validateUniqueResName(SysMenuDTO dto) {
        SysMenu orgName = sysMenuMapper.selectOne(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getResName, dto.getResName())
                .eq(SysMenu::getPid, Objects.isNull(dto.getPid()) ? 0L : dto.getPid())
                .ne(Objects.nonNull(dto.getMenuId()), SysMenu::getMenuId, dto.getMenuId()));

        if (Objects.nonNull(orgName)) {
            throw new ValidationDataException("名称已存在");
        }
    }

    private void validatePid(SysMenuDTO dto) {
        if (Objects.nonNull(dto.getPid())) {
            SysMenu organizationByPid = sysMenuMapper.selectOne(Wrappers.<SysMenu>lambdaQuery()
                    .eq(SysMenu::getMenuId, dto.getPid()));
            if (Objects.isNull(organizationByPid)) {
                throw new ValidationDataException(String.format("未找到上级菜单：%d", dto.getMenuId()));
            }
        }
    }
}

