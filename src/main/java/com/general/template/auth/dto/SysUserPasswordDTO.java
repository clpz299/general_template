package com.general.template.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "SysUserPasswordDTO", description = "用户密码 DTO")
public class SysUserPasswordDTO {
    @ApiModelProperty(value = "用户 ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "原密码")
    @NotBlank(message = "原密码不能为空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "新密码不能为空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$", message = "密码格式有误")
    private String newPassword;

}
