package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 * 用于查询用户信息（需要根据实际用户表结构调整）
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户头像URL")
    private String avatar;

    @ApiModelProperty(value = "所属部门")
    private String department;

    @ApiModelProperty(value = "用户等级")
    private Integer level;

    @ApiModelProperty(value = "获得荣誉数量")
    private Integer honorCount;
}
