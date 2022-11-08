package com.general.template.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.general.template.core.BaseEntity;
import com.general.template.enums.MenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel(value = "sys_menu")
@Data
@TableName(value = "sys_menu")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer menuId;

    /**
     * 菜单名称
     */
    @TableField(value = "res_name")
    @ApiModelProperty(value = "菜单名称")
    private String resName;

    /**
     * 菜单编码
     */
    @TableField(value = "res_code")
    @ApiModelProperty(value = "菜单编码")
    private String resCode;

    /**
     * 菜单类型
     */
    @TableField(value = "res_type")
    @ApiModelProperty(value = "菜单类型")
    private MenuType menuType;

    /**
     * 描述
     */
    @TableField(value = "note")
    @ApiModelProperty(value = "描述")
    private String note;

    /**
     * 图标
     */
    @TableField(value = "icon")
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 父id
     */
    @TableField(value = "pid")
    @ApiModelProperty(value = "父id")
    private Long pid;

    /**
     * 路径
     */
    @TableField(value = "`path`")
    @ApiModelProperty(value = "路径")
    private String path;

    /**
     * 等级
     */
    @TableField(value = "`level`")
    @ApiModelProperty(value = "等级")
    private Integer level;

    /**
     * 排序
     */
    @TableField(value = "sort")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 子级菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children;


    private static final long serialVersionUID = 1L;

    public static final String COL_menu_id = "menu_id";

    public static final String COL_RES_NAME = "res_name";

    public static final String COL_RES_CODE = "res_code";

    public static final String COL_RES_TYPE = "res_type";

    public static final String COL_NOTE = "note";

    public static final String COL_ICON = "icon";

    public static final String COL_PID = "pid";

    public static final String COL_PATH = "path";

    public static final String COL_LEVEL = "level";

    public static final String COL_SORT = "sort";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_DEL_FLAG = "del_flag";
}
