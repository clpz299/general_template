package com.general.template.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.general.template.core.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel(value = "用户")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_user")
public class SysUser extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long userId;

    /**
     * 组织编号
     */
    @TableField(value = "org_id")
    @ApiModelProperty(value = "组织编号")
    private Long orgId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "`password`")
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 全名
     */
    @TableField(value = "full_name")
    @ApiModelProperty(value = "全名")
    private String fullName;

    /**
     * 手机号码
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "手机号码")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 是否锁定
     */
    @TableField(value = "locked")
    @ApiModelProperty(value = "是否锁定")
    private Boolean locked;

    /**
     * 是否可用
     */
    @TableField(value = "enabled")
    @ApiModelProperty(value = "是否可用")
    private Boolean enabled;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_login_time")
    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;

    /**
     * 角色 ID 集合
     */
    @TableField(exist = false)
    private List<Long> roleIds;


    private static final long serialVersionUID = 1L;

    public static final String COL_USER_ID = "user_id";

    public static final String COL_ORG_ID = "org_id";

    public static final String COL_USERNAME = "username";

    public static final String COL_PASSWORD = "password";

    public static final String COL_FULL_NAME = "full_name";

    public static final String COL_PHONE = "phone";

    public static final String COL_EMAIL = "email";

    public static final String COL_LOCKED = "locked";

    public static final String COL_ENABLED = "enabled";

    public static final String COL_LAST_LOGIN_TIME = "last_login_time";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_DEL_FLAG = "del_flag";
}
