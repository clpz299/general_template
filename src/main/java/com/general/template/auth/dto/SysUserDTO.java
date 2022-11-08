package com.general.template.auth.dto;


import com.general.template.auth.group.CreateAction;
import com.general.template.auth.group.UpdateAction;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "SysUserDTO", description = "用户 DTO")
public class SysUserDTO {

    @ApiModelProperty(value = "用户 ID")
    @Null(groups = {CreateAction.class}, message = "ID 必须为空")
    @NotNull(groups = {UpdateAction.class}, message = "ID 不能为空")
    private Long userId;

    @ApiModelProperty(value = " ID")
    @NotNull(groups = {CreateAction.class, UpdateAction.class}, message = "机构不能为空")
    private Long orgId;

    @ApiModelProperty(value = "用户名")
    @NotBlank(groups = {CreateAction.class, UpdateAction.class}, message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(\\s*)|((?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20})$", message = "密码格式有误")
    private String password;

    @ApiModelProperty(value = "姓名")
    @NotBlank(groups = {CreateAction.class, UpdateAction.class}, message = "姓名不能为空")
    private String fullName;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$", message = "手机号格式有误")
    @NotBlank(groups = {CreateAction.class, UpdateAction.class}, message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式有误")
    @NotBlank(groups = {CreateAction.class, UpdateAction.class}, message = "邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "是否锁定")
    private Boolean locked;

    @ApiModelProperty(value = "是否可用")
    private Boolean enabled;

    @ApiModelProperty(value = "最近登录时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "角色 ID 集合")
    @NotEmpty(message = "角色不能为空")
    private List<Long> roleIds;

    @ApiModelProperty(value = "角色名称集合")
    private List<String> roleNames;

    @ApiModelProperty(value = "角色Code集合")
    private List<String> roleCodes;

    @ApiModelProperty(value = "机构名称")
    private String orgName;
}
