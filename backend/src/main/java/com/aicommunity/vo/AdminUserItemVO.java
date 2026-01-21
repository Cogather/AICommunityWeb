package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理后台用户项VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台用户项")
public class AdminUserItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "角色：admin（管理员）、tool_owner（工具Owner）")
    private String role;
}
