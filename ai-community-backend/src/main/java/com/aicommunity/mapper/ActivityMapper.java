package com.aicommunity.mapper;

import com.aicommunity.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ActivityMapper {
    /**
     * 根据ID查询活动
     */
    Activity selectById(@Param("id") Long id);

    /**
     * 查询活动列表
     */
    List<Activity> selectActivities(@Param("toolId") Long toolId,
                                    @Param("type") String type,
                                    @Param("status") String status);

    /**
     * 插入活动
     */
    void insert(Activity activity);

    /**
     * 更新活动
     */
    void updateById(Activity activity);

    /**
     * 删除活动
     */
    void deleteById(@Param("id") Long id);

    /**
     * 根据报名用户ID查询活动列表
     */
    List<Activity> selectByRegisteredUserId(@Param("userId") Long userId);

    /**
     * 根据发布者ID查询活动列表
     */
    List<Activity> selectByAuthorId(@Param("authorId") Long authorId, @Param("status") String status);

    /**
     * 增加报名人数
     */
    void incrementRegisteredCount(@Param("id") Long id);

    /**
     * 减少报名人数
     */
    void decrementRegisteredCount(@Param("id") Long id);
}
