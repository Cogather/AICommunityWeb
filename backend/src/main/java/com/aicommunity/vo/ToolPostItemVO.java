package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工具帖子项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "工具帖子项")
public class ToolPostItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子唯一标识")
    private Integer id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "帖子摘要/描述")
    private String description;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "作者名称")
    private String author;

    @ApiModelProperty(value = "作者用户ID")
    private Integer authorId;

    @ApiModelProperty(value = "作者头像URL")
    private String authorAvatar;

    @ApiModelProperty(value = "作者所属部门")
    private String department;

    @ApiModelProperty(value = "关联工具ID，0表示其他工具")
    private Integer toolId;

    @ApiModelProperty(value = "关联工具名称")
    private String toolName;

    @ApiModelProperty(value = "帖子分类：guide/excellent")
    private String category;

    @ApiModelProperty(value = "帖子标签数组")
    private List<String> tags;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "评论数")
    private Integer comments;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）")
    private String createTime;

    @ApiModelProperty(value = "更新时间（ISO 8601格式）")
    private String updateTime;
}
