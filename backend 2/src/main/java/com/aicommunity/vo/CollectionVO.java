package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 精选合集VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "精选合集")
public class CollectionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合集ID", required = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "合集标题", required = true, example = "顶级AI研究论文")
    private String title;

    @ApiModelProperty(value = "合集描述", required = true, example = "精选AI领域最具影响力的研究论文合集")
    private String description;

    @ApiModelProperty(value = "合集封面图URL", required = true, example = "https://example.com/collections/papers.jpg")
    private String cover;

    @ApiModelProperty(value = "合集内帖子数量", required = true, example = "25")
    private Integer postCount;

    @ApiModelProperty(value = "合集总浏览量", required = true, example = "8500")
    private Integer viewCount;
}
