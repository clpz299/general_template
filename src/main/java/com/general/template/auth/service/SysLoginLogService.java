package com.general.template.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.auth.query.SysLoginLogQuery;
import com.general.template.core.PageDTO;
import com.general.template.entity.SysLoginLog;
import lombok.NonNull;

public interface SysLoginLogService {

    /**
     * 新增登录记录
     *
     * @param userId
     * @param username
     * @return
     */
    Boolean add(@NonNull Long userId, @NonNull String username);

    /**
     * 分页查询
     *
     * @param pageDTO
     * @param query
     * @return
     */
    Page<SysLoginLog> page(PageDTO pageDTO, @NonNull SysLoginLogQuery query);

}
