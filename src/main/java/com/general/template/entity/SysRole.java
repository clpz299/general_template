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

@ApiModel(value = "权限")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_role")
public class SysRole extends BaseEntity implements  Serializable {
    /**
     * 主键
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long roleId;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 角色编码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "角色编码")
    private String code;

    /**
     * 角色描述
     */
    @TableField(value = "note")
    @ApiModelProperty(value = "角色描述")
    private String note;


    private static final long serialVersionUID = 1L;

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_NAME = "name";

    public static final String COL_CODE = "code";

    public static final String COL_NOTE = "note";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_DEL_FLAG = "del_flag";
}
