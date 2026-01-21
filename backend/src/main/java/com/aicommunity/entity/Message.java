package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息实体类
 * 对应数据库表：t_user_messages
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "用户消息")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息ID")
    private Long id;

    @ApiModelProperty(value = "接收消息的用户ID")
    private String userId;

    @ApiModelProperty(value = "消息类型：activity_registration-活动报名，post_comment-帖子评论，comment_reply-评论回复，post_like-点赞通知，award_notification-奖项通知")
    private String type;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "相关资源ID（帖子ID、活动ID、奖项ID等）")
    private String relatedId;

    @ApiModelProperty(value = "相关资源类型：post-帖子，activity-活动，award-奖项")
    private String relatedType;

    @ApiModelProperty(value = "评论ID（POST_COMMENT和COMMENT_REPLY类型使用）")
    private Integer commentId;

    @ApiModelProperty(value = "回复ID（COMMENT_REPLY类型使用）")
    private Integer replyId;

    @ApiModelProperty(value = "发送消息的用户ID")
    private String fromUserId;

    @ApiModelProperty(value = "发送消息的用户名")
    private String fromUserName;

    @ApiModelProperty(value = "是否已读：0-未读，1-已读")
    private Integer isRead;

    @ApiModelProperty(value = "自定义跳转链接")
    private String link;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
