package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门信息VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "部门信息")
public class DepartmentInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门ID", example = "100")
    private String id;

    @ApiModelProperty(value = "部门名称", example = "技术部")
    private String name;

    @ApiModelProperty(value = "部门层级", example = "1")
    private Integer level;
}
