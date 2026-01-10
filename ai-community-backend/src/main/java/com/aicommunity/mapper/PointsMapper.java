package com.aicommunity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 积分Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface PointsMapper {
    /**
     * 插入积分记录
     */
    void insert(@Param("userId") Long userId,
                @Param("points") Integer points,
                @Param("type") String type,
                @Param("targetId") Long targetId,
                @Param("targetType") String targetType,
                @Param("month") String month);

    /**
     * 计算用户总积分
     */
    Integer calculateUserPoints(@Param("userId") Long userId);

    /**
     * 计算用户指定类型的积分
     */
    Integer calculatePointsByType(@Param("userId") Long userId, @Param("type") String type);

    /**
     * 计算用户本月积分
     */
    Integer calculateMonthlyPoints(@Param("userId") Long userId, @Param("month") String month);

    /**
     * 查询本月积分排行榜Top用户
     */
    List<com.aicommunity.dto.RecommendedWinnersResponse.WinnerItem> selectMonthlyTopUsers(@Param("month") String month, @Param("limit") Integer limit);
}
