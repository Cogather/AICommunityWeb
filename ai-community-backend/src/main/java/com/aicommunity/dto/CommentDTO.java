package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 评论DTO
 *
 * @author AI Community Team
 */
@Data
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Long id;

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
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 当前用户是否已点赞
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
    private List<ReplyDTO> replies;

    /**
     * 回复DTO
     */
    @Data
    public static class ReplyDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 回复ID
         */
        private Long id;

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
         * 回复内容
         */
        private String content;

        /**
         * 创建时间
         */
        private String createTime;

        /**
         * 回复的用户名
         */
        private String replyTo;

        /**
         * 回复的用户ID
         */
        private Long replyToId;

        /**
         * 点赞数
         */
        private Integer likes;

        /**
         * 当前用户是否已点赞
         */
        private Boolean isLiked;
    }
}
