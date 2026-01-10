package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门统计响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "部门统计响应")
public class DepartmentStatsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门ID")
    private Long id;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "该部门发布的帖子数量")
    private Integer postCount;

    @ApiModelProperty(value = "该部门的贡献者数量")
    private Integer contributorCount;
}
