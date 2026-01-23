package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 荣誉项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "荣誉项")
public class HonorItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉记录ID")
    private Integer id;

    @ApiModelProperty(value = "获奖者姓名")
    private String name;

    @ApiModelProperty(value = "获奖者部门")
    private String department;

    @ApiModelProperty(value = "获奖者头像URL")
    private String avatar;

    @ApiModelProperty(value = "奖项名称")
    private String awardName;

    @ApiModelProperty(value = "获奖日期（YYYY-MM-DD格式）")
    private String awardDate;

    @ApiModelProperty(value = "奖项类别：innovation（创新）/efficiency（效能）/practice（实践）/community（社区）")
    private String category;

    @ApiModelProperty(value = "获奖年份")
    private String year;

    @ApiModelProperty(value = "是否为当前登录用户的荣誉")
    private Boolean isMine;

    @ApiModelProperty(value = "收到的花朵数")
    private Integer flowers;

    @ApiModelProperty(value = "当前用户是否已送花")
    private Boolean hasGivenFlower;

    @ApiModelProperty(value = "获奖成就描述")
    private String achievement;
}
