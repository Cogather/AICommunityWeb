package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "登录响应")
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Token")
    private String token;

    @ApiModelProperty(value = "用户信息")
    private UserInfo user;

    @Data
    @ApiModel(description = "用户信息")
    public static class UserInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "用户ID")
        private Long id;

        @ApiModelProperty(value = "姓名")
        private String name;

        @ApiModelProperty(value = "头像")
        private String avatar;

        @ApiModelProperty(value = "部门")
        private String department;

        @ApiModelProperty(value = "角色：admin-管理员，user-普通用户")
        private String role;
    }
}
