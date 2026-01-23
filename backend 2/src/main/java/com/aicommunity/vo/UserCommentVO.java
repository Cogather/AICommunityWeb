package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户评论VO（用于个人中心）
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "用户评论")
public class UserCommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论ID")
    private Integer id;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "所属帖子ID")
    private Integer postId;

    @ApiModelProperty(value = "所属帖子标题")
    private String postTitle;

    @ApiModelProperty(value = "评论者ID")
    private Integer userId;

    @ApiModelProperty(value = "评论者姓名")
    private String userName;

    @ApiModelProperty(value = "评论者头像")
    private String userAvatar;

    @ApiModelProperty(value = "评论点赞数")
    private Integer likes;

    @ApiModelProperty(value = "评论时间（ISO 8601格式）")
    private String createTime;
}
