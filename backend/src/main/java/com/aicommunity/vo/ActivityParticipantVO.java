package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动参与者VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "活动参与者")
public class ActivityParticipantVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "工号")
    private String employeeId;

    @ApiModelProperty(value = "所属部门")
    private String department;

    @ApiModelProperty(value = "用户头像URL")
    private String avatar;

    @ApiModelProperty(value = "报名时间（ISO 8601格式）")
    private String joinTime;
}
