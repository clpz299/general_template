package com.general.template.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(value = "RoleResOperateDTO", description = "角色关联菜单操作 DTO")
public class RoleResOperateDTO implements Serializable {
    private static final long serialVersionUID = 3876509477437916270L;

    @ApiModelProperty(value = "角色ID", example = "")
    @NotNull
    private Long roleId;

    @ApiModelProperty(value = "菜单列表", example = "")
    private List<Long> resourceIds;
}

