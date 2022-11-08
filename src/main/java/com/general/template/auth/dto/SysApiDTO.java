package com.general.template.auth.dto;

import com.general.template.auth.group.UpdateAction;
import com.general.template.enums.ApiScope;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "SysApiDTO", description = "接口 DTO")
public class SysApiDTO {
    @ApiModelProperty(value = "接口 ID")
    @NotNull(groups = {UpdateAction.class}, message = "ID 不能为空")
    private Long apiId;

    @ApiModelProperty(value = "接口名称")
    @NotBlank(groups = {UpdateAction.class}, message = "接口名称不能为空")
    private String apiName;

    @ApiModelProperty(value = "描述")
    private String note;

    @ApiModelProperty(value = "范围")
    private ApiScope scope;

    @ApiModelProperty(value = "方法")
    private String method;

    @ApiModelProperty(value = "URL")
    private String url;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
