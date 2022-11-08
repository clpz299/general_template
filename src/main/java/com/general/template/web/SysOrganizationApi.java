package com.general.template.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.auth.dto.SysOrganizationDTO;
import com.general.template.auth.dto.SysUserDTO;
import com.general.template.auth.group.CreateAction;
import com.general.template.auth.group.UpdateAction;
import com.general.template.auth.service.SysOrganizationService;
import com.general.template.core.PageDTO;
import com.general.template.core.ResultResponse;
import com.general.template.infrastructure.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys/org")
@Api(value = "【权限】机构管理", tags = "sys_org")
public class SysOrganizationApi {

    @Resource
    SysOrganizationService organizationService;

    @ApiOperation(value = "查询分页机构列表")
    @GetMapping
    public ResultResponse<Page<SysOrganizationDTO>> getList(PageDTO page,
                                                            @RequestParam(required = false) String name) {
        return ResultResponse.of(organizationService.getDTOList(page, name));
    }

    @ApiOperation(value = "查询全部机构列表")
    @GetMapping("/all")
    public ResultResponse<List<SysOrganizationDTO>> getAllList(@RequestParam(required = false) String name) {
        return ResultResponse.of(organizationService.getAllDTOList(name));
    }

    @ApiOperation(value = "查询机构")
    @GetMapping("/{id}")
    public ResultResponse<SysOrganizationDTO> getApi(@PathVariable Long id) {
        return ResultResponse.of(organizationService.getDTOById(id));
    }

    @ApiOperation(value = "新增机构")
    @PostMapping
    public ResultResponse create(@Validated(value = {CreateAction.class}) @RequestBody SysOrganizationDTO dto,
                                 @CurrentUser SysUserDTO user) {
        organizationService.create(dto, user.getUserId());
        return ResultResponse.of();
    }

    @ApiOperation(value = "更新机构")
    @PutMapping
    public ResultResponse update(@Validated(value = {UpdateAction.class}) @RequestBody SysOrganizationDTO dto,
                                 @CurrentUser SysUserDTO user) {
        organizationService.update(dto, user.getUserId());
        return ResultResponse.of();
    }

    @ApiOperation(value = "删除机构")
    @DeleteMapping("/{id}")
    public ResultResponse delete(@PathVariable Long id, @CurrentUser SysUserDTO user) {
        organizationService.delete(id, user.getUserId());
        return ResultResponse.of();
    }
}
