package com.general.template.config.security;

import com.alibaba.fastjson.JSONObject;
import com.general.template.core.ResultResponse;
import com.general.template.core.ResultResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限拒绝处理器
 *
 * @author quantumtso
 */
@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException {
        //HTTP 编码统一用 200
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResultResponse result = ResultResponse.of(ResultResponseCode.FORBIDDEN);
        httpServletResponse.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
    }
}
