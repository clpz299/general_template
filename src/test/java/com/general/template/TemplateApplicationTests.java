package com.general.template;

import com.general.template.auth.dto.SysUserDTO;
import com.general.template.auth.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TemplateApplicationTests {

    @Resource
    private SysUserService service;

    @Test
    void contextLoads() {
        List<Long> roles = new ArrayList<>();
        roles.add(1L);
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setUsername("admin");
        sysUserDTO.setPassword("123456");
        sysUserDTO.setEnabled(true);
        sysUserDTO.setEmail("clpz@.qq.com");
        sysUserDTO.setLocked(false);
        sysUserDTO.setFullName("clpz");
        sysUserDTO.setOrgId(1L);
        sysUserDTO.setRoleIds(roles);
        service.create(sysUserDTO, null);
    }

}
