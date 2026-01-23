package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发布活动请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "发布活动请求")
public class ActivityCreateRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联工具ID", required = true)
    @NotNull(message = "工具ID不能为空")
    private Integer toolId;

    @ApiModelProperty(value = "活动类型：activity/training/workshop", required = true)
    @NotBlank(message = "活动类型不能为空")
    private String type;

    @ApiModelProperty(value = "活动标题", required = true)
    @NotBlank(message = "活动标题不能为空")
    private String title;

    @ApiModelProperty(value = "活动详细内容（HTML格式）", required = true)
    @NotBlank(message = "活动内容不能为空")
    private String content;

    @ApiModelProperty(value = "活动封面图URL")
    private String cover;

    @ApiModelProperty(value = "活动日期（YYYY-MM-DD）", required = true)
    @NotBlank(message = "活动日期不能为空")
    private String date;

    @ApiModelProperty(value = "开始时间（HH:mm）", required = true)
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    @ApiModelProperty(value = "结束时间（HH:mm）", required = true)
    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    @ApiModelProperty(value = "活动地点", required = true)
    @NotBlank(message = "活动地点不能为空")
    private String location;

    @ApiModelProperty(value = "线上会议链接")
    private String meetingLink;

    @ApiModelProperty(value = "主讲人姓名")
    private String speaker;

    @ApiModelProperty(value = "主讲人职称")
    private String speakerTitle;

    @ApiModelProperty(value = "最大参与人数，不填则不限制")
    private Integer maxParticipants;
}
