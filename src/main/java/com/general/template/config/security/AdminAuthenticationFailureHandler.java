package com.general.template.config.security;

import com.alibaba.fastjson.JSONObject;
import com.general.template.auth.dto.AuthenticationResponseDTO;
import com.general.template.auth.dto.SignInRequestDTO;
import com.general.template.core.ResultResponse;
import com.general.template.core.ResultResponseCode;
import org.apache.commons.compress.utils.CharsetNames;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class AdminAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String LOGIN_FAILURE_TIMES_KEY = "sys:login:failureTimes:";
    private static final Integer LOGIN_MAX_FAILURE_TIMES = 3;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        ResultResponse<AuthenticationResponseDTO> responseEntity;

        // 参数
        SignInRequestDTO signInRequest = (SignInRequestDTO) request.getAttribute("requestDTO");

        RedisTemplate redisTemplate = SpringContextHolder.getBean("redisTemplate");

        String loginFailureTimesKey = LOGIN_FAILURE_TIMES_KEY + signInRequest.getUsername();

        // 记录登录错误次数
        int loginFailureTimes = redisTemplate.opsForValue().increment(loginFailureTimesKey).intValue();

        if (Objects.nonNull(loginFailureTimes) && loginFailureTimes >= LOGIN_MAX_FAILURE_TIMES) {
            // 需要验证码登录
            responseEntity = new ResultResponse<AuthenticationResponseDTO>(ResultResponseCode.BAD_REQUEST.value(),
                    exception.getMessage(),
                    new AuthenticationResponseDTO(null, true));
        } else {
            responseEntity = new ResultResponse<AuthenticationResponseDTO>(ResultResponseCode.BAD_REQUEST.value(),
                    exception.getMessage(),
                    new AuthenticationResponseDTO(null, false));
        }

        response.setCharacterEncoding(CharsetNames.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter writer = response.getWriter();
        writer.write(JSONObject.toJSONString(responseEntity));
    }
}
