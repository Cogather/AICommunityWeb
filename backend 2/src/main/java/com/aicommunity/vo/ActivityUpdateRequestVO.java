package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 编辑活动请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "编辑活动请求")
public class ActivityUpdateRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联工具ID（不可更改关联工具）")
    private Integer toolId;

    @ApiModelProperty(value = "活动类型：activity/training/workshop")
    private String type;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动详情（HTML格式）")
    private String content;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "活动日期（YYYY-MM-DD）")
    private String date;

    @ApiModelProperty(value = "开始时间（HH:mm）")
    private String startTime;

    @ApiModelProperty(value = "结束时间（HH:mm）")
    private String endTime;

    @ApiModelProperty(value = "活动地点")
    private String location;

    @ApiModelProperty(value = "线上会议链接")
    private String meetingLink;

    @ApiModelProperty(value = "主讲人姓名")
    private String speaker;

    @ApiModelProperty(value = "主讲人职称")
    private String speakerTitle;

    @ApiModelProperty(value = "最大参与人数")
    private Integer maxParticipants;
}
