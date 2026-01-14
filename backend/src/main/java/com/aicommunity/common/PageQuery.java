package com.aicommunity.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数基类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "分页查询参数")
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;
}
