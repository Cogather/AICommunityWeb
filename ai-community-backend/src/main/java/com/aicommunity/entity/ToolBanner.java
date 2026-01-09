package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 工具Banner实体类
 *
 * @author AI Community Team
 */
@Data
public class ToolBanner implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Banner ID
     */
    private Long id;

    /**
     * 工具ID
     */
    private Long toolId;

    /**
     * Banner图片URL
     */
    private String image;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String desc;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
