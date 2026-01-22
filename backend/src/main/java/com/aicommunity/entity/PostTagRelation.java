package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 帖子标签关联实体类
 * 对应数据库表：t_new_posts_tag_relation
 *
 * @author AI Community Team
 * @date 2026-01-22
 */
@Data
@ApiModel(description = "帖子标签关联")
public class PostTagRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "帖子ID")
    private String postId;

    @ApiModelProperty(value = "标签ID（关联t_new_posts_tag表的id）")
    private Integer tagId;

    @ApiModelProperty(value = "是否删除")
    private Boolean deleted;

    // 以下字段通过JOIN t_new_posts_tag表获取，用于查询结果映射
    @ApiModelProperty(value = "标签名称")
    private String tag;

    @ApiModelProperty(value = "标签颜色")
    private String color;

    @ApiModelProperty(value = "背景色")
    private String bg;

    @ApiModelProperty(value = "边框颜色")
    private String borderColor;

    @ApiModelProperty(value = "用户ID")
    private String userId;
}
