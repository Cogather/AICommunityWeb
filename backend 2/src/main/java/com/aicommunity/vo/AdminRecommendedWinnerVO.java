package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理后台推荐获奖者VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台推荐获奖者")
public class AdminRecommendedWinnerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Integer id;

    @ApiModelProperty(value = "工号")
    private String employeeId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "头像URL")
    private String avatar;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "积分")
    private Integer points;

    @ApiModelProperty(value = "帖子数")
    private Integer postsCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentsCount;

    @ApiModelProperty(value = "活动数")
    private Integer activitiesCount;

    @ApiModelProperty(value = "收到点赞数")
    private Integer likesReceived;

    @ApiModelProperty(value = "收到收藏数")
    private Integer favoritesReceived;

    @ApiModelProperty(value = "是否已获奖")
    private Boolean hasAwarded;

    @ApiModelProperty(value = "荣誉ID")
    private Integer honorId;
}
