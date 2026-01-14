package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页Banner配置实体类
 * 对应数据库表：t_new_home_banner_config
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "首页Banner配置")
public class HomeBannerConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID，自增")
    private Long id;

    @ApiModelProperty(value = "Banner图片URL")
    private String bannerImage;

    @ApiModelProperty(value = "Banner标题")
    private String bannerTitle;

    @ApiModelProperty(value = "Banner描述")
    private String bannerDesc;

    @ApiModelProperty(value = "Banner跳转URL")
    private String bannerUrl;

    @ApiModelProperty(value = "排序序号")
    private Integer order;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
