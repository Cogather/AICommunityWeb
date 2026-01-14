package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 团队奖项图片VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "团队奖项图片")
public class TeamAwardImageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片ID")
    private Integer id;

    @ApiModelProperty(value = "图片URL")
    private String image;

    @ApiModelProperty(value = "图片类型：url/upload")
    private String imageType;

    @ApiModelProperty(value = "获奖团队名称")
    private String winnerName;

    @ApiModelProperty(value = "团队领域")
    private String teamField;

    @ApiModelProperty(value = "获奖事迹（HTML富文本格式）")
    private String story;

    @ApiModelProperty(value = "收到的花朵数")
    private Integer flowers;

    @ApiModelProperty(value = "当前用户是否已送花")
    private Boolean hasGivenFlower;
}
