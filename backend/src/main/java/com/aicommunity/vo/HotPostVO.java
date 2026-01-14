package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 最热帖子VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "最热帖子")
public class HotPostVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private Integer id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @ApiModelProperty(value = "排名")
    private Integer rank;
}
