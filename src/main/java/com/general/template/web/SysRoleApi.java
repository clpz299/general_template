package com.general.template.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.auth.dto.*;
import com.general.template.auth.service.SysRoleService;
import com.general.template.core.PageDTO;
import com.general.template.core.ResultResponse;
import com.general.template.infrastructure.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api(value = "【权限】角色管理", tags = "sys_role")
@RestController
@RequestMapping("/sys/role")
public class SysRoleApi {

    @Autowired
    private SysRoleService roleService;

    @GetMapping("/list")
    @ApiOperation("查询角色列表")
    public ResultResponse<Page<SysRoleDTO>> roleListByPage(PageDTO pageDTO, RoleSelectDTO roleSelectDTO) {
        return roleService.listRole(pageDTO, roleSelectDTO);
    }

    @GetMapping("/list/all")
    @ApiOperation("查询所有角色列表")
    public ResultResponse<List<SysRoleDTO>> getAllRoleListByPage() {
        return ResultResponse.of(roleService.getAllRoles());
    }

    @GetMapping("/show/{roleId}")
    @ApiOperation("查看角色信息")
    public ResultResponse<SysRoleDTO> showRole(@ApiParam("角色ID") @PathVariable Long roleId) {
        return roleService.showRole(roleId);
    }

    @PostMapping("/operate")
    @ApiOperation("添加/更新角色")
    public ResultResponse operateRole(@CurrentUser SysUserDTO user, @Validated RoleOperateDTO roleOperateDTO) {
        if (Objects.isNull(roleOperateDTO.getRoleId())) {
            return roleService.addRole(roleOperateDTO, user.getUserId());
        } else {
            return roleService.updateRole(roleOperateDTO, user.getUserId());
        }
    }

    @PostMapping("/operateMenu")
    @ApiOperation("更新角色关联菜单")
    public ResultResponse operateResource(@CurrentUser SysUserDTO user, @Validated RoleResOperateDTO roleResOperateDTO) {
        return roleService.operateResource(roleResOperateDTO, user.getUserId());
    }

    @GetMapping("/menu/{roleId}")
    @ApiOperation("角色关联的菜单集合")
    public ResultResponse<SysRoleDTO> showRoleResources(@ApiParam("角色ID") @PathVariable Long roleId) {
        return roleService.getRoleResources(roleId);
    }

    @PostMapping("/remove")
    @ApiOperation("批量删除角色")
    public ResultResponse removeRole(Long[] roleIds) {
        return roleService.removeRole(roleIds);
    }
}
