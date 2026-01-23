package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "活动项")
public class ActivityItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动唯一标识")
    private Integer id;

    @ApiModelProperty(value = "关联工具ID")
    private Integer toolId;

    @ApiModelProperty(value = "关联工具名称")
    private String toolName;

    @ApiModelProperty(value = "活动类型：activity/training/workshop")
    private String type;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动详细内容（HTML格式）")
    private String content;

    @ApiModelProperty(value = "活动封面图URL")
    private String cover;

    @ApiModelProperty(value = "活动日期（YYYY-MM-DD）")
    private String date;

    @ApiModelProperty(value = "开始时间（HH:mm）")
    private String startTime;

    @ApiModelProperty(value = "结束时间（HH:mm）")
    private String endTime;

    @ApiModelProperty(value = "活动地点")
    private String location;

    @ApiModelProperty(value = "线上会议链接（可选）")
    private String meetingLink;

    @ApiModelProperty(value = "主讲人姓名")
    private String speaker;

    @ApiModelProperty(value = "主讲人职称")
    private String speakerTitle;

    @ApiModelProperty(value = "最大参与人数")
    private Integer maxParticipants;

    @ApiModelProperty(value = "当前报名人数")
    private Integer currentParticipants;

    @ApiModelProperty(value = "活动状态：upcoming/ongoing/ended")
    private String status;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）")
    private String createTime;

    @ApiModelProperty(value = "已报名人数（用于个人中心接口）")
    private Integer registeredCount;

    @ApiModelProperty(value = "最大报名人数（用于个人中心接口）")
    private Integer maxRegistrations;

    @ApiModelProperty(value = "创建者ID（用于个人中心接口）")
    private Integer creatorId;

    @ApiModelProperty(value = "创建者姓名（用于个人中心接口）")
    private String creatorName;
}
