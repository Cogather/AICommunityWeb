package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复实体类
 * 对应数据库表：t_new_post_replies
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "回复")
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回复ID")
    private Integer id;

    @ApiModelProperty(value = "评论ID")
    private Integer commentId;

    @ApiModelProperty(value = "回复者用户ID")
    private String userId;

    @ApiModelProperty(value = "被回复者用户ID")
    private String replyToUserId;

    @ApiModelProperty(value = "被回复的评论/回复ID（用于前端定位展示）")
    private Integer replyToId;

    @ApiModelProperty(value = "回复内容")
    private String content;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "状态，0-正常，1-已删除")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
