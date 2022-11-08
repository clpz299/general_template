package com.general.template.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "JwtAuthenticationResponseDTO", description = "认证返回 DTO")
public class AuthenticationResponseDTO {

    @ApiModelProperty(value = "访问令牌")
    private String token;

    @ApiModelProperty(value = "是否需要输入验证码")
    private Boolean needCaptcha;
}
