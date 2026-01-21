package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 专区标签VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "专区标签")
public class ZoneTagVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签名称")
    private String name;

    @ApiModelProperty(value = "该标签的帖子数量")
    private Integer count;
}
