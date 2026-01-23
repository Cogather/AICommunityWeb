package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子实体类
 * 对应数据库表：t_new_posts
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "帖子")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private String postId;

    @ApiModelProperty(value = "作者（关联用户表user_id）")
    private String authorId;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "帖子描述")
    private String description;

    @ApiModelProperty(value = "封面")
    private String frontCover;

    @ApiModelProperty(value = "帖子内容")
    private String content;

    @ApiModelProperty(value = "markdown或richtext，默认richtext")
    private String contentType;

    @ApiModelProperty(value = "发布时间")
    private Date createdAt;

    @ApiModelProperty(value = "最后更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "帖子链接")
    private String url;

    @ApiModelProperty(value = "状态，0-正常，1-已删除")
    private String status;

    @ApiModelProperty(value = "0（普通帖子）或1（精华帖）")
    private String essencePost;

    @ApiModelProperty(value = "外键，指向t_new_zone_type表（用于关联帖子的分类。1：AI优秀实践 2：AI使用达人（团队/个人））3：AI工具专区 4：赋能交流 5：扶摇Agent应用")
    private Integer zoneId;

    @ApiModelProperty(value = "外键，指向t_new_post_label表 场景标签id")
    private Integer labelId;

    @ApiModelProperty(value = "外键，指向t_new_tool表 工具id")
    private Integer toolId;

    @ApiModelProperty(value = "帖子浏览量")
    private Integer viewsNums;
}
