package com.general.template.web;


import com.general.template.auth.dto.AuthenticationResponseDTO;
import com.general.template.auth.dto.SignInRequestDTO;
import com.general.template.auth.dto.SysUserDTO;
import com.general.template.auth.service.SysLoginLogService;
import com.general.template.auth.service.SysUserService;
import com.general.template.core.ResultResponse;
import com.general.template.core.ResultResponseCode;
import com.general.template.core.util.JwtTokenUtils;
import com.general.template.entity.SysUser;
import com.general.template.infrastructure.AdminAuthenticationToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin
@Api(value = "【权限】登录", tags = "login")
public class LoginApi {

    private static final String LOGIN_FAILURE_TIMES_KEY = "sys:login:failureTimes:";
    private static final String LOGIN_CAPTCHA_KEY = "sys:login:captcha:";


    Integer userLoginMaxFailureTimes = 10000000;

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    JwtTokenUtils jwtTokenUtils;

    @Resource(name = "adminUserDetailsServiceImpl")
    UserDetailsService userDetailsService;

    @Resource
    SysUserService userService;

    @Resource
    SysLoginLogService authLoginLogService;

    @Resource
    BCryptPasswordEncoder passwordEncoder;


    @Resource
    RedisTemplate redisTemplate;


    @ApiOperation("登录")
    @PostMapping("/signIn")
    public ResultResponse<AuthenticationResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO signInRequest) {
        String loginFailureTimesKey = LOGIN_FAILURE_TIMES_KEY + signInRequest.getUsername();

        try {
            // 判断是否需要输入验证码
//            Integer loginFailureTimes = (Integer) redisTemplate.opsForValue().get(loginFailureTimesKey);
//            if (Objects.nonNull(loginFailureTimes) && loginFailureTimes >= userLoginMaxFailureTimes) {
//
//                if (StringUtils.isBlank(signInRequest.getCaptcha())) {
//                    return new ResultResponse<>(ResultResponseCode.BAD_REQUEST.value(), "验证码已过期",
//                            new AuthenticationResponseDTO(null, true));
//                }
//
//                String loginCaptchaKey = LOGIN_CAPTCHA_KEY + signInRequest.getUsername();
//                if (!Optional.ofNullable(redisTemplate.opsForValue().get(loginCaptchaKey))
//                        .map(String::valueOf)
//                        .map(c -> c.equals(signInRequest.getCaptcha().toLowerCase()))
//                        .orElse(false)) {
//                    return new ResultResponse(ResultResponseCode.BAD_REQUEST.value(), "验证码已过期",
//                            new AuthenticationResponseDTO(null, true));
//                }
//            }

            // 登录认证
            String token = authenticate(signInRequest.getUsername(), passwordEncoder.encode(signInRequest.getPassword()));
//            String token = "111";

            // 登录成功
            userService.updateUserLastLoginInfo(signInRequest.getUsername());

            // 记录登录日志
            SysUser user = userService.getUserByUserName(signInRequest.getUsername());
            authLoginLogService.add(user.getUserId(), user.getUsername());

            // 清除缓存
            redisTemplate.delete(loginFailureTimesKey);

            log.info("用户登录, username: {}, token: {}", signInRequest.getUsername(), token);
            return ResultResponse.of(new AuthenticationResponseDTO(token, false));

        } catch (BadCredentialsException e) {

            // 记录登录错误次数
            Integer loginFailureTimes = redisTemplate.opsForValue().increment(loginFailureTimesKey).intValue();

            if (Objects.nonNull(loginFailureTimes) && loginFailureTimes >= userLoginMaxFailureTimes) {
                // 需要验证码登录
                return ResultResponse.of(ResultResponseCode.BAD_REQUEST, new AuthenticationResponseDTO(null, true));
            }
            return ResultResponse.of(ResultResponseCode.BAD_REQUEST, new AuthenticationResponseDTO(null, false));
        }
    }


    @ApiOperation("获取当前用户信息")
    @GetMapping("/current")
    public ResultResponse current() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUserDTO user = userService.getDTOByUsername(username);
        return ResultResponse.of(user);
    }

    @ApiOperation("获取用户权限")
    @GetMapping("/permissions")
    public ResultResponse<List<String>> permissions() {

        List<String> perms = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResultResponse.of(perms);
    }

//    @ApiOperation("图形验证码")
//    @GetMapping("/captcha")
//    public void buildCaptcha(@RequestParam String username, HttpServletResponse response) {
//        // 生成验证码
//        String text = kaptchaProducer.createText();
//        BufferedImage image = kaptchaProducer.createImage(text);
//
//        // 将验证码存入缓存,有效时间30秒
//        redisTemplate.opsForValue().set(LOGIN_CAPTCHA_KEY + username, text.toLowerCase(), 60L, TimeUnit.SECONDS);
//
//        // 将突图片输出给浏览器
//        response.setContentType("image/png");
//        try {
//            OutputStream os = response.getOutputStream();
//            ImageIO.write(image, "png", os);
//        } catch (IOException e) {
//            log.error("生成图形验证码失败:" + e.getMessage());
//        }
//    }

    private String authenticate(String username, String password) {
        authenticationManager.authenticate(new AdminAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtils.generateToken(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
        return token;
    }

}
