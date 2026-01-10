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
     * 页码，从1开始
     */
    private Integer page = 1;

    /**
     * 每页数量，默认10，最大100
     */
    private Integer pageSize = 10;

    public void setPageSize(Integer pageSize) {
        if (pageSize != null && pageSize > 0 && pageSize <= 100) {
            this.pageSize = pageSize;
        } else if (pageSize != null && pageSize > 100) {
            this.pageSize = 100;
        }
    }
}
