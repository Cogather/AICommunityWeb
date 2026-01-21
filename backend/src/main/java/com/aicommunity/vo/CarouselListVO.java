package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 轮播图列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "轮播图列表")
public class CarouselListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播图列表")
    private List<CarouselItemVO> list;
}
