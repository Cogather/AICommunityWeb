package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 荣誉列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "荣誉列表")
public class HonorListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉列表")
    private List<HonorItemVO> list;

    @ApiModelProperty(value = "总记录数")
    private Long total;

    @ApiModelProperty(value = "当前页码")
    private Integer page;

    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;

    @ApiModelProperty(value = "总页数")
    private Integer totalPages;
}
