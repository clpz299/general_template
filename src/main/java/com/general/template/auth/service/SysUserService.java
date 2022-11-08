package com.general.template.auth.service;

import com.general.template.auth.dto.SysRoleDTO;
import com.general.template.auth.dto.SysUserDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.template.auth.dto.SysUserPasswordDTO;
import com.general.template.auth.dto.SysUserRoleDTO;
import com.general.template.core.PageDTO;
import com.general.template.entity.SysUser;

import java.util.List;

public interface SysUserService {

    /**
     * 查询用户 DTO 列表
     *
     * @param page    分页
     * @param orgId   机构 ID
     * @param keyword 关键字
     * @return 用户 DTO 列表
     */
    Page<SysUserDTO> getDTOList(PageDTO page, Long orgId, String keyword);

    /**
     * 获取用户授权的用户角色
     *
     * @param userId
     * @return
     */
    List<SysRoleDTO> getRolesByUserId(Long userId);

    /**
     * 查询用户 DTO
     *
     * @param id 用户 ID
     * @return 用户 DTO
     */
    SysUserDTO getDTOById(Long id);

    /**
     * 创建用户
     *
     * @param dto           用户参数
     * @param currentUserId 当前用户 ID
     */
    void create(SysUserDTO dto, Long currentUserId);

    /**
     * 更新用户
     *
     * @param dto           用户参数
     * @param currentUserId 当前用户 ID
     */
    void update(SysUserDTO dto, Long currentUserId);

    /**
     * 设置锁定状态
     *
     * @param userId        用户 ID
     * @param locked        是否锁定
     * @param currentUserId 当前用户 ID
     */
    void setLocked(Long userId, Boolean locked, Long currentUserId);

    /**
     * 删除用户
     *
     * @param userId        用户 ID
     * @param currentUserId 当前用户 ID
     */
    void delete(Long userId, Long currentUserId);

    /**
     * 查询系统用户
     *
     * @param account 账号（用户名、邮箱、手机号）
     * @return 系统用户
     */
    SysUser getUser(String account);

    /**
     * 查询系统用户
     *
     * @param username 用户名
     * @return 系统用户
     */
    SysUser getUserByUserName(String username);

    /**
     * 查询系统用户
     *
     * @param phone 电话
     * @return 系统用户
     */
    SysUser getUserByPhone(String phone);

    /**
     * 查询系统用户
     *
     * @param email 邮箱
     * @return 系统用户
     */
    SysUser getUserByEmail(String email);

    /**
     * 查询用户 ID 所关联的角色 ID 集合
     *
     * @param userId 用户 ID
     * @return 系统角色 ID 集合
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 查询用户 ID 所关联的菜单编码集合
     *
     * @param userId 用户 ID
     * @return 菜单编码集合
     */
    List<String> getResourceCodesById(Long userId);


    /**
     * 查询用户 DTO
     *
     * @param username 用户名
     * @return 用户 DTO
     */
    SysUserDTO getDTOByUsername(String username);


    /**
     * 分配角色
     *
     * @param dto           用户角色关联参数
     * @param currentUserId 当前用户 ID
     */
    void assignRole(SysUserRoleDTO dto, Long currentUserId);

    /**
     * 插入用户
     *
     * @param user 用户
     */
    void insert(SysUser user);

    /**
     * 更新用户最近登录信息
     *
     * @param username 用户名
     */
    void updateUserLastLoginInfo(String username);

    /**
     * 修改密码
     *
     * @param authUserPasswordDTO
     */
    void updatePassword(SysUserPasswordDTO authUserPasswordDTO);
}
