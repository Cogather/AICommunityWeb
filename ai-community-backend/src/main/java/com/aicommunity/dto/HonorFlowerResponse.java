package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 荣誉送花响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "荣誉送花响应")
public class HonorFlowerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "花朵数")
    private Integer flowers;

    @ApiModelProperty(value = "是否已送花")
    private Boolean hasGivenFlower;
}
