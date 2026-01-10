package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工具配置响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "工具配置响应")
public class ToolsConfigResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工具列表")
    private List<ToolItem> list;

    @Data
    @ApiModel(description = "工具项")
    public static class ToolItem implements Serializable {
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

        @ApiModelProperty(value = "Banner列表")
        private List<BannerItem> banners;
    }

    @Data
    @ApiModel(description = "Banner项")
    public static class BannerItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "Banner ID")
        private Long id;

        @ApiModelProperty(value = "工具ID")
        private Long toolId;

        @ApiModelProperty(value = "图片URL")
        private String image;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "描述")
        private String desc;

        @ApiModelProperty(value = "排序")
        private Integer order;
    }
}
