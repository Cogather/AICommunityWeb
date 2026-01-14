package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 悬浮工具平台项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "悬浮工具平台项")
public class ToolPlatformItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工具ID")
    private Integer id;

    @ApiModelProperty(value = "工具名称")
    private String name;

    @ApiModelProperty(value = "工具简介描述")
    private String desc;

    @ApiModelProperty(value = "工具Logo图片URL")
    private String logo;

    @ApiModelProperty(value = "工具主题颜色（十六进制）")
    private String color;

    @ApiModelProperty(value = "工具平台跳转链接")
    private String platformUrl;
}
