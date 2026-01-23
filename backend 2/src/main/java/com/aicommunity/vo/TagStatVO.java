package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 标签统计VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "标签统计")
public class TagStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签名称", required = true, example = "全部")
    private String name;

    @ApiModelProperty(value = "该标签下的帖子数量（包含精华帖子和普通帖子）", required = true, example = "56")
    private Integer count;
}
