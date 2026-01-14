package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动报名信息VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "活动报名信息")
public class ActivityRegistrationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报名记录ID")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户头像URL")
    private String userAvatar;

    @ApiModelProperty(value = "工号")
    private String employeeId;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "报名时间（ISO 8601格式）")
    private String registerTime;
}
