package com.aicommunity.mapper;

import com.aicommunity.entity.PointsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 积分记录Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface PointsRecordMapper {

    /**
     * 插入积分记录
     *
     * @param record 积分记录
     * @return 影响行数
     */
    int insert(PointsRecord record);

    /**
     * 根据用户ID和月份查询积分记录
     *
     * @param userId 用户ID
     * @param month  月份
     * @return 积分记录列表
     */
    List<Map<String, Object>> selectByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);

    /**
     * 统计用户某月总积分
     *
     * @param userId 用户ID
     * @param month  月份
     * @return 总积分
     */
    Integer sumPointsByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);

    /**
     * 查询本月积分Top用户（排除管理员）
     *
     * @param month 月份
     * @param limit 返回数量
     * @return 用户列表
     */
    List<Map<String, Object>> selectTopUsersByMonth(@Param("month") String month, @Param("limit") Integer limit);
}
