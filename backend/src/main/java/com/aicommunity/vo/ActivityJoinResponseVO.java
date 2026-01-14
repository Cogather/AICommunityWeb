package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报名活动响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "报名活动响应")
public class ActivityJoinResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "报名时间（ISO 8601格式）")
    private String joinTime;
}
