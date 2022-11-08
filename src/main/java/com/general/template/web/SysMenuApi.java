package com.general.template.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.auth.dto.SysApiDTO;
import com.general.template.auth.dto.SysMenuDTO;
import com.general.template.auth.dto.SysUserDTO;
import com.general.template.auth.group.CreateAction;
import com.general.template.auth.service.SysMenuService;
import com.general.template.core.PageDTO;
import com.general.template.core.ResultResponse;
import com.general.template.infrastructure.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sys/menu")
@Api(value = "【权限】菜单管理", tags = "sys_menu")
public class SysMenuApi {

    @Resource
    SysMenuService resourceService;

    @GetMapping
    @ApiOperation(value = "查询列表")
    public ResultResponse<Page<SysMenuDTO>> getList(PageDTO page, @RequestParam(required = false) String keyword) {
        return ResultResponse.of(resourceService.getDTOList(page, keyword));
    }

    @GetMapping("/all")
    @ApiOperation(value = "查询所有菜单")
    public ResultResponse<List<SysMenuDTO>> getAllMenu() {
        return ResultResponse.of(resourceService.getAllMenuDTOList());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询菜单")
    public ResultResponse<SysMenuDTO> getById(@PathVariable Long id) {
        return ResultResponse.of(resourceService.getDTOById(id));
    }

    @PostMapping
    @ApiOperation(value = "创建菜单")
    public ResultResponse create(@Validated(value = {CreateAction.class}) @RequestBody SysMenuDTO dto,
                                 @CurrentUser SysUserDTO user) {
        resourceService.create(dto, user.getUserId());
        return ResultResponse.of();
    }

    @PutMapping
    @ApiOperation(value = "更新菜单")
    public ResultResponse update(@CurrentUser SysUserDTO user, @RequestBody SysMenuDTO dto) {
        resourceService.update(dto, user.getUserId());
        return ResultResponse.of();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除菜单")
    public ResultResponse delete(@PathVariable Long id) {
        resourceService.delete(id);
        return ResultResponse.of();
    }

    @GetMapping("/apis")
    @ApiOperation(value = "查询关联/未关联接口列表")
    public ResultResponse<Page<SysApiDTO>> getApiDTOList(PageDTO page,
                                                         @RequestParam Long resId,
                                                         @RequestParam Boolean related,
                                                         @RequestParam(required = false) String method,
                                                         @RequestParam(required = false) String url,
                                                         @RequestParam(required = false) String keyword) {
        return ResultResponse.of(resourceService.getApiDTOList(resId, related, page, method, url, keyword));
    }

    @PostMapping("/apis")
    @ApiOperation(value = "新增接口关联")
    public ResultResponse relateApi(@CurrentUser SysUserDTO user, @RequestParam Long resId, @RequestParam Long apiId) {
        resourceService.relateApi(resId, apiId, user.getUserId());
        return ResultResponse.of();
    }

    @DeleteMapping("/apis")
    @ApiOperation(value = "删除接口关联")
    public ResultResponse unrelateApi(@CurrentUser SysUserDTO user, @RequestParam Long resId, @RequestParam Long apiId) {
        resourceService.unrelateApi(resId, apiId, user.getUserId());
        return ResultResponse.of();
    }
}
