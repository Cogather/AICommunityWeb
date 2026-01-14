package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 精华帖子VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "精华帖子")
public class FeaturedPostVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID", required = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "帖子标题", required = true, example = "如何高效使用Agent提升开发效率")
    private String title;

    @ApiModelProperty(value = "帖子摘要", required = true, example = "分享使用Agent工具在开发过程中的最佳实践和技巧。")
    private String description;

    @ApiModelProperty(value = "封面图URL", example = "https://example.com/covers/featured1.jpg")
    private String cover;

    @ApiModelProperty(value = "封面图URL（与cover二选一）", example = "https://example.com/covers/featured1.jpg")
    private String image;

    @ApiModelProperty(value = "作者名称", required = true, example = "张工程师")
    private String author;

    @ApiModelProperty(value = "作者ID", example = "101")
    private Integer authorId;

    @ApiModelProperty(value = "作者头像URL", example = "https://example.com/avatars/user101.jpg")
    private String authorAvatar;

    @ApiModelProperty(value = "标签列表", example = "[\"讨论\", \"Agent\"]")
    private List<String> tags;

    @ApiModelProperty(value = "浏览量", required = true, example = "1250")
    private Integer views;

    @ApiModelProperty(value = "评论数", required = true, example = "45")
    private Integer comments;

    @ApiModelProperty(value = "点赞数", required = true, example = "128")
    private Integer likes;

    @ApiModelProperty(value = "是否为精华帖子（始终为true）", required = true, example = "true")
    private Boolean featured;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）", required = true, example = "2026-01-10T10:30:00Z")
    private String createTime;
}
