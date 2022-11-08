package com.general.template.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.general.template.core.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;


@ApiModel(value = "sys_role_menu_relation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_role_menu_relation")
public class SysRoleMenuRelation extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "relation_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long relationId;

    /**
     * 权限id
     */
    @TableField(value = "role_id")
    @ApiModelProperty(value = "权限id")
    private Long roleId;

    /**
     * 菜单id
     */
    @TableField(value = "menu_id")
    @ApiModelProperty(value = "菜单id")
    private Long menuId;


    private static final long serialVersionUID = 1L;

    public static final String COL_RELATION_ID = "relation_id";

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_menu_id = "menu_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_DEL_FLAG = "del_flag";
}
