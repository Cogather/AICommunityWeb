package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 回复VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "回复")
public class ReplyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回复ID")
    private Integer id;

    @ApiModelProperty(value = "所属评论ID")
    private Integer commentId;

    @ApiModelProperty(value = "回复者用户ID")
    private Integer userId;

    @ApiModelProperty(value = "回复者名称")
    private String userName;

    @ApiModelProperty(value = "回复者头像URL")
    private String userAvatar;

    @ApiModelProperty(value = "回复内容")
    private String content;

    @ApiModelProperty(value = "被回复者名称")
    private String replyTo;

    @ApiModelProperty(value = "被回复者用户ID")
    private Integer replyToUserId;

    @ApiModelProperty(value = "被回复的评论/回复ID（用于前端定位展示）")
    private Integer replyToId;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
