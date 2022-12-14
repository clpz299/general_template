package com.general.template.config.security;


import com.general.template.auth.SysUserDetails;
import com.general.template.core.util.JwtTokenUtils;
import com.general.template.enums.LoginType;
import com.general.template.infrastructure.AdminAuthenticationToken;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // Token åįž
        String tokenPrefix = "Bearer ";

        if (requestTokenHeader != null && requestTokenHeader.startsWith(tokenPrefix)) {
            jwtToken = requestTokenHeader.substring(tokenPrefix.length());
            try {
                username = jwtTokenUtils.getSubjectFromToken(jwtToken);
            } catch (JwtException e) {
                logger.error("JWT Token æ æ", e);
                SecurityContextHolder.clearContext();
                this.authenticationEntryPoint.commence(request, response, new AccountExpiredException(e.getMessage(), e));
                return;
            }
        }

        //æ ĄéŠ Token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            LoginType loginType = jwtTokenUtils.getLoginTypeFromToken(jwtToken);
            UsernamePasswordAuthenticationToken authenticationToken = null;

            // įŪĄįįŦŊ
            if (LoginType.ADMIN.equals(loginType)) {
                SysUserDetails adminUserDetails = jwtTokenUtils.getUserDetailsFromToken(jwtToken, SysUserDetails.class);
                authenticationToken = new AdminAuthenticationToken(adminUserDetails.getUsername(),
                        adminUserDetails.getPassword(),
                        adminUserDetails.getAuthorities());
                ((AdminAuthenticationToken) authenticationToken).setUserDetails(adminUserDetails);
            }


            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // čŪūį―Ū authentication äŋĄæŊ
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }

}
