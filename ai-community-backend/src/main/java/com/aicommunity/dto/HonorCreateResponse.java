package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建荣誉响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "创建荣誉响应")
public class HonorCreateResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉记录ID")
    private Long id;

    @ApiModelProperty(value = "消息")
    private String message;
}
