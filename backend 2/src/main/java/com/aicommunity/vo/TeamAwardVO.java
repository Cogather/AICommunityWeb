package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 团队奖项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "团队奖项")
public class TeamAwardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "奖项ID")
    private String id;

    @ApiModelProperty(value = "奖项名称")
    private String title;

    @ApiModelProperty(value = "获奖年份")
    private String year;

    @ApiModelProperty(value = "获奖团队图片列表")
    private List<TeamAwardImageVO> images;
}
