package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工具Banner项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "工具Banner项")
public class ToolBannerItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Banner ID")
    private Integer id;

    @ApiModelProperty(value = "Banner图片URL")
    private String image;

    @ApiModelProperty(value = "Banner标题")
    private String title;

    @ApiModelProperty(value = "Banner描述")
    private String desc;

    @ApiModelProperty(value = "排序序号")
    private Integer order;
}
