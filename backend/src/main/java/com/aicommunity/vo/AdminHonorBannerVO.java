package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理后台荣誉殿堂Banner配置VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台荣誉殿堂Banner配置")
public class AdminHonorBannerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Banner图片URL")
    private String bannerImage;
}
