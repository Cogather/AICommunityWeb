package com.aicommunity.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 分页查询参数
 *
 * @author AI Community Team
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码（从1开始）
     */
    private Integer page = 1;

    /**
     * 每页数量（默认10，最大100）
     */
    private Integer pageSize = 10;

    public PageQuery() {
    }

    public PageQuery(Integer page, Integer pageSize) {
        this.page = page != null && page > 0 ? page : 1;
        this.pageSize = pageSize != null && pageSize > 0 && pageSize <= 100 ? pageSize : 10;
    }

    /**
     * 获取偏移量
     *
     * @return 偏移量
     */
    public Integer getOffset() {
        return (page - 1) * pageSize;
    }
}
