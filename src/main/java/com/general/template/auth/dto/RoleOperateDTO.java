package com.general.template.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(value = "RoleOperateDTO", description = "角色操作 DTO")
public class RoleOperateDTO implements Serializable {

    @ApiModelProperty(value = "角色ID", example = "")
    private Long roleId;

    @ApiModelProperty(value = "角色名称", example = "")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "角色编码", example = "")
    @NotBlank
    private String code;

    @ApiModelProperty(value = "描述", example = "")
    private String node;


}
