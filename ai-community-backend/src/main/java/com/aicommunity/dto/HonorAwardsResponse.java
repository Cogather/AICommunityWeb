package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 荣誉殿堂奖项响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "荣誉殿堂奖项响应")
public class HonorAwardsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "奖项列表")
    private List<AwardItem> list;

    @Data
    @ApiModel(description = "奖项项")
    public static class AwardItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "奖项ID")
        private Long id;

        @ApiModelProperty(value = "奖项名称")
        private String name;

        @ApiModelProperty(value = "奖项描述")
        private String desc;

        @ApiModelProperty(value = "奖项图片URL")
        private String image;

        @ApiModelProperty(value = "排序")
        private Integer order;
    }
}
