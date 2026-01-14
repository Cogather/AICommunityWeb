package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 时光轴项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "时光轴项")
public class TimelineItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉记录ID")
    private Integer id;

    @ApiModelProperty(value = "获奖者姓名")
    private String name;

    @ApiModelProperty(value = "获奖者头像URL")
    private String avatar;

    @ApiModelProperty(value = "奖项名称")
    private String awardName;

    @ApiModelProperty(value = "获奖日期（YYYY-MM-DD格式）")
    private String awardDate;

    @ApiModelProperty(value = "奖项类别")
    private String category;
}
