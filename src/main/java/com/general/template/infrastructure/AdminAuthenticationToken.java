package com.general.template.infrastructure;


import com.general.template.auth.SysUserDetails;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class AdminAuthenticationToken extends UsernamePasswordAuthenticationToken {

    SysUserDetails userDetails;

    public AdminAuthenticationToken(@NonNull Object principal, @NonNull Object credentials) {
        super(principal, credentials);
    }

    public AdminAuthenticationToken(@NonNull Object principal, @NonNull Object credentials, @NonNull Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public SysUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(SysUserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
