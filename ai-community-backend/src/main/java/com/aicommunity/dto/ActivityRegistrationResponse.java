package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动报名响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "活动报名响应")
public class ActivityRegistrationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "更新后的报名状态")
    private Boolean isRegistered;

    @ApiModelProperty(value = "更新后的报名人数")
    private Integer registeredCount;

    @Data
    @ApiModel(description = "报名用户")
    public static class RegistrationUser implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "报名记录ID")
        private Long id;

        @ApiModelProperty(value = "用户ID")
        private Long userId;

        @ApiModelProperty(value = "用户姓名")
        private String userName;

        @ApiModelProperty(value = "用户头像")
        private String userAvatar;

        @ApiModelProperty(value = "工号")
        private String employeeId;

        @ApiModelProperty(value = "部门")
        private String department;

        @ApiModelProperty(value = "报名时间")
        private String registerTime;
    }
}
