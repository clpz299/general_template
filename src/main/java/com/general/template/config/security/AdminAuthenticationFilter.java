package com.general.template.config.security;

import com.alibaba.fastjson.JSONObject;
import com.general.template.auth.dto.SignInRequestDTO;
import com.general.template.core.exception.SysSignInException;
import com.general.template.infrastructure.AdminAuthenticationToken;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class AdminAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final String LOGIN_FAILURE_TIMES_KEY = "sys:login:failureTimes:";
    private static final String LOGIN_CAPTCHA_KEY = "sys:login:captcha:";
    private static final Integer LOGIN_MAX_FAILURE_TIMES = 3;

    public AdminAuthenticationFilter() {
        // 拦截路径
        super(new AntPathRequestMatcher("/signIn", "POST"));
        super.setAuthenticationSuccessHandler(new AdminAuthenticationSuccessHandler());
        super.setAuthenticationFailureHandler(new AdminAuthenticationFailureHandler());
        setAuthenticationManager(SpringContextHolder.getBean(AuthenticationManager.class));
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // body校验
        HttpServletRequest requestWrapper = new MultiReadHttpServletRequest(request);
        String body = IOUtils.toString(requestWrapper.getInputStream(), CharsetNames.UTF_8);
        if (StringUtils.isBlank(body)) {
            throw new AuthenticationServiceException("Request body must not be empty or null");
        }

        // 参数校验
        SignInRequestDTO signInRequest = JSONObject.parseObject(body, SignInRequestDTO.class);
        Set<ConstraintViolation<SignInRequestDTO>> validate = validator.validate(signInRequest);
        validate.forEach(v -> {
            throw new SysSignInException(signInRequest, v.getMessage());
        });

        // 设置参数，方便向下传递
        request.setAttribute("requestDTO", signInRequest);

        // 判断是否需要输入验证码
        RedisTemplate redisTemplate = SpringContextHolder.getBean("redisTemplate");
        String loginFailureTimesKey = LOGIN_FAILURE_TIMES_KEY + signInRequest.getUsername();

        Integer loginFailureTimes = (Integer) redisTemplate.opsForValue().get(loginFailureTimesKey);
        if (Objects.nonNull(loginFailureTimes) && loginFailureTimes >= LOGIN_MAX_FAILURE_TIMES) {

            if (StringUtils.isBlank(signInRequest.getCaptcha())) {
                throw new AuthenticationServiceException("请输入验证码");
            }

            String loginCaptchaKey = LOGIN_CAPTCHA_KEY + signInRequest.getUsername();
//            if (!Optional.ofNullable(redisTemplate.opsForValue().get(loginCaptchaKey))
//                    .map(String::valueOf)
//                    .map(c -> c.equals(signInRequest.getCaptcha().toLowerCase()))
//                    .orElse(false)) {
//                throw new AuthenticationServiceException("验证码已过期");
//            }
        }

        AdminAuthenticationToken authRequest = new AdminAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());

        // Allow subclasses to set the "details" property
        authRequest.setDetails(authenticationDetailsSource.buildDetails(requestWrapper));

        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
