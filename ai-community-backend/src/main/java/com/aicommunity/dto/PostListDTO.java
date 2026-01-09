package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 帖子列表DTO
 *
 * @author AI Community Team
 */
@Data
public class PostListDTO implements Serializable {

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
     * 标签列表
     */
    private List<String> tags;

    /**
     * 封面图URL
     */
    private String image;

    /**
     * 封面图URL（兼容字段）
     */
    private String cover;

    /**
     * 是否精选
     */
    private Boolean isFeatured;

    /**
     * 专区
     */
    private String zone;

    /**
     * 工具ID
     */
    private Long toolId;

    /**
     * 工具名称
     */
    private String toolName;
}
