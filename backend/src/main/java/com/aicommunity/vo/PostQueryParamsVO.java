package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 帖子查询参数VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "帖子查询参数")
public class PostQueryParamsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工具ID")
    private Integer toolId;

    @ApiModelProperty(value = "专区")
    private String zone;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "关键词搜索")
    private String keyword;

    @ApiModelProperty(value = "排序方式：newest-最新，hot-最热，likes-点赞最多，comments-评论最多")
    private String sortBy;

    @ApiModelProperty(value = "页码，从1开始")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数量")
    private Integer pageSize = 15;
}