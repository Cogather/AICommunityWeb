package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户多级部门信息VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "用户多级部门信息")
public class UserDepartmentsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "一级部门")
    private DepartmentInfoVO level1;

    @ApiModelProperty(value = "二级部门")
    private DepartmentInfoVO level2;

    @ApiModelProperty(value = "三级部门")
    private DepartmentInfoVO level3;

    @ApiModelProperty(value = "四级部门")
    private DepartmentInfoVO level4;

    @ApiModelProperty(value = "五级部门")
    private DepartmentInfoVO level5;

    @ApiModelProperty(value = "六级部门")
    private DepartmentInfoVO level6;
}
