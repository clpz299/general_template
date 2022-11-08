package com.general.template.config.security;

import com.general.template.auth.SysUserDetails;
import com.general.template.infrastructure.AdminAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


public class AdminAuthenticationProvider extends DaoAuthenticationProvider {

    /**
     * 初始化 将使用Manager专用的userDetailsService
     *
     * @param encoder
     * @param userDetailsService
     */
    public AdminAuthenticationProvider(PasswordEncoder encoder, UserDetailsService userDetailsService) {
        setPasswordEncoder(encoder);
        setUserDetailsService(userDetailsService);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        AdminAuthenticationToken authenticationToken = (AdminAuthenticationToken) authentication;
        authenticationToken.setUserDetails((SysUserDetails) user);
        return authenticationToken;
    }

    /**
     * 判断只有传入AdminAuthenticationToken的时候才使用这个Provider
     * supports会在AuthenticationManager层被调用
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return AdminAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
