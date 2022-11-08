package com.general.template.auth.service.impl;

import com.general.template.auth.SysUserDetails;
import com.general.template.auth.dto.SysRoleDTO;
import com.general.template.auth.dto.SysUserDTO;
import com.general.template.auth.service.SysUserService;
import com.general.template.infrastructure.ResourceGrantedAuthority;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

@Service
public class AdminUserDetailsServiceImpl implements UserDetailsService {

    public static final String ROLE_PREFIX = "ROLE_";

    @Resource
    private SysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("账号不能为空");
        }

        SysUserDTO authUserDTO = userService.getDTOByUsername(username);
        if (Objects.isNull(authUserDTO)) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        List<ResourceGrantedAuthority> grantedAuthorities = new ArrayList<>();

        //获取该用户所拥有的权限
        List<String> resourceCodes = userService.getResourceCodesById(authUserDTO.getUserId());
        List<SysRoleDTO> roleDTOS = this.userService.getRolesByUserId(authUserDTO.getUserId());

        // 声明用户授权
        resourceCodes.forEach(resourceCode -> {
            grantedAuthorities.add(new ResourceGrantedAuthority(resourceCode));
        });
        // 声明角色授权
        roleDTOS.forEach(role -> {
            grantedAuthorities.add(new ResourceGrantedAuthority(ROLE_PREFIX + role.getCode()));
        });

        return new SysUserDetails(authUserDTO.getUsername(),
                authUserDTO.getPassword(),
                authUserDTO.getEnabled(),
                true,
                true,
                !authUserDTO.getLocked(),
                grantedAuthorities,
                authUserDTO);
    }

}
