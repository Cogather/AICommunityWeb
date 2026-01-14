package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 最新获奖者VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "最新获奖者信息")
public class LatestWinnerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉记录ID", required = true)
    private Integer id;

    @ApiModelProperty(value = "获奖者姓名", required = true)
    private String name;

    @ApiModelProperty(value = "获奖者头像URL")
    private String avatar;

    @ApiModelProperty(value = "奖项名称", required = true)
    private String awardName;
}
