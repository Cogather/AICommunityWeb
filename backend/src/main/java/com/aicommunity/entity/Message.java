package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息实体类
 *
 * @author AI Community Team
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 接收用户ID
     */
    private Long userId;

    /**
     * 消息类型：activity_registration-活动报名，post_comment-帖子评论，comment_reply-评论回复，post_like-帖子点赞，award_notification-获奖通知
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 发送用户ID
     */
    private Long fromUserId;

    /**
     * 是否已读：0-未读，1-已读
     */
    private Boolean read;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
