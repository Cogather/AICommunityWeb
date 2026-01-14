package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息实体类
 * 对应数据库表：t_user_info
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "用户信息")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "工号")
    private String userName;

    @ApiModelProperty(value = "中文名")
    private String chnName;

    @ApiModelProperty(value = "头像")
    private String authorAvatar;

    @ApiModelProperty(value = "个人简介")
    private String bio;

    @ApiModelProperty(value = "一级部门")
    private String departmentL1;

    @ApiModelProperty(value = "一级部门ID")
    private String departmentL1Id;

    @ApiModelProperty(value = "二级部门")
    private String departmentL2;

    @ApiModelProperty(value = "二级部门ID")
    private String departmentL2Id;

    @ApiModelProperty(value = "三级部门")
    private String departmentL3;

    @ApiModelProperty(value = "三级部门ID")
    private String departmentL3Id;

    @ApiModelProperty(value = "四级部门")
    private String departmentL4;

    @ApiModelProperty(value = "四级部门ID")
    private String departmentL4Id;

    @ApiModelProperty(value = "五级部门")
    private String departmentL5;

    @ApiModelProperty(value = "五级部门ID")
    private String departmentL5Id;

    @ApiModelProperty(value = "六级部门")
    private String departmentL6;

    @ApiModelProperty(value = "六级部门ID")
    private String departmentL6Id;

    @ApiModelProperty(value = "角色")
    private String role;

    @ApiModelProperty(value = "用户等级")
    private Integer level;

    @ApiModelProperty(value = "审核状态")
    private String status;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;
}
