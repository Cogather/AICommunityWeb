package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工具活动实体类
 * 对应数据库表：t_new_tool_activity
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "工具活动")
public class ToolActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动ID")
    private Integer id;

    @ApiModelProperty(value = "工具ID")
    private Integer toolId;

    @ApiModelProperty(value = "工具名称")
    private String toolName;

    @ApiModelProperty(value = "活动类型：activity/training/workshop")
    private String type;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动内容")
    private String content;

    @ApiModelProperty(value = "封面图片URL")
    private String cover;

    @ApiModelProperty(value = "活动日期")
    private Date date;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "活动地点")
    private String location;

    @ApiModelProperty(value = "会议链接")
    private String meetingLink;

    @ApiModelProperty(value = "主讲人")
    private String speaker;

    @ApiModelProperty(value = "主讲人头衔")
    private String speakerTitle;

    @ApiModelProperty(value = "最大参与人数")
    private Integer maxParticipants;

    @ApiModelProperty(value = "当前参与人数")
    private Integer currentParticipants;

    @ApiModelProperty(value = "状态：upcoming/ongoing/ended")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
