package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工具项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "工具项")
public class ToolItemVO implements Serializable {

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

    @ApiModelProperty(value = "点击跳转链接")
    private String link;
}
