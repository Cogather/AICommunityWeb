package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 赋能交流帖子项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "赋能交流帖子项")
public class EmpowermentPostItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID", required = true, example = "2")
    private Integer id;

    @ApiModelProperty(value = "帖子标题", required = true, example = "Prompt工程的最佳实践分享")
    private String title;

    @ApiModelProperty(value = "帖子摘要", required = true, example = "如何编写高质量的Prompt，提升AI模型输出效果。")
    private String description;

    @ApiModelProperty(value = "封面图URL", example = "https://example.com/covers/post2.jpg")
    private String cover;

    @ApiModelProperty(value = "封面图URL（与cover二选一）", example = "https://example.com/covers/post2.jpg")
    private String image;

    @ApiModelProperty(value = "作者名称", required = true, example = "李开发者")
    private String author;

    @ApiModelProperty(value = "作者ID", example = "102")
    private Integer authorId;

    @ApiModelProperty(value = "作者头像URL", example = "https://example.com/avatars/user102.jpg")
    private String authorAvatar;

    @ApiModelProperty(value = "标签列表", example = "[\"分享\", \"Prompt\"]")
    private List<String> tags;

    @ApiModelProperty(value = "浏览量", required = true, example = "890")
    private Integer views;

    @ApiModelProperty(value = "评论数", required = true, example = "32")
    private Integer comments;

    @ApiModelProperty(value = "点赞数", required = true, example = "75")
    private Integer likes;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）", required = true, example = "2026-01-12T14:30:00Z")
    private String createTime;

    @ApiModelProperty(value = "更新时间（ISO 8601格式）", example = "2026-01-12T14:30:00Z")
    private String updateTime;
}
