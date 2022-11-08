package com.general.template.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.general.template.core.BaseEntity;
import com.general.template.enums.ApiScope;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel(value = "sys_api")
@Data
@TableName(value = "sys_api")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysApi extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "api_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long apiId;

    /**
     * 接口名称
     */
    @TableField(value = "api_name")
    @ApiModelProperty(value = "接口名称")
    private String apiName;

    /**
     * 接口描述
     */
    @TableField(value = "note")
    @ApiModelProperty(value = "接口描述")
    private String note;

    /**
     * 范围
     */
    @TableField(value = "`scope`")
    @ApiModelProperty(value = "范围")
    private ApiScope scope;

    /**
     * 方法
     */
    @TableField(value = "`method`")
    @ApiModelProperty(value = "方法")
    private String method;

    /**
     * 路径
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "路径")
    private String url;

    /**
     * 参数
     */
    @TableField(value = "params")
    @ApiModelProperty(value = "参数")
    private String params;


    private static final long serialVersionUID = 1L;

    public static final String COL_API_ID = "api_id";

    public static final String COL_API_NAME = "api_name";

    public static final String COL_NOTE = "note";

    public static final String COL_SCOPE = "scope";

    public static final String COL_METHOD = "method";

    public static final String COL_URL = "url";

    public static final String COL_PARAMS = "params";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_DEL_FLAG = "del_flag";
}
