package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 帖子详情DTO
 *
 * @author AI Community Team
 */
@Data
public class PostDetailDTO implements Serializable {

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
     * 作者姓名
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 评论总数
     */
    private Integer comments;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 当前用户是否已点赞
     */
    private Boolean isLiked;

    /**
     * 当前用户是否已收藏
     */
    private Boolean isCollected;

    /**
     * 当前用户是否是作者
     */
    private Boolean isAuthor;

    /**
     * 当前用户是否可以编辑（作者或管理员）
     */
    private Boolean canEdit;

    /**
     * 当前用户是否可以删除（作者或管理员）
     */
    private Boolean canDelete;

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
