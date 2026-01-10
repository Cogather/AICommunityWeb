package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 荣誉殿堂Banner响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "荣誉殿堂Banner响应")
public class HonorBannerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Banner图片URL")
    private String bannerImage;
}
