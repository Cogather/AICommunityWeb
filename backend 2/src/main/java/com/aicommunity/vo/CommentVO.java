package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 评论VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "评论")
public class CommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论ID")
    private Integer id;

    @ApiModelProperty(value = "帖子ID")
    private String postId;

    @ApiModelProperty(value = "评论者用户ID")
    private String userId;

    @ApiModelProperty(value = "评论者名称")
    private String userName;

    @ApiModelProperty(value = "评论者头像URL")
    private String userAvatar;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "当前用户是否已点赞")
    private Boolean isLiked;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "回复列表")
    private List<ReplyVO> replies;
}
