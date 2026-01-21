package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设置帖子精华/置顶响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "设置帖子精华/置顶响应")
public class PostFeaturedSetResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否成功", required = true, example = "true")
    private Boolean success;

    @ApiModelProperty(value = "是否为精华/置顶", required = true, example = "true")
    private Boolean featured;
}
