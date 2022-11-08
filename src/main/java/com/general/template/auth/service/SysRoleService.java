package com.general.template.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.general.template.auth.dto.RoleOperateDTO;
import com.general.template.auth.dto.RoleResOperateDTO;
import com.general.template.auth.dto.RoleSelectDTO;
import com.general.template.auth.dto.SysRoleDTO;
import com.general.template.core.PageDTO;
import com.general.template.core.ResultResponse;
import com.general.template.entity.SysRole;
import lombok.NonNull;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     */
    ResultResponse<Page<SysRoleDTO>> listRole(PageDTO pageDTO, RoleSelectDTO roleSelectDTO);

    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    List<SysRoleDTO> getAllRoles();

    /**
     * 通过id获取角色dto
     * @param roleId
     * @return
     */
    SysRoleDTO getRoleDTOById(@NonNull Long roleId);

    /**
     * 查看角色
     */
    ResultResponse<SysRoleDTO> showRole(Long roleId);

    /**
     * 新增角色
     */
    ResultResponse addRole(RoleOperateDTO roleOperateDTO, Long currentUserId);

    /**
     * 更新角色
     */
    ResultResponse updateRole(RoleOperateDTO roleOperateDTO, Long currentUserId);

    /**
     * 操作角色菜单关联
     */
    ResultResponse operateResource(RoleResOperateDTO roleResOperateDTO, Long currentUserId);

    /**
     * 获取角色关联的菜单集合
     *
     * @return
     */
    ResultResponse<SysRoleDTO> getRoleResources(Long roleId);

    /**
     * 批量删除角色
     */
    ResultResponse removeRole(Long[] roleIds);

    /**
     * 查询角色 ID 集合所关联的菜单编码集合
     *
     * @param roleIds 角色 ID 集合
     * @return 菜单编码集合
     */
    List<String> getResourceCodesByRoleIds(List<Long> roleIds);

    /**
     * 查询角色 ID 集合所对应对角色集合
     *
     * @param roleIds 角色 ID 集合
     * @return 角色集合
     */
    List<SysRoleDTO> getRolesByRoleIds(List<Long> roleIds);

}
