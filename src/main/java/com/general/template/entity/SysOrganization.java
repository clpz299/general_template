package com.general.template.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.general.template.core.BaseEntity;
import com.general.template.enums.OrganizationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel(value = "sys_organization")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_organization")
public class SysOrganization extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "org_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long orgId;

    /**
     * 父id
     */
    @TableField(value = "pid")
    @ApiModelProperty(value = "父id")
    private Long pid;

    /**
     * 机构名称
     */
    @TableField(value = "org_name")
    @ApiModelProperty(value = "机构名称")
    private String orgName;


    /**
     * 机构类型
     */
    private OrganizationType orgType;


    /**
     * 描述
     */
    @TableField(value = "note")
    @ApiModelProperty(value = "描述")
    private String note;

    /**
     * 子级机构
     */
    @TableField(exist = false)
    private List<SysOrganization> children;


    private static final long serialVersionUID = 1L;

    public static final String COL_ORG_ID = "org_id";

    public static final String COL_PID = "pid";

    public static final String COL_ORG_NAME = "org_name";

    public static final String COL_ORG_TYPE_ID = "org_type_id";

    public static final String COL_NOTE = "note";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_DEL_FLAG = "del_flag";
}
