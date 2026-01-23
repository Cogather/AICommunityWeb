package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 部门列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "部门列表")
public class DepartmentListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门列表")
    private List<DepartmentVO> list;
}
