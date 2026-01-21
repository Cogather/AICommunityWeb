package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子详情VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "帖子详情")
public class PostDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private String id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "帖子摘要")
    private String summary;

    @ApiModelProperty(value = "帖子描述")
    private String description;

    @ApiModelProperty(value = "帖子内容（HTML格式）")
    private String content;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "封面图URL（与cover二选一）")
    private String image;

    @ApiModelProperty(value = "作者名称")
    private String author;

    @ApiModelProperty(value = "作者名称（与author相同）")
    private String authorName;

    @ApiModelProperty(value = "作者用户ID")
    private String authorId;

    @ApiModelProperty(value = "作者头像URL")
    private String authorAvatar;

    @ApiModelProperty(value = "作者所属部门")
    private String department;

    @ApiModelProperty(value = "所属专区：practices/tools/agent/empowerment")
    private String zone;

    @ApiModelProperty(value = "关联工具ID（仅工具专区和Agent）")
    private Integer toolId;

    @ApiModelProperty(value = "关联工具名称")
    private String toolName;

    @ApiModelProperty(value = "帖子分类：guide/excellent")
    private String category;

    @ApiModelProperty(value = "标签数组")
    private List<String> tags;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @ApiModelProperty(value = "评论数")
    private Integer comments;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "当前用户是否已点赞")
    private Boolean isLiked;

    @ApiModelProperty(value = "当前用户是否已收藏")
    private Boolean isCollected;

    @ApiModelProperty(value = "当前用户是否为作者")
    private Boolean isAuthor;

    @ApiModelProperty(value = "当前用户是否有编辑权限")
    private Boolean canEdit;

    @ApiModelProperty(value = "当前用户是否有删除权限")
    private Boolean canDelete;

    @ApiModelProperty(value = "是否为精华帖子")
    private Boolean featured;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）")
    private String createTime;

    @ApiModelProperty(value = "更新时间（ISO 8601格式）")
    private String updateTime;
}
