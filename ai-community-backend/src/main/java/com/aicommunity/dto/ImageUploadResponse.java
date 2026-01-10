package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 图片上传响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "图片上传响应")
public class ImageUploadResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片URL")
    private String url;
}
