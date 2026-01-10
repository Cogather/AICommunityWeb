package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户积分响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "用户积分响应")
public class UserPointsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总积分")
    private Integer points;

    @ApiModelProperty(value = "积分明细")
    private PointsBreakdown breakdown;

    @Data
    @ApiModel(description = "积分明细")
    public static class PointsBreakdown implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "发帖获得的积分")
        private Integer posts;

        @ApiModelProperty(value = "评论获得的积分")
        private Integer comments;

        @ApiModelProperty(value = "被点赞获得的积分")
        private Integer likesReceived;

        @ApiModelProperty(value = "被收藏获得的积分")
        private Integer favoritesReceived;

        @ApiModelProperty(value = "参加活动获得的积分")
        private Integer activities;
    }
}
