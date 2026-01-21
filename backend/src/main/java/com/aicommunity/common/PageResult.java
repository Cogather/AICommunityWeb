package com.aicommunity.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果
 *
 * @param <T> 数据类型
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "分页查询结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据列表")
    private List<T> list;

    @ApiModelProperty(value = "总记录数", example = "100")
    private Long total;

    @ApiModelProperty(value = "当前页码", example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total, Integer page, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }
}
