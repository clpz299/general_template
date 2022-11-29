package com.general.template.auth.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.general.template.auth.query.SysLoginLogQuery;
import com.general.template.auth.service.SysLoginLogService;
import com.general.template.core.PageDTO;
import com.general.template.core.util.WebUtils;
import com.general.template.entity.SysLoginLog;
import com.general.template.mapper.SysLoginLogMapper;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@DS("slave")
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public Boolean add(@NonNull Long userId, @NonNull String username) {
        SysLoginLog authLoginLog = new SysLoginLog();
        authLoginLog.setUserId(userId);
        authLoginLog.setUsername(username);
        authLoginLog.setIp(WebUtils.getIP());
        authLoginLog.setUa(WebUtils.getUserAgent());
        authLoginLog.setCreateUser(userId);
        authLoginLog.setUpdateUser(userId);
        return this.save(authLoginLog);
    }

    @Override
    public Page<SysLoginLog> page(PageDTO pageDTO, @NonNull SysLoginLogQuery query) {
        Page<SysLoginLog> logPage = this.baseMapper.selectPage(pageDTO.toPage(),
                Wrappers.<SysLoginLog>lambdaQuery()
                        .eq(Objects.nonNull(query.getUserId()), SysLoginLog::getUserId, query.getUserId())
                        .between(ObjectUtils.allNotNull(query.getStartTime(), query.getEndTime()),
                                SysLoginLog::getCreateTime,
                                query.getStartTime(), query.getEndTime())
                        .orderByDesc(SysLoginLog::getCreateTime)
        );
        return logPage;
    }
}
