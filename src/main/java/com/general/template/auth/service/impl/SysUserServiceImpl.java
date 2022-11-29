package com.general.template.auth.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.assembler.SysUserAssembler;
import com.general.template.auth.dto.SysRoleDTO;
import com.general.template.auth.dto.SysUserDTO;
import com.general.template.auth.dto.SysUserPasswordDTO;
import com.general.template.auth.dto.SysUserRoleDTO;
import com.general.template.auth.service.SysOrganizationService;
import com.general.template.auth.service.SysRoleService;
import com.general.template.auth.service.SysUserService;
import com.general.template.core.PageDTO;
import com.general.template.core.exception.MeteorologicException;
import com.general.template.core.exception.ValidationDataException;
import com.general.template.entity.SysUser;
import com.general.template.entity.SysUserRoleRelation;
import com.general.template.mapper.SysUserMapper;
import com.general.template.mapper.SysUserRoleRelationMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@DS("slave")
public class SysUserServiceImpl implements SysUserService {

    private String defaultPassword = "123456";

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysUserAssembler authUserAssembler;

    @Resource
    SysUserRoleRelationMapper userRoleRelationMapper;

    @Resource
    SysOrganizationService organizationService;

    @Resource
    SysRoleService roleService;

    @Resource
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public Page<SysUserDTO> getDTOList(PageDTO page, Long orgId, String keyword) {
        Page<SysUser> users = sysUserMapper.selectPage(page.toPage(), Wrappers.<SysUser>lambdaQuery()
                .eq(Objects.nonNull(orgId), SysUser::getOrgId, orgId)
                .and(StringUtils.isNotBlank(keyword), wrapper -> wrapper.like(SysUser::getUserId, keyword)
                        .or().like(SysUser::getUsername, keyword)
                        .or().like(SysUser::getPhone, keyword)));

        Page<SysUserDTO> userDTOs = new Page<>();
        BeanUtils.copyProperties(users, userDTOs);
        userDTOs.setRecords(authUserAssembler.toDTOs(users.getRecords()));

        userDTOs.getRecords().forEach(user -> {
            user.setRoleNames(
                    getRolesByUserId(user.getUserId()).stream()
                            .map(SysRoleDTO::getName)
                            .collect(Collectors.toList())
            );
            user.setOrgName(organizationService.getDTOById(user.getOrgId()).getOrgName());
        });

        return userDTOs;
    }

    @Override
    public List<SysRoleDTO> getRolesByUserId(Long userId) {
        List<SysUserRoleRelation> relations = userRoleRelationMapper.selectList(
                Wrappers.<SysUserRoleRelation>lambdaQuery().eq(SysUserRoleRelation::getUserId, userId));

        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }

        List<Long> roleIds = relations.stream()
                .map(SysUserRoleRelation::getRoleId)
                .collect(Collectors.toList());

        return roleService.getRolesByRoleIds(roleIds);
    }

    @Override
    public SysUserDTO getDTOById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (Objects.isNull(user)) {
            throw new MeteorologicException(String.format("未找到用户：%d", id));
        }

        List<SysUserRoleRelation> relations = userRoleRelationMapper.selectList(
                Wrappers.<SysUserRoleRelation>lambdaQuery().eq(SysUserRoleRelation::getUserId, id));
        if (CollectionUtils.isNotEmpty(relations)) {
            List<Long> roleIds = relations.stream().map(SysUserRoleRelation::getRoleId).collect(Collectors.toList());
            user.setRoleIds(roleIds);
        }

        return authUserAssembler.toDTO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SysUserDTO dto, Long currentUserId) {
        List<SysUser> users = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, dto.getUsername())
                .or().eq(SysUser::getEmail, dto.getEmail())
                .or().eq(SysUser::getPhone, dto.getPhone()));
        if (CollectionUtils.isNotEmpty(users)) {
            throw new ValidationDataException("用户名、邮箱或手机号已被使用");
        }

        if (Objects.isNull(organizationService.getDTOById(dto.getOrgId()))) {
            throw new ValidationDataException(String.format("未找到机构：%d", dto.getOrgId()));
        }

        SysUser user = new SysUser();
        user.setOrgId(dto.getOrgId())
                .setUsername(dto.getUsername())
                .setPassword(passwordEncoder.encode(
                        StringUtils.isBlank(dto.getPassword()) ? defaultPassword : dto.getPassword()))
                .setFullName(dto.getFullName())
                .setPhone(dto.getPhone())
                .setEmail(dto.getEmail())
                .setLocked(false).setEnabled(true)
                .setCreateUser(currentUserId)
                .setUpdateUser(currentUserId);
        sysUserMapper.insert(user);

        Long userId = user.getUserId();
        dto.getRoleIds().forEach(roleId -> saveRelation(userId, roleId, currentUserId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserDTO dto, Long currentUserId) {
        SysUser userById = sysUserMapper.selectById(dto.getUserId());
        if (Objects.isNull(userById)) {
            throw new ValidationDataException(String.format("未找到用户：%d", dto.getUserId()));
        }

        List<SysUser> users = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                .ne(SysUser::getUserId, dto.getUserId())
                .and(wrapper -> wrapper.eq(SysUser::getUsername, dto.getUsername()).or().eq(SysUser::getEmail, dto.getEmail())
                        .or().eq(SysUser::getPhone, dto.getPhone())));
        if (CollectionUtils.isNotEmpty(users)) {
            throw new ValidationDataException("用户名、邮箱或手机号已被使用");
        }

        if (Objects.isNull(organizationService.getDTOById(dto.getOrgId()))) {
            throw new ValidationDataException(String.format("未找到机构：%d", dto.getOrgId()));
        }

        userById.setOrgId(dto.getOrgId())
                .setUsername(dto.getUsername())
                .setFullName(dto.getFullName())
                .setPhone(dto.getPhone())
                .setEmail(dto.getEmail())
                .setCreateUser(currentUserId)
                .setUpdateUser(currentUserId);

        if (StringUtils.isNotBlank(dto.getPassword())) {
            userById.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        sysUserMapper.updateById(userById);

        userRoleRelationMapper.delete(Wrappers.<SysUserRoleRelation>lambdaUpdate()
                .eq(SysUserRoleRelation::getUserId, dto.getUserId()));

        dto.getRoleIds().forEach(roleId -> saveRelation(dto.getUserId(), roleId, currentUserId));
    }

    @Override
    public void setLocked(Long userId, Boolean locked, Long currentUserId) {
        SysUser userById = sysUserMapper.selectById(userId);
        if (Objects.isNull(userById)) {
            throw new ValidationDataException(String.format("未找到用户：%d", userId));
        }

        userById.setLocked(locked).setUpdateUser(currentUserId);
        sysUserMapper.updateById(userById);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long currentUserId) {
        SysUser userById = sysUserMapper.selectById(userId);
        if (Objects.isNull(userById)) {
            throw new ValidationDataException(String.format("未找到用户：%d", userId));
        }

        sysUserMapper.deleteById(userById);
        userRoleRelationMapper.delete(Wrappers.<SysUserRoleRelation>lambdaUpdate()
                .eq(SysUserRoleRelation::getUserId, userId));
    }

    @Override
    public SysUser getUser(String account) {
        return sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, account)
                .or().eq(SysUser::getPhone, account)
                .or().eq(SysUser::getEmail, account));
    }

    @Override
    public SysUser getUserByUserName(String username) {
        return sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
    }

    @Override
    public SysUser getUserByPhone(String phone) {
        return sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getPhone, phone));
    }

    @Override
    public SysUser getUserByEmail(String email) {
        return sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getEmail, email));
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        List<SysUserRoleRelation> relations = userRoleRelationMapper.selectList(
                Wrappers.<SysUserRoleRelation>lambdaQuery().eq(SysUserRoleRelation::getUserId, userId));

        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }

        return relations.stream().map(SysUserRoleRelation::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<String> getResourceCodesById(Long userId) {
        List<Long> roleIds = getRoleIdsByUserId(userId);
        return roleService.getResourceCodesByRoleIds(roleIds);
    }

    @Override
    public SysUserDTO getDTOByUsername(String username) {
        SysUser user = getUserByUserName(username);
        if (Objects.isNull(user)) {
            throw new MeteorologicException(String.format("未找到用户：%s", username));
        }

        SysUserDTO authUserDTO = authUserAssembler.toDTO(user);
        authUserDTO.setRoleCodes(
                this.getRolesByUserId(user.getUserId()).stream()
                        .map(SysRoleDTO::getCode)
                        .collect(Collectors.toList())
        );

        return authUserDTO;
    }

    @Override
    public void assignRole(SysUserRoleDTO dto, Long currentUserId) {
        SysUser user = sysUserMapper.selectById(dto.getUserId());

        if (Objects.isNull(user)) {
            throw new MeteorologicException(String.format("未找到用户：%d", dto.getUserId()));
        }

        //删除历史关联数据
        userRoleRelationMapper.delete(Wrappers.<SysUserRoleRelation>lambdaUpdate()
                .eq(SysUserRoleRelation::getUserId, dto.getUserId()));
        dto.getRoleIds().forEach(roleId -> saveRelation(dto.getUserId(), roleId, currentUserId));
    }

    @Override
    public void insert(SysUser user) {
        sysUserMapper.insert(user);
    }

    @Override
    public void updateUserLastLoginInfo(String username) {
        SysUser sysUser = this.getUser(username);
        sysUser.setLastLoginTime(LocalDateTime.now()).setUpdateUser(sysUser.getUserId());
        sysUserMapper.updateById(sysUser);
    }

    @Override
    public void updatePassword(SysUserPasswordDTO authUserPasswordDTO) {
        SysUser user = sysUserMapper.selectById(authUserPasswordDTO.getUserId());
        if (Objects.isNull(user)) {
            throw new ValidationDataException(String.format("未找到用户：%d", authUserPasswordDTO.getUserId()));
        }

        if (!passwordEncoder.matches(authUserPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new ValidationDataException("原密码有误");
        }

        user.setPassword(passwordEncoder.encode(authUserPasswordDTO.getNewPassword()));

        sysUserMapper.updateById(user);
    }

    private void saveRelation(Long userId, Long roleId, Long currentUserId) {
        SysUserRoleRelation relation = new SysUserRoleRelation();
        relation.setUserId(userId).setRoleId(roleId).setCreateUser(currentUserId);
        userRoleRelationMapper.insert(relation);
    }
}
