package com.general.template.auth.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.general.template.assembler.SysRoleAssembler;
import com.general.template.auth.dto.RoleOperateDTO;
import com.general.template.auth.dto.RoleResOperateDTO;
import com.general.template.auth.dto.RoleSelectDTO;
import com.general.template.auth.dto.SysRoleDTO;
import com.general.template.auth.service.SysMenuService;
import com.general.template.auth.service.SysRoleService;
import com.general.template.core.PageDTO;
import com.general.template.core.ResultResponse;
import com.general.template.entity.SysRole;
import com.general.template.entity.SysRoleMenuRelation;
import com.general.template.mapper.SysRoleMapper;
import com.general.template.mapper.SysRoleMenuRelationMapper;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
@DS("slave")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuRelationMapper roleResourceRelationMapper;

    @Resource
    private SysRoleAssembler roleAssembler;

    @Resource
    private SysMenuService menuService;

    @Override
    public ResultResponse<Page<SysRoleDTO>> listRole(PageDTO pageDTO, RoleSelectDTO roleSelectDTO) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper<SysRole>()
                .like(Objects.nonNull(roleSelectDTO.getName()), SysRole::getName, roleSelectDTO.getName())
                .like(Objects.nonNull(roleSelectDTO.getCode()), SysRole::getCode, roleSelectDTO.getCode())
                .orderByDesc(SysRole::getUpdateTime);
        Page<SysRole> page = sysRoleMapper.selectPage(new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize()), lambdaQueryWrapper);

        List<SysRoleDTO> roleDTOList = Lists.newArrayList();
        Page<SysRoleDTO> pageInfo = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(role -> {
                roleDTOList.add(roleAssembler.toDTO(role));
            });
            pageInfo.setRecords(roleDTOList);
        }
        return ResultResponse.of(pageInfo);
    }

    @Override
    public List<SysRoleDTO> getAllRoles() {
        List<SysRole> list = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery().orderByAsc(SysRole::getCreateTime));
        return roleAssembler.toDTOs(list);
    }

    @Override
    public SysRoleDTO getRoleDTOById(@NonNull Long roleId) {

        SysRole authRole = this.sysRoleMapper.selectById(roleId);

        return this.roleAssembler.toDTO(authRole);
    }

    @Override
    public ResultResponse<SysRoleDTO> showRole(Long roleId) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (Objects.nonNull(role)) {
            return ResultResponse.of(roleAssembler.toDTO(role));
        }
        return ResultResponse.error("角色不存在！");
    }

    @Override
    public ResultResponse addRole(RoleOperateDTO roleOperateDTO, Long currentUserId) {
        if (isExistRole(roleOperateDTO)) {
            return ResultResponse.error("角色名称或编码已存在！");
        }
        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleOperateDTO, role);
        role.setCreateUser(currentUserId);
        sysRoleMapper.insert(role);
        return ResultResponse.of();
    }

    @Override
    public ResultResponse updateRole(RoleOperateDTO roleOperateDTO, Long currentUserId) {
        if (isExistRole(roleOperateDTO)) {
            return ResultResponse.error("角色名称或编码已存在！");
        }
        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleOperateDTO, role);
        role.setUpdateUser(currentUserId);
        sysRoleMapper.updateById(role);
        return ResultResponse.of();
    }

    @Override
    @Transactional
    public ResultResponse operateResource(RoleResOperateDTO roleResOperateDTO, Long currentUserId) {
        // 删除角色原关联的菜单
        roleResourceRelationMapper.delete(new LambdaQueryWrapper<SysRoleMenuRelation>()
                .eq(SysRoleMenuRelation::getRoleId, roleResOperateDTO.getRoleId()));
        // 新增新关联的菜单
        if (CollectionUtils.isNotEmpty(roleResOperateDTO.getResourceIds())) {
            roleResOperateDTO.getResourceIds().forEach(resourceId -> {
                SysRoleMenuRelation roleResourceRelation = SysRoleMenuRelation.builder()
                        .roleId(roleResOperateDTO.getRoleId()).menuId(resourceId).build();
                roleResourceRelation.setCreateUser(currentUserId);
                roleResourceRelationMapper.insert(roleResourceRelation);
            });
        }
        return ResultResponse.of();
    }

    @Override
    public ResultResponse<SysRoleDTO> getRoleResources(Long roleId) {
        SysRole authRole = getById(roleId);
        if (Objects.isNull(authRole)) {
            return ResultResponse.error("角色不存在");
        }

        SysRoleDTO roleDTO = roleAssembler.toDTO(authRole);

        List<SysRoleMenuRelation> resourceList = roleResourceRelationMapper.selectList(Wrappers.<SysRoleMenuRelation>lambdaQuery()
                .eq(SysRoleMenuRelation::getRoleId, roleId));
        roleDTO.setResourceList(CollectionUtils.isEmpty(resourceList) ? null : resourceList.stream().map(item -> item.getMenuId()).collect(Collectors.toList()));

        return ResultResponse.of(roleDTO);
    }

    @Override
    public ResultResponse removeRole(Long[] roleIds) {
        if (roleIds != null && roleIds.length > 0) {
            List<Long> roleList = new ArrayList<>(Arrays.asList(roleIds));
            sysRoleMapper.deleteBatchIds(roleList);
        }
        return ResultResponse.of();
    }

    @Override
    public List<String> getResourceCodesByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<SysRoleMenuRelation> relations = roleResourceRelationMapper
                .selectList(Wrappers.<SysRoleMenuRelation>lambdaQuery()
                        .in(SysRoleMenuRelation::getRoleId, roleIds));

        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }

        List<Long> resourceIds = relations.stream().map(SysRoleMenuRelation::getMenuId).collect(Collectors.toList());

        return menuService.getMenuCodesByIds(resourceIds);
    }

    @Override
    public List<SysRoleDTO> getRolesByRoleIds(List<Long> roleIds) {

        List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);

        return this.roleAssembler.toDTOs(roles);
    }

    /**
     * 是否存在角色名称或者编码
     *
     * @return
     */
    private boolean isExistRole(RoleOperateDTO roleOperateDTO) {
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<SysRole>()
                .ne(Objects.nonNull(roleOperateDTO.getRoleId()), SysRole::getRoleId, roleOperateDTO.getRoleId())
                .and(wrapper -> wrapper.eq(SysRole::getName, roleOperateDTO.getName())
                        .or().eq(SysRole::getCode, roleOperateDTO.getCode()));
        Integer count = sysRoleMapper.selectCount(queryWrapper);
        if (Objects.nonNull(count) && count > 0) {
            return true;
        }
        return false;


    }

}
