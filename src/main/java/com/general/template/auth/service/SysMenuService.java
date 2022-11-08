package com.general.template.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.auth.dto.SysApiDTO;
import com.general.template.auth.dto.SysMenuDTO;
import com.general.template.core.PageDTO;
import com.general.template.entity.SysMenu;

import java.util.List;

public interface SysMenuService {

    /**
     * 查询菜单 DTO 列表
     *
     * @param page    分页
     * @param keyword 关键字
     * @return 菜单 DTO 列表
     */
    Page<SysMenuDTO> getDTOList(PageDTO page, String keyword);

    /**
     * 查询菜单 DTO 列表
     *
     * @return 菜单 DTO 列表
     */
    List<SysMenuDTO> getAllMenuDTOList();

    /**
     * 查询菜单 DTO
     *
     * @param id 菜单 ID
     * @return 菜单 DTO
     */
    SysMenuDTO getDTOById(Long id);

    /**
     * 创建菜单
     *
     * @param dto           菜单参数
     * @param currentUserId 当前用户 ID
     */
    void create(SysMenuDTO dto, Long currentUserId);

    /**
     * 更新菜单
     *
     * @param dto           菜单参数
     * @param currentUserId 当前用户 ID
     */
    void update(SysMenuDTO dto, Long currentUserId);

    /**
     * 删除菜单
     *
     * @param id 菜单 ID
     */
    void delete(Long id);

    /**
     * 增加接口关联
     *
     * @param menuId         菜单 ID
     * @param apiId         接口 ID
     * @param currentUserId 当前用户 ID
     */
    void relateApi(Long menuId, Long apiId, Long currentUserId);

    /**
     * 删除接口关联
     *
     * @param menuId         菜单 ID
     * @param apiId         接口 ID
     * @param currentUserId 当前用户 ID
     */
    void unrelateApi(Long menuId, Long apiId, Long currentUserId);

    /**
     * 查询接口 ID 关联角色的菜单列表
     *
     * @param apiId 接口 ID
     * @return 菜单列表
     */
    List<SysMenu> getMenusByApiId(Long apiId);

    /**
     * 查询菜单 ID 集合所对应的菜单编码集合
     *
     * @param resourceIds 菜单 ID 集合
     * @return 菜单编码集合
     */
    List<String> getMenuCodesByIds(List<Long> resourceIds);

    /**
     * 查询已关联/未关联的接口 DTO 列表
     *
     * @param menuId   菜单 ID
     * @param related 是否关联
     * @param page    分页
     * @param method  请求方法
     * @param url     请求路径
     * @param keyword 接口名称
     * @return 接口 DTO 列表
     */
    Page<SysApiDTO> getApiDTOList(Long menuId, Boolean related, PageDTO page, String method, String url, String keyword);
}
