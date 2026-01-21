package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 荣誉奖项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "荣誉奖项")
public class HonorAwardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "奖项ID")
    private String id;

    @ApiModelProperty(value = "奖项名称")
    private String name;

    @ApiModelProperty(value = "奖项描述")
    private String desc;

    @ApiModelProperty(value = "奖项图片URL")
    private String image;
}
