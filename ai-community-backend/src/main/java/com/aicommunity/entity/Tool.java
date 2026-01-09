package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 工具实体类
 *
 * @author AI Community Team
 */
@Data
public class Tool implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工具ID
     */
    private Long id;

    /**
     * 工具名称
     */
    private String name;

    /**
     * 工具描述
     */
    private String desc;

    /**
     * 工具Logo URL
     */
    private String logo;

    /**
     * 工具颜色
     */
    private String color;

    /**
     * 跳转路由路径
     */
    private String link;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
