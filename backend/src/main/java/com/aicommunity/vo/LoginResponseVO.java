package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "登录响应")
public class LoginResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "JWT访问令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @ApiModelProperty(value = "令牌有效期（秒）", example = "86400")
    private Long expiresIn;

    @ApiModelProperty(value = "用户基本信息")
    private UserProfileVO user;
}
