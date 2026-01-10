package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论实体类
 *
 * @author AI Community Team
 */
@Data
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 是否已点赞
     */
    private Boolean isLiked;

    /**
     * 是否是帖子作者
     */
    private Boolean isAuthor;

    /**
     * 是否是当前用户的评论
     */
    private Boolean isMyComment;

    /**
     * 是否可以编辑
     */
    private Boolean canEdit;

    /**
     * 是否可以删除
     */
    private Boolean canDelete;

    /**
     * 回复列表
     */
    private List<Reply> replies;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
