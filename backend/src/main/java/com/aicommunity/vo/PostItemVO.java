package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "帖子项")
public class PostItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private Integer id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "帖子描述/摘要")
    private String description;

    @ApiModelProperty(value = "作者名称")
    private String author;

    @ApiModelProperty(value = "作者ID")
    private Integer authorId;

    @ApiModelProperty(value = "作者头像URL")
    private String authorAvatar;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）")
    private String createTime;

    @ApiModelProperty(value = "显示用的时间格式")
    private String createTimeDisplay;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @ApiModelProperty(value = "评论数")
    private Integer comments;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "标签数组")
    private List<String> tags;

    @ApiModelProperty(value = "封面图片URL")
    private String image;

    @ApiModelProperty(value = "是否为精华帖")
    private Boolean featured;

    @ApiModelProperty(value = "所属部门")
    private String department;

    @ApiModelProperty(value = "收藏时间（ISO 8601格式，仅收藏列表返回）")
    private String favoriteTime;
}
