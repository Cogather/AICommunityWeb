package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 荣誉Banner实体类
 * 对应数据库表：t_honor_banners
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "荣誉Banner")
public class HonorBanner implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Banner ID")
    private Long id;

    @ApiModelProperty(value = "图片URL")
    private String image;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "跳转链接")
    private String link;

    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "排序")
    private Integer order;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
