package com.general.template.config.security;


import com.alibaba.fastjson.JSONObject;
import com.general.template.auth.dto.AuthenticationResponseDTO;
import com.general.template.auth.dto.SignInRequestDTO;
import com.general.template.auth.service.SysLoginLogService;
import com.general.template.auth.service.SysUserService;
import com.general.template.core.ResultResponse;
import com.general.template.core.util.JwtTokenUtils;
import com.general.template.entity.SysUser;
import com.general.template.infrastructure.AdminAuthenticationToken;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.CharsetNames;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String LOGIN_FAILURE_TIMES_KEY = "sys:login:failureTimes:";

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        RedisTemplate redisTemplate = SpringContextHolder.getBean("redisTemplate");
        JwtTokenUtils jwtTokenUtils = SpringContextHolder.getBean(JwtTokenUtils.class);
        SysUserService authUserService = SpringContextHolder.getBean(SysUserService.class);
        SysLoginLogService authLoginLogService = SpringContextHolder.getBean(SysLoginLogService.class);

        // 请求参数
        AdminAuthenticationToken adminSysenticationToken = (AdminAuthenticationToken) authentication;
        SignInRequestDTO signInRequest = (SignInRequestDTO) request.getAttribute("requestDTO");

        // 登录成功
        authUserService.updateUserLastLoginInfo(signInRequest.getUsername());

        // 记录登录日志
        SysUser user = authUserService.getUserByUserName(signInRequest.getUsername());
        authLoginLogService.add(user.getUserId(), user.getUsername());

        // 清除缓存
        String loginFailureTimesKey = LOGIN_FAILURE_TIMES_KEY + signInRequest.getUsername();
        redisTemplate.delete(loginFailureTimesKey);

        // 生成Token
        String token = jwtTokenUtils.generateToken(adminSysenticationToken);

        ResultResponse<AuthenticationResponseDTO> responseEntity = ResultResponse.of(new AuthenticationResponseDTO(token, false));

        response.setCharacterEncoding(CharsetNames.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter writer = response.getWriter();
        writer.write(JSONObject.toJSONString(responseEntity));
    }

}
