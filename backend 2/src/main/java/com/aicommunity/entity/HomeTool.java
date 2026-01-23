package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页工具实体类
 * 对应数据库表：t_new_home_tool
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "首页工具")
public class HomeTool implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工具ID")
    private Long id;

    @ApiModelProperty(value = "工具名称")
    private String name;

    @ApiModelProperty(value = "工具描述")
    private String desc;

    @ApiModelProperty(value = "Logo URL")
    private String logo;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "跳转路由路径")
    private String link;

    @ApiModelProperty(value = "排序")
    private Integer order;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
