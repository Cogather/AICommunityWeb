package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息视图对象
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "消息信息")
public class MessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "消息类型", example = "post_comment")
    private String type;

    @ApiModelProperty(value = "消息标题", example = "帖子评论通知")
    private String title;

    @ApiModelProperty(value = "消息内容", example = "李四 评论了您的帖子《AI大会2024》")
    private String content;

    @ApiModelProperty(value = "相关资源ID（帖子ID、活动ID等）", example = "1")
    private Long relatedId;

    @ApiModelProperty(value = "相关资源类型（post、activity、award等）", example = "post")
    private String relatedType;

    @ApiModelProperty(value = "评论ID（POST_COMMENT和COMMENT_REPLY类型使用）", example = "101")
    private Integer commentId;

    @ApiModelProperty(value = "回复ID（COMMENT_REPLY类型使用）", example = "1002")
    private Integer replyId;

    @ApiModelProperty(value = "发送者用户ID", example = "2")
    private Long fromUserId;

    @ApiModelProperty(value = "发送者用户名", example = "李四")
    private String fromUserName;

    @ApiModelProperty(value = "用户ID（前端兼容字段）", example = "2")
    private String userId;

    @ApiModelProperty(value = "用户名（前端兼容字段）", example = "李四")
    private String userName;

    @ApiModelProperty(value = "是否已读", example = "false")
    private Boolean read;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）", example = "2026-01-13T10:30:00Z")
    private String createdAt;

    @ApiModelProperty(value = "自定义跳转链接（可选）")
    private String link;
}
