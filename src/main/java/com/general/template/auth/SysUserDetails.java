package com.general.template.auth;


import com.general.template.auth.dto.SysUserDTO;
import com.general.template.infrastructure.ResourceGrantedAuthority;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Objects;


public class SysUserDetails extends User {

    private SysUserDTO userInfo;

    @JsonCreator
    public SysUserDetails(@JsonProperty(value = "username") String username,
                          @JsonProperty(value = "password") String password,
                          @JsonProperty("enabled") boolean enabled,
                          @JsonProperty("accountNonExpired") boolean accountNonExpired,
                          @JsonProperty("credentialsNonExpired") boolean credentialsNonExpired,
                          @JsonProperty("accountNonLocked") boolean accountNonLocked,
                          @JsonProperty("authorities") List<ResourceGrantedAuthority> authorities,
                          @JsonProperty("userInfo") SysUserDTO userInfo) {

        super(username, Objects.isNull(password) ? "" : password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);

        this.setUserInfo(userInfo);
    }

    public SysUserDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SysUserDTO userInfo) {
        this.userInfo = userInfo;
    }

}

