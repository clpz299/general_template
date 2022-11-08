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
@ApiModel(value = "RoleSelectDTO", description = "角色查询条件 DTO")
public class RoleSelectDTO implements Serializable {

    @ApiModelProperty(value = "角色名称", example = "")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "角色编码", example = "")
    @NotBlank
    private String code;




}
