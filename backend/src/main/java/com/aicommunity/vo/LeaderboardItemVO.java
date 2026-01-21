package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 排行榜项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "排行榜项")
public class LeaderboardItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "所属部门")
    private String department;

    @ApiModelProperty(value = "用户头像URL")
    private String avatar;

    @ApiModelProperty(value = "获奖数量（勋章数）")
    private Integer count;

    @ApiModelProperty(value = "累计收到的花朵总数")
    private Integer totalFlowers;
}
