package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子实体类
 *
 * @author AI Community Team
 */
@Data
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容简介
     */
    private String summary;

    /**
     * 帖子内容（HTML格式）
     */
    private String content;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 专区：practices-AI优秀实践，tools-AI工具专区，agent-扶摇Agent应用，empowerment-赋能交流
     */
    private String zone;

    /**
     * 工具ID（当zone为tools时必填）
     */
    private Long toolId;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 是否精选：0-否，1-是
     */
    private Boolean isFeatured;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
