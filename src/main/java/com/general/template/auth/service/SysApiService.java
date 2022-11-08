package com.general.template.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.auth.dto.SysApiDTO;
import com.general.template.core.PageDTO;
import com.general.template.entity.SysApi;

import java.util.List;
import java.util.Set;

public interface SysApiService {

    /**
     * 查询接口 DTO 列表
     *
     * @param page   分页
     * @param ids    ID 列表
     * @param method 请求方法
     * @param url    请求路径
     * @param name   接口名称
     * @return 接口 DTO 列表
     */
    Page<SysApiDTO> getList(PageDTO page, List<Long> ids, String method, String url, String name);

    /**
     * 查询接口 DTO
     *
     * @param id 接口 ID
     * @return 接口 DTO
     */
    SysApiDTO getApi(Long id);

    /**
     * 更新接口（名称、范围、描述）
     *
     * @param dto           参数对象
     * @param currentUserId
     */
    void update(SysApiDTO dto, Long currentUserId);

    /**
     * 扫描接口
     */
    void scanApis();

    /**
     * 查询接口 DTO 列表（根据 ID 集合）
     *
     * @param ids ID 集合
     * @return 接口 DTO 列表
     */
    List<SysApiDTO> getListByMenuId(List<Long> ids);

    /**
     * 查询接口（根据 url 和 方法）
     *
     * @param url    url
     * @param method 方法
     * @return 接口
     */
    SysApi getByUrlAndMethod(String url, String method);

    /**
     * 获取所有 URL
     *
     * @return URL 集合
     */
    Set<String> getAllUrlPatterns();

    /**
     * 获取所有接口 ID
     *
     * @return 接口 ID 集合
     */
    List<Long> getAllIds();
}
