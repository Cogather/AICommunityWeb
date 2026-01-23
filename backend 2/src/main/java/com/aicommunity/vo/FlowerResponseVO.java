package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 送花响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "送花响应")
public class FlowerResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "送花后的总花朵数")
    private Integer flowers;

    @ApiModelProperty(value = "是否已送花（始终为true）")
    private Boolean hasGivenFlower;
}
