package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子草稿实体类
 *
 * @author AI Community Team
 */
@Data
public class PostDraft implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 草稿ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容简介
     */
    private String summary;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 标签（JSON格式）
     */
    private String tags;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 专区
     */
    private String zone;

    /**
     * 工具ID
     */
    private Long toolId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
