package com.aicommunity.mapper;

import com.aicommunity.entity.ActivityRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 活动报名Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ActivityRegistrationMapper {

    /**
     * 查询用户是否已报名
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 报名记录
     */
    ActivityRegistration selectByActivityAndUser(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 插入报名记录
     *
     * @param registration 报名记录
     * @return 影响行数
     */
    int insert(ActivityRegistration registration);

    /**
     * 删除报名记录
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 影响行数
     */
    int delete(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 查询活动报名用户列表
     *
     * @param activityId 活动ID
     * @return 用户列表
     */
    List<Map<String, Object>> selectUsersByActivityId(@Param("activityId") Long activityId);
}
