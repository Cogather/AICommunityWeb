package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建活动请求DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "创建活动请求")
public class ActivityCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动标题", required = true)
    @NotBlank(message = "活动标题不能为空")
    private String title;

    @ApiModelProperty(value = "工具ID（-1表示扶摇Agent应用）")
    private Long toolId;

    @ApiModelProperty(value = "活动类型：training-培训，competition-竞赛，sharing-分享", required = true)
    @NotBlank(message = "活动类型不能为空")
    private String type;

    @ApiModelProperty(value = "活动日期（格式：YYYY-MM-DD）", required = true)
    @NotBlank(message = "活动日期不能为空")
    private String date;

    @ApiModelProperty(value = "封面图URL", required = true)
    @NotBlank(message = "封面图不能为空")
    private String cover;

    @ApiModelProperty(value = "活动内容（HTML格式）", required = true)
    @NotBlank(message = "活动内容不能为空")
    private String content;
}
