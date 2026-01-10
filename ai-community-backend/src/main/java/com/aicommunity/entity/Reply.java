package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 回复实体类
 *
 * @author AI Community Team
 */
@Data
public class Reply implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 回复ID
     */
    private Long id;

    /**
     * 评论ID
     */
    private Long commentId;

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
     * 回复的用户ID
     */
    private Long replyToUserId;

    /**
     * 回复的用户名
     */
    private String replyTo;

    /**
     * 回复内容
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
     * 创建时间
     */
    private Date createTime;
}
