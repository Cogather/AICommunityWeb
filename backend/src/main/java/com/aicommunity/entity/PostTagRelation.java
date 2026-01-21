package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 帖子标签实体类
 * 对应数据库表：t_new_posts_tag
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "帖子标签")
public class PostTagRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private String postId;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "标签ID（关联t_new_post_label表）")
    private Integer labelId;

    @ApiModelProperty(value = "标签名称")
    private String tag;

    @ApiModelProperty(value = "标签颜色")
    private String color;

    @ApiModelProperty(value = "背景色")
    private String bg;

    @ApiModelProperty(value = "边框颜色")
    private String borderColor;

    @ApiModelProperty(value = "是否删除")
    private Boolean deleted;

    @ApiModelProperty(value = "用户ID")
    private String userId;
}
