package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 轮播图项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "轮播图项")
public class CarouselItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播图ID")
    private Integer id;

    @ApiModelProperty(value = "图片URL")
    private String image;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "点击跳转链接")
    private String link;

    @ApiModelProperty(value = "是否显示标题和描述内容")
    private Boolean showContent;

    @ApiModelProperty(value = "排序序号")
    private Integer order;
}
