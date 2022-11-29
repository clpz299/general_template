package com.general.template.auth.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.assembler.SysApiAssembler;
import com.general.template.auth.dto.SysApiDTO;
import com.general.template.auth.service.SysApiService;
import com.general.template.core.PageDTO;
import com.general.template.core.exception.MeteorologicException;
import com.general.template.core.exception.ValidationDataException;
import com.general.template.entity.SysApi;
import com.general.template.enums.ApiScope;
import com.general.template.mapper.SysApiMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@DS("slave")
public class SysApiServiceImpl implements SysApiService {

    @Resource
    SysApiMapper apiMapper;

    @Resource
    SysApiAssembler apiAssembler;

    @Autowired
    WebApplicationContext applicationContext;

    @Override
    public Page<SysApiDTO> getList(PageDTO page, List<Long> ids, String method, String url, String name) {
        Page<SysApi> apis = apiMapper.selectPage(page.toPage(), Wrappers.<SysApi>lambdaQuery()
                .eq(StringUtils.isNotBlank(method), SysApi::getMethod, method)
                .like(StringUtils.isNotBlank(url), SysApi::getUrl, url)
                .like(StringUtils.isNotBlank(name), SysApi::getApiName, name)
                .in(CollectionUtils.isNotEmpty(ids), SysApi::getApiId, ids));

        Page<SysApiDTO> apiDTOs = new Page<>();
        BeanUtils.copyProperties(apis, apiDTOs);
        apiDTOs.setRecords(apiAssembler.toDTOs(apis.getRecords()));
        return apiDTOs;
    }

    @Override
    public SysApiDTO getApi(Long id) {
        SysApi api = apiMapper.selectById(id);
        if (Objects.isNull(api)) {
            throw new MeteorologicException(String.format("未找到接口：%d", id));
        }

        return apiAssembler.toDTO(api);
    }

    @Override
    public void update(SysApiDTO dto, Long currentUserId) {
        SysApi apiById = apiMapper.selectById(dto.getApiId());
        if (Objects.isNull(apiById)) {
            throw new ValidationDataException(String.format("未找到接口：%d", dto.getApiId()));
        }

        SysApi settingByCode = apiMapper.selectOne(Wrappers.<SysApi>lambdaQuery()
                .eq(SysApi::getApiName, dto.getApiName())
                .ne(SysApi::getApiId, dto.getApiId()));
        if (Objects.nonNull(settingByCode)) {
            throw new ValidationDataException("接口名称已存在");
        }

        apiById.setApiName(dto.getApiName())
                .setScope(dto.getScope())
                .setNote(dto.getNote())
                .setUpdateUser(currentUserId);

        apiMapper.updateById(apiById);
    }

    @Override
    public void scanApis() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.forEach((mappingInfo, handlerMethod) -> {

            Set<String> patterns = mappingInfo.getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();

            patterns.forEach(url -> {
                SysApi sysApi = apiMapper.selectOne(Wrappers.<SysApi>lambdaQuery()
                        .eq(SysApi::getUrl, url)
                        .eq(SysApi::getMethod, StringUtils.join(methods, ",")));
                String apiTag = Optional
                        .ofNullable(handlerMethod.getBeanType().getAnnotation(Api.class))
                        .map(Api::tags)
                        .filter(tags -> tags.length > 0)
                        .map(tags -> tags[0] + " - ")
                        .orElse("");
                String apiDesc = Optional
                        .ofNullable(handlerMethod.getMethodAnnotation(ApiOperation.class))
                        .map(ApiOperation::value)
                        .orElse("");

                if (Objects.isNull(sysApi)) {
                    sysApi = new SysApi()
                            .setApiName(apiTag + apiDesc)
                            .setNote(apiTag)
                            .setScope(ApiScope.PRIVATE)
                            .setMethod(StringUtils.join(methods, ","))
                            .setUrl(url);
                    apiMapper.insert(sysApi);
                }

            });

        });
    }

    @Override
    public List<SysApiDTO> getListByMenuId(List<Long> ids) {
        List<SysApi> sysApis = apiMapper.selectBatchIds(ids);
        return apiAssembler.toDTOs(sysApis);
    }


    @Override
    public SysApi getByUrlAndMethod(String url, String method) {
        return apiMapper.selectOne(Wrappers.<SysApi>lambdaQuery()
                .eq(SysApi::getUrl, url)
                .and(i -> i.isNull(SysApi::getMethod).or().eq(SysApi::getMethod, method)));
    }

    @Override
    public Set<String> getAllUrlPatterns() {
        return apiMapper.selectList(Wrappers.emptyWrapper()).stream().map(SysApi::getUrl)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Long> getAllIds() {
        return apiMapper.selectList(Wrappers.emptyWrapper())
                .stream().map(SysApi::getApiId).collect(Collectors.toList());
    }

}
