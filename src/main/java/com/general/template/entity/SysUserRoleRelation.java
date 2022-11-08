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

@ApiModel(value = "用户权限关联")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_user_role_relation")
public class SysUserRoleRelation extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "relation_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long relationId;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户编号")
    private Long userId;

    /**
     * 权限编号
     */
    @TableField(value = "role_id")
    @ApiModelProperty(value = "权限编号")
    private Long roleId;


    private static final long serialVersionUID = 1L;

    public static final String COL_RELATION_ID = "relation_id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_DEL_FLAG = "del_flag";
}
