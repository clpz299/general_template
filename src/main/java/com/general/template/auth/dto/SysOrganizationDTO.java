package com.general.template.auth.dto;

import com.general.template.auth.group.CreateAction;
import com.general.template.auth.group.UpdateAction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "AuthOrganizationDTO", description = "机构 DTO")
public class SysOrganizationDTO {
    @ApiModelProperty(value = "机构 ID")
    @Null(groups = {CreateAction.class}, message = "ID 必须为空")
    @NotNull(groups = {UpdateAction.class}, message = "ID 不能为空")
    private Long orgId;

    @ApiModelProperty(value = "父 ID")
    private Long pid;

    @ApiModelProperty(value = "父级机构名称")
    private String pOrgName;

    @ApiModelProperty(value = "机构名称")
    @NotBlank(groups = {CreateAction.class, UpdateAction.class}, message = "机构名称不能为空")
    private String orgName;

    @ApiModelProperty(value = "描述")
    private String note;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "子级机构")
    private List<SysOrganizationDTO> children;
}
