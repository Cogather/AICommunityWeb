package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 扶摇Agent应用置顶帖子VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "扶摇Agent应用置顶帖子")
public class AgentFeaturedPostVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID", required = true, example = "1")
    private String id;

    @ApiModelProperty(value = "帖子标题", required = true, example = "扶摇Agent智能编排最佳实践")
    private String title;

    @ApiModelProperty(value = "帖子摘要", required = true, example = "本文详细介绍如何利用扶摇Agent进行智能工作流编排...")
    private String description;

    @ApiModelProperty(value = "帖子封面图（优先使用）", example = "https://example.com/covers/featured1.jpg")
    private String image;

    @ApiModelProperty(value = "帖子封面图（备用字段，与image二选一）", example = "https://example.com/covers/featured1.jpg")
    private String cover;

    @ApiModelProperty(value = "作者名称", required = true, example = "张三")
    private String author;

    @ApiModelProperty(value = "作者ID", example = "101")
    private String authorId;

    @ApiModelProperty(value = "作者头像", example = "https://example.com/avatars/user101.jpg")
    private String authorAvatar;

    @ApiModelProperty(value = "标签列表", example = "[\"Agent应用\", \"智能编排\", \"最佳实践\"]")
    private List<String> tags;

    @ApiModelProperty(value = "浏览量", required = true, example = "2580")
    private Integer views;

    @ApiModelProperty(value = "评论数", required = true, example = "89")
    private Integer comments;

    @ApiModelProperty(value = "点赞数", example = "156")
    private Integer likes;

    @ApiModelProperty(value = "是否为精华帖子（始终为true）", required = true, example = "true")
    private Boolean featured;

    @ApiModelProperty(value = "创建时间（ISO 8601格式或日期字符串）", required = true, example = "2026-01-10T10:30:00Z")
    private String createTime;
}
