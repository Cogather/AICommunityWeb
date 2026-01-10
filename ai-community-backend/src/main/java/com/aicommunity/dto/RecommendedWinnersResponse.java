package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐获奖者响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "推荐获奖者响应")
public class RecommendedWinnersResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "推荐用户列表")
    private List<WinnerItem> list;

    @ApiModelProperty(value = "当前查询的月份（格式：YYYY-MM）")
    private String month;

    @Data
    @ApiModel(description = "获奖者项")
    public static class WinnerItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "用户ID")
        private Long id;

        @ApiModelProperty(value = "工号")
        private String employeeId;

        @ApiModelProperty(value = "姓名")
        private String name;

        @ApiModelProperty(value = "头像")
        private String avatar;

        @ApiModelProperty(value = "部门")
        private String department;

        @ApiModelProperty(value = "本月积分")
        private Integer points;

        @ApiModelProperty(value = "本月发帖数")
        private Integer postsCount;

        @ApiModelProperty(value = "本月评论数")
        private Integer commentsCount;

        @ApiModelProperty(value = "本月参加活动数")
        private Integer activitiesCount;

        @ApiModelProperty(value = "本月被点赞数")
        private Integer likesReceived;

        @ApiModelProperty(value = "本月被收藏数")
        private Integer favoritesReceived;

        @ApiModelProperty(value = "是否已评奖")
        private Boolean hasAwarded;
    }
}
