package com.general.template;

import com.alibaba.fastjson.JSONObject;
import com.general.template.auth.dto.SysUserDTO;
import com.general.template.auth.service.SysUserService;
import com.general.template.utils.CommonlyLocalDateTimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
