package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "部门")
public class DepartmentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门ID")
    private Integer id;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "该部门的发帖数")
    private Integer postCount;

    @ApiModelProperty(value = "该部门的贡献者数量")
    private Integer contributorCount;
}
