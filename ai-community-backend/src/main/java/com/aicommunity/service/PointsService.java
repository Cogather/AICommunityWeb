package com.aicommunity.service;

import java.util.Date;

/**
 * 积分服务接口
 *
 * @author AI Community Team
 */
public interface PointsService {
    /**
     * 添加积分
     *
     * @param userId 用户ID
     * @param points 积分数量
     * @param type 积分类型：post-发帖，comment-评论，like_received-被点赞，favorite_received-被收藏，activity-参加活动
     * @param targetId 关联目标ID
     * @param targetType 关联目标类型
     */
    void addPoints(Long userId, Integer points, String type, Long targetId, String targetType);

    /**
     * 计算用户总积分
     *
     * @param userId 用户ID
     * @return 总积分
     */
    Integer calculateUserPoints(Long userId);

    /**
     * 计算用户指定类型的积分
     *
     * @param userId 用户ID
     * @param type 积分类型
     * @return 积分数量
     */
    Integer calculatePointsByType(Long userId, String type);

    /**
     * 计算用户本月积分
     *
     * @param userId 用户ID
     * @param month 月份（格式：YYYY-MM）
     * @return 本月积分
     */
    Integer calculateMonthlyPoints(Long userId, String month);

    /**
     * 获取本月积分排行榜Top用户
     *
     * @param month 月份（格式：YYYY-MM）
     * @param limit 返回数量
     * @return Top用户列表
     */
    List<com.aicommunity.dto.RecommendedWinnersResponse.WinnerItem> getMonthlyTopUsers(String month, Integer limit);
}
