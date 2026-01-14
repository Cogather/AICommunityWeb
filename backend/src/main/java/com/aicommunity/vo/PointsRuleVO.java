package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分规则VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "积分规则")
public class PointsRuleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "行为类型", example = "post_publish")
    private String action;

    @ApiModelProperty(value = "积分值", example = "50")
    private Integer points;

    @ApiModelProperty(value = "描述", example = "发布帖子")
    private String description;
}
