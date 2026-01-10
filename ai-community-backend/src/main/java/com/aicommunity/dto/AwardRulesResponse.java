package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 奖项规则响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "奖项规则响应")
public class AwardRulesResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "奖项ID")
    private Long id;

    @ApiModelProperty(value = "奖项名称")
    private String name;

    @ApiModelProperty(value = "奖项规则详细说明")
    private String rules;

    @ApiModelProperty(value = "奖项分类")
    private String category;

    @ApiModelProperty(value = "奖项图片")
    private String image;

    @ApiModelProperty(value = "奖项描述")
    private String desc;
}
