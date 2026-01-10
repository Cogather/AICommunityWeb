package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 标签信息DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "标签信息")
public class TagInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签名称")
    private String name;

    @ApiModelProperty(value = "该标签下的帖子数量")
    private Integer count;

    @ApiModelProperty(value = "是否为预设标签")
    private Boolean preset;
}
