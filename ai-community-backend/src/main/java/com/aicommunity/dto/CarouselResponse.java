package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 轮播图响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "轮播图响应")
public class CarouselResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播图列表")
    private List<CarouselItem> list;

    @Data
    @ApiModel(description = "轮播图项")
    public static class CarouselItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "轮播图ID")
        private Long id;

        @ApiModelProperty(value = "图片URL")
        private String image;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "描述")
        private String desc;

        @ApiModelProperty(value = "链接")
        private String link;

        @ApiModelProperty(value = "是否显示内容")
        private Boolean showContent;

        @ApiModelProperty(value = "排序")
        private Integer order;
    }
}
