package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 贡献者VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "贡献者")
public class ContributorVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Integer id;

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "用户头像URL")
    private String avatar;

    @ApiModelProperty(value = "发帖数量")
    private Integer postCount;

    @ApiModelProperty(value = "所属部门")
    private String department;
}
