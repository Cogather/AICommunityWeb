package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户收藏的帖子VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "用户收藏的帖子")
public class UserFavoritePostVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private Integer id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "帖子描述")
    private String description;

    @ApiModelProperty(value = "作者名称")
    private String author;

    @ApiModelProperty(value = "作者ID")
    private Integer authorId;

    @ApiModelProperty(value = "作者头像URL")
    private String authorAvatar;

    @ApiModelProperty(value = "封面图片URL")
    private String cover;

    @ApiModelProperty(value = "图片URL")
    private String image;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "评论数")
    private Integer comments;

    @ApiModelProperty(value = "标签列表")
    private List<String> tags;

    @ApiModelProperty(value = "所属专区", example = "practices")
    private String zone;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）")
    private String createTime;

    @ApiModelProperty(value = "收藏时间（ISO 8601格式）")
    private String favoriteTime;
}
