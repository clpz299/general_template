package com.general.template.core.util;


import com.general.template.enums.LoginType;
import com.general.template.infrastructure.AdminAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;


@Slf4j
@Component
public class JwtTokenUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private static final String USER_DETAILS = "userDetails";
    private static final String SESSION_KEY = "sessionKey";
    private static final String LOGIN_TYPE = "loginType";

    @Resource
    private ObjectMapper mapper;

    @Value("%{jwt.secret}")
    private String secret;

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 生成Token
     *
     * @param authentication
     * @return
     */
    @SneakyThrows
    public String generateToken(UsernamePasswordAuthenticationToken authentication) {

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret);

        if (authentication instanceof AdminAuthenticationToken) {
            AdminAuthenticationToken adminAuthenticationToken = (AdminAuthenticationToken) authentication;
            jwtBuilder.claim(LOGIN_TYPE, LoginType.ADMIN.name())
                    .claim(USER_DETAILS, mapper.writeValueAsString(adminAuthenticationToken.getUserDetails()));
        }


        return jwtBuilder.compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    /**
     * 验证Token
     *
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 获取用户明细
     *
     * @param token
     * @return
     */
    @SneakyThrows
    public <T> T getUserDetailsFromToken(String token, Class<T> clazz) {
        String o = getAllClaimsFromToken(token).get(USER_DETAILS, String.class);
        return mapper.readValue(o, clazz);
    }

    /**
     * 获取小程序SessionKey
     *
     * @param token
     * @return
     */
    public String getSessionKeyFromToken(String token) {
        return getAllClaimsFromToken(token).get(SESSION_KEY, String.class);
    }

    /**
     * 获取登录类型
     *
     * @param token
     * @return
     */
    public LoginType getLoginTypeFromToken(String token) {
        return LoginType.valueOf(getAllClaimsFromToken(token).get(LOGIN_TYPE, String.class));
    }

}
