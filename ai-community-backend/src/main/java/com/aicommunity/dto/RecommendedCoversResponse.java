package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐封面响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "推荐封面响应")
public class RecommendedCoversResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "封面列表")
    private List<CoverItem> list;

    @Data
    @ApiModel(description = "封面项")
    public static class CoverItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "封面ID")
        private Long id;

        @ApiModelProperty(value = "封面URL")
        private String url;

        @ApiModelProperty(value = "缩略图URL")
        private String thumbnail;

        @ApiModelProperty(value = "排序")
        private Integer order;
    }
}
