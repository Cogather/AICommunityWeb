package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 标签实体类
 *
 * @author AI Community Team
 */
@Data
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 专区
     */
    private String zone;

    /**
     * 工具ID（可选）
     */
    private Long toolId;

    /**
     * 是否预设标签
     */
    private Boolean preset;

    /**
     * 创建时间
     */
    private Date createTime;
}
