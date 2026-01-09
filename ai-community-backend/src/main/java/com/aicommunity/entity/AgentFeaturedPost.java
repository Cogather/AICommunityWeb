package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 扶摇Agent应用置顶帖子实体类
 *
 * @author AI Community Team
 */
@Data
public class AgentFeaturedPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的帖子ID（可选）
     */
    private Long postId;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
