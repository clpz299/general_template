package com.general.template.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "SysUserRoleDTO", description = "用户角色关联 DTO")
public class SysUserRoleDTO {

    @ApiModelProperty(value = "用户 ID")
    @NotNull
    private Long userId;

    @ApiModelProperty(value = "角色 ID 集合")
    @NotEmpty
    private List<Long> roleIds;

}
