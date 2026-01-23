package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新用户信息请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "更新用户信息请求参数")
public class UserUpdateRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "新头像URL", example = "https://example.com/avatars/new-avatar.jpg")
    private String avatar;

    @ApiModelProperty(value = "新个人简介", example = "更新后的个人简介")
    private String bio;
}
