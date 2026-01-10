package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新用户资料请求DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "更新用户资料请求")
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "个人简介")
    private String bio;

    @ApiModelProperty(value = "头像URL")
    private String avatar;
}
