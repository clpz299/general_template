package com.general.template.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.general.template.core.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel(value = "登录日志")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_login_log")
public class SysLoginLog extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "login_record_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long loginRecordId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 登录ip
     */
    @TableField(value = "ip")
    @ApiModelProperty(value = "登录ip")
    private String ip;

    /**
     * user-agent
     */
    @TableField(value = "ua")
    @ApiModelProperty(value = "user-agent")
    private String ua;


    private static final long serialVersionUID = 1L;

    public static final String COL_LOGIN_RECORD_ID = "login_record_id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_USERNAME = "username";

    public static final String COL_IP = "ip";

    public static final String COL_UA = "ua";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_DEL_FLAG = "del_flag";
}
