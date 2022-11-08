package com.general.template.core;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;


@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel("基础实体类")
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -6100906301506454259L;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    LocalDateTime updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Long updateUser;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    Long createUser;

    /**
     * 删除标识，0为未删除，1为已删除
     */
    @TableLogic
    @ApiModelProperty(hidden = true)
    Boolean delFlag;
}
