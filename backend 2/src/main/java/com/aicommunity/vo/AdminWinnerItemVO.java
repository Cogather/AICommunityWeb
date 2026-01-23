package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理后台获奖者项VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台获奖者项")
public class AdminWinnerItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "获奖记录ID")
    private Integer id;

    @ApiModelProperty(value = "获奖者姓名")
    private String name;

    @ApiModelProperty(value = "获奖时间（YYYY-MM格式）")
    private String awardTime;

    @ApiModelProperty(value = "奖项名称")
    private String awardName;
}
