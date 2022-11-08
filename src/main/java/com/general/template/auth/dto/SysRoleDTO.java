package com.general.template.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(value = "SysRoleDTO", description = "角色信息 DTO")
public class SysRoleDTO implements Serializable {
    @ApiModelProperty(value = "角色ID", example = "")
    private Long roleId;

    @ApiModelProperty(value = "角色名称", example = "")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "角色编码", example = "")
    @NotBlank
    private String code;

    @ApiModelProperty(value = "描述", example = "")
    private String note;

    @ApiModelProperty(value = "角色关联的菜单ID集合", example = "")
    private List<Long> resourceList;

    @ApiModelProperty(value = "创建时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "更新时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    @ApiModelProperty(value = "更新人", example = "")
    private String updateUser;

    @ApiModelProperty(value = "创建人", example = "")
    private String createUser;

}
