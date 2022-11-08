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

@ApiModel(value = "sys_menu_api_relation")
@Data
@TableName(value = "sys_menu_api_relation")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysMenuApiRelation extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "relation_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long relationId;

    /**
     * 菜单id
     */
    @TableField(value = "menu_id")
    @ApiModelProperty(value = "菜单id")
    private Long menuId;

    /**
     * 接口id
     */
    @TableField(value = "api_id")
    @ApiModelProperty(value = "接口id")
    private Long apiId;


    private static final long serialVersionUID = 1L;

    public static final String COL_RELATION_ID = "relation_id";

    public static final String COL_MENU_ID = "menu_id";

    public static final String COL_API_ID = "api_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE = "update";

    public static final String COL_DEL_FLAG = "del_flag";
}
