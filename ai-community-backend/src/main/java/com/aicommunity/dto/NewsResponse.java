package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 社区头条响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "社区头条响应")
public class NewsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "头条列表")
    private List<NewsItem> list;

    @Data
    @ApiModel(description = "头条项")
    public static class NewsItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "头条ID")
        private Long id;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "图片URL")
        private String image;

        @ApiModelProperty(value = "日期")
        private String date;

        @ApiModelProperty(value = "链接")
        private String link;

        @ApiModelProperty(value = "排序")
        private Integer order;
    }
}
