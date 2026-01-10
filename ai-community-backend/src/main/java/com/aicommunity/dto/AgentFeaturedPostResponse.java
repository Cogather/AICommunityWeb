package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 扶摇Agent置顶帖子响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "扶摇Agent置顶帖子响应")
public class AgentFeaturedPostResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "作者头像")
    private String authorAvatar;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @ApiModelProperty(value = "评论数")
    private Integer comments;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "标签列表")
    private String[] tags;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "跳转链接")
    private String link;
}
