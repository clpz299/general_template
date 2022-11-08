package com.general.template.auth.dto;

import com.general.template.auth.group.CreateAction;
import com.general.template.auth.group.UpdateAction;
import com.general.template.enums.MenuType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "SysMenuDTO", description = "菜单目录 DTO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuDTO {

    @ApiModelProperty(value = "菜单目录 ID")
    @Null(groups = {CreateAction.class}, message = "ID 必须为空")
    @NotNull(groups = {UpdateAction.class}, message = "ID 不能为空")
    private Long menuId;

    @ApiModelProperty(value = "菜单目录名称")
    @NotEmpty(groups = {CreateAction.class, UpdateAction.class}, message = "菜单目录名称不能为空")
    private String resName;

    @ApiModelProperty(value = "菜单目录编码")
    @NotEmpty(groups = {CreateAction.class, UpdateAction.class}, message = "菜单目录编码不能为空")
    private String resCode;

    @ApiModelProperty(value = "菜单目录类型")
    @NotNull(groups = {CreateAction.class, UpdateAction.class}, message = "菜单目录类型不能为空")
    private MenuType resType;

    @ApiModelProperty(value = "父 ID")
    private Long pid;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "子级菜单目录")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<SysMenuDTO> children;

    @ApiModelProperty(value = "关联接口")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<SysApiDTO> apis;
}
