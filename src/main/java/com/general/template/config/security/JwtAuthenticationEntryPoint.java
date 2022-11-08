package com.general.template.config.security;

import com.alibaba.fastjson.JSONObject;
import com.general.template.core.ResultResponse;
import com.general.template.core.ResultResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        //HTTP 编码统一用 200
        httpServletResponse.setStatus(HttpStatus.OK.value());
        ResultResponse result = ResultResponse.of(e.getMessage(), ResultResponseCode.UNAUTHORIZED.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
    }
}
