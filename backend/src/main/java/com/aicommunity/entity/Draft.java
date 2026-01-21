package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 草稿实体类
 * 对应数据库表：t_new_post_drafts
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "草稿")
public class Draft implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "草稿ID")
    private String draftId;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "所属专区")
    private String zone;

    @ApiModelProperty(value = "工具ID")
    private Integer toolId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "标签（JSON格式存储）")
    private String tags;

    @ApiModelProperty(value = "保存时间")
    private Date savedAt;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
