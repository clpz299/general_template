package com.general.template.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.auth.dto.SysOrganizationDTO;
import com.general.template.core.PageDTO;

import java.util.List;

public interface SysOrganizationService {

    /**
     * 查询机构 DTO 列表
     *
     * @param page 分页
     * @param name 机构名称
     * @return 机构 DTO 列表
     */
    Page<SysOrganizationDTO> getDTOList(PageDTO page, String name);

    /**
     * 查询机构 DTO 列表
     *
     * @param name 机构名称
     * @return 机构 DTO 列表
     */
    List<SysOrganizationDTO> getAllDTOList(String name);

    /**
     * 查询机构 DTO
     *
     * @param id 机构 ID
     * @return 机构 DTO
     */
    SysOrganizationDTO getDTOById(Long id);

    /**
     * 新增机构
     *
     * @param dto           参数
     * @param currentUserId 当前用户ID
     */
    void create(SysOrganizationDTO dto, Long currentUserId);

    /**
     * 更新机构
     *
     * @param dto           参数
     * @param currentUserId 当前用户ID
     */
    void update(SysOrganizationDTO dto, Long currentUserId);

    /**
     * 删除机构
     *
     * @param id            系统设置 ID
     * @param currentUserId 当前用户ID
     */
    void delete(Long id, Long currentUserId);
}
