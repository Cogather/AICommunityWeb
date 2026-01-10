package com.aicommunity.mapper;

import com.aicommunity.dto.ActivityRegistrationResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动报名Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ActivityRegistrationMapper {
    /**
     * 插入报名记录
     */
    void insert(@Param("userId") Long userId, @Param("activityId") Long activityId);

    /**
     * 删除报名记录
     */
    void deleteByUserAndActivity(@Param("userId") Long userId, @Param("activityId") Long activityId);

    /**
     * 检查是否存在报名记录
     */
    boolean existsByUserAndActivity(@Param("userId") Long userId, @Param("activityId") Long activityId);

    /**
     * 根据活动ID查询报名用户列表
     */
    List<ActivityRegistrationResponse.RegistrationUser> selectUsersByActivityId(@Param("activityId") Long activityId);

    /**
     * 统计用户报名活动数
     */
    Integer countByUserId(@Param("userId") Long userId);

    /**
     * 根据活动ID删除报名记录
     */
    void deleteByActivityId(@Param("activityId") Long activityId);
}
