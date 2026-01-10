package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 荣誉影响力响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "荣誉影响力响应")
public class HonorInfluenceResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总荣誉数")
    private Integer totalHonors;

    @ApiModelProperty(value = "获奖用户总数")
    private Integer totalUsers;

    @ApiModelProperty(value = "总花朵数")
    private Integer totalFlowers;

    @ApiModelProperty(value = "分类统计")
    private List<CategoryStats> categories;

    @ApiModelProperty(value = "Top部门")
    private List<DepartmentStats> topDepartments;

    @Data
    @ApiModel(description = "分类统计")
    public static class CategoryStats implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "分类")
        private String category;

        @ApiModelProperty(value = "数量")
        private Integer count;

        @ApiModelProperty(value = "类别名称")
        private String label;
    }

    @Data
    @ApiModel(description = "部门统计")
    public static class DepartmentStats implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "部门名称")
        private String name;

        @ApiModelProperty(value = "数量")
        private Integer count;
    }
}
