package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户积分DTO
 *
 * @author AI Community Team
 */
@Data
public class UserPointsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总积分
     */
    private Integer points;

    /**
     * 积分明细
     */
    private PointsBreakdown breakdown;

    /**
     * 积分明细
     */
    @Data
    public static class PointsBreakdown implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 发帖获得的积分
         */
        private Integer posts;

        /**
         * 评论获得的积分
         */
        private Integer comments;

        /**
         * 被点赞获得的积分
         */
        private Integer likesReceived;

        /**
         * 被收藏获得的积分
         */
        private Integer favoritesReceived;

        /**
         * 参加活动获得的积分
         */
        private Integer activities;
    }
}
