package com.general.template.config.security;

import com.general.template.auth.service.SysApiService;
import com.general.template.auth.service.SysMenuService;
import com.general.template.entity.SysApi;
import com.general.template.entity.SysMenu;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.*;

@Component
public class CustomizeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    public static Set<String> urlPatterns = new TreeSet<>();

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    SysApiService sysApiService;

    @Autowired
    SysMenuService sysMenuService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(urlPatterns)) {
            urlPatterns = sysApiService.getAllUrlPatterns();
        }

        FilterInvocation filterInvocation = (FilterInvocation) o;

        String requestUrl = filterInvocation.getRequest().getServletPath();
        String method = filterInvocation.getRequest().getMethod();

        String urlPattern = matchPattern(requestUrl);
        SysApi sysApi = sysApiService.getByUrlAndMethod(urlPattern, method);

        //接口不存在，可以任意访问
        if (Objects.isNull(sysApi)) {
            return null;
        }

        List<SysMenu> resources = sysMenuService.getMenusByApiId(sysApi.getApiId());

        //接口没有关联菜单，可以任意访问
        if (CollectionUtils.isEmpty(resources)) {
            return null;
        }

        String[] attributes = resources.stream().map(SysMenu::getResCode).toArray(String[]::new);
        return SecurityConfig.createList(attributes);
    }

    private String matchPattern(String requestUrl) {
        if (CollectionUtils.isNotEmpty(urlPatterns)) {
            for (String urlPattern : urlPatterns) {
                if (antPathMatcher.match(urlPattern, requestUrl)) {
                    return urlPattern;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
