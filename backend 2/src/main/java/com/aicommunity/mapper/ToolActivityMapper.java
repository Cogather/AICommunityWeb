package com.aicommunity.mapper;

import com.aicommunity.entity.ToolActivity;
import com.aicommunity.entity.ToolActivityJoin;
import com.aicommunity.vo.ActivityParticipantVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工具活动Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface ToolActivityMapper {

    /**
     * 查询工具活动列表（支持分页、状态筛选）
     *
     * @param toolId    工具ID
     * @param status    活动状态：upcoming-即将开始，ongoing-进行中，ended-已结束
     * @param offset    偏移量
     * @param pageSize  每页数量
     * @return 活动列表
     */
    List<ToolActivity> selectActivities(@Param("toolId") Integer toolId,
                                       @Param("status") String status,
                                       @Param("offset") Integer offset,
                                       @Param("pageSize") Integer pageSize);

    /**
     * 统计工具活动总数（支持状态筛选）
     *
     * @param toolId 工具ID
     * @param status 活动状态
     * @return 总数
     */
    Long countActivities(@Param("toolId") Integer toolId,
                        @Param("status") String status);

    /**
     * 根据活动ID查询活动详情
     *
     * @param activityId 活动ID
     * @return 活动详情
     */
    ToolActivity selectActivityById(@Param("activityId") Integer activityId);

    /**
     * 插入活动
     *
     * @param activity 活动对象
     * @return 影响行数
     */
    int insertActivity(ToolActivity activity);

    /**
     * 更新活动
     *
     * @param activity 活动对象
     * @return 影响行数
     */
    int updateActivity(ToolActivity activity);

    /**
     * 查询活动参与者列表（最多返回前10个）
     *
     * @param activityId 活动ID
     * @return 参与者列表
     */
    List<ActivityParticipantVO> selectParticipants(@Param("activityId") Integer activityId);

    /**
     * 查询用户是否已报名活动
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 报名记录
     */
    ToolActivityJoin selectJoinByUser(@Param("activityId") Integer activityId,
                                     @Param("userId") String userId);

    /**
     * 插入活动报名记录
     *
     * @param join 报名记录
     * @return 影响行数
     */
    int insertJoin(ToolActivityJoin join);

    /**
     * 更新活动报名记录（取消报名）
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 影响行数
     */
    int cancelJoin(@Param("activityId") Integer activityId,
                   @Param("userId") String userId);

    /**
     * 统计活动当前报名人数（未取消的）
     *
     * @param activityId 活动ID
     * @return 报名人数
     */
    Integer countParticipants(@Param("activityId") Integer activityId);

    /**
     * 查询活动报名列表（分页）
     *
     * @param activityId 活动ID
     * @param offset     偏移量
     * @param pageSize   每页数量
     * @return 报名用户列表
     */
    List<ActivityParticipantVO> selectRegistrations(@Param("activityId") Integer activityId,
                                                    @Param("offset") Integer offset,
                                                    @Param("pageSize") Integer pageSize);

    /**
     * 统计活动报名总数（未取消的）
     *
     * @param activityId 活动ID
     * @return 报名总数
     */
    Long countRegistrations(@Param("activityId") Integer activityId);

    /**
     * 删除活动（同时删除所有报名记录）
     *
     * @param activityId 活动ID
     * @return 影响行数
     */
    int deleteActivity(@Param("activityId") Integer activityId);

    /**
     * 删除活动报名记录（删除活动时调用）
     *
     * @param activityId 活动ID
     * @return 影响行数
     */
    int deleteJoinsByActivity(@Param("activityId") Integer activityId);

    /**
     * 查询用户参与的活动列表（分页）
     *
     * @param userId   用户ID
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 活动列表
     */
    List<ToolActivity> selectUserActivities(@Param("userId") String userId,
                                            @Param("offset") Integer offset,
                                            @Param("pageSize") Integer pageSize);

    /**
     * 统计用户参与的活动总数
     *
     * @param userId 用户ID
     * @return 活动总数
     */
    Long countUserActivities(@Param("userId") String userId);

    /**
     * 查询用户发布的活动列表（分页）
     *
     * @param userId   用户ID（通过authorId字段关联）
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 活动列表
     */
    List<ToolActivity> selectUserCreatedActivities(@Param("userId") String userId,
                                                   @Param("offset") Integer offset,
                                                   @Param("pageSize") Integer pageSize);

    /**
     * 统计用户发布的活动总数
     *
     * @param userId 用户ID
     * @return 活动总数
     */
    Long countUserCreatedActivities(@Param("userId") String userId);
}
