package com.aicommunity.mapper;

import com.aicommunity.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 活动Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ActivityMapper {

    /**
     * 根据条件查询活动列表
     *
     * @param toolId 工具ID
     * @param type   活动类型
     * @param status 活动状态
     * @return 活动列表
     */
    List<Map<String, Object>> selectByCondition(
            @Param("toolId") Long toolId,
            @Param("type") String type,
            @Param("status") String status);

    /**
     * 根据ID查询活动
     *
     * @param id 活动ID
     * @return 活动
     */
    Activity selectById(@Param("id") Long id);

    /**
     * 插入活动
     *
     * @param activity 活动
     * @return 影响行数
     */
    int insert(Activity activity);

    /**
     * 更新活动
     *
     * @param activity 活动
     * @return 影响行数
     */
    int update(Activity activity);

    /**
     * 删除活动
     *
     * @param id 活动ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新报名人数
     *
     * @param id    活动ID
     * @param delta 变化量
     * @return 影响行数
     */
    int updateRegisteredCount(@Param("id") Long id, @Param("delta") Integer delta);

    /**
     * 根据用户ID查询参与的活动
     *
     * @param userId 用户ID
     * @return 活动列表
     */
    List<Map<String, Object>> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据作者ID查询发布的活动
     *
     * @param authorId 作者ID
     * @return 活动列表
     */
    List<Map<String, Object>> selectByAuthorId(@Param("authorId") Long authorId);

    /**
     * 统计用户参与的活动数
     *
     * @param userId 用户ID
     * @return 活动数
     */
    int countByUserId(@Param("userId") Long userId);
}
