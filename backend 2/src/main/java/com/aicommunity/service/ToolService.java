package com.aicommunity.service;

import com.aicommunity.vo.*;

import java.util.List;

/**
 * 工具服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface ToolService {

    /**
     * 获取工具列表
     *
     * @param featured 是否只返回推荐工具
     * @return 工具列表
     */
    List<ToolItemVO> getToolList(Boolean featured);

    /**
     * 获取工具帖子列表
     *
     * @param toolId     工具ID，0表示"其他工具"
     * @param category   帖子分类：guide-操作指导，excellent-优秀使用
     * @param tag        标签筛选
     * @param department 部门筛选
     * @param keyword    搜索关键词
     * @param sortBy     排序方式：newest-最新，hot-最热，comments-评论最多
     * @param page       页码
     * @param pageSize   每页条数
     * @return 帖子列表
     */
    ToolPostListVO getToolPostList(Integer toolId, String category, String tag,
                                   String department, String keyword, String sortBy,
                                   Integer page, Integer pageSize);

    /**
     * 获取工具活动列表
     *
     * @param toolId   工具ID
     * @param status   活动状态：upcoming-即将开始，ongoing-进行中，ended-已结束
     * @param page     页码
     * @param pageSize 每页条数
     * @return 活动列表
     */
    ActivityListVO getActivityList(Integer toolId, String status, Integer page, Integer pageSize);

    /**
     * 检查工具Owner权限
     *
     * @param toolId 工具ID
     * @param userId  当前用户ID
     * @return 权限信息
     */
    OwnerPermissionVO checkOwnerPermission(Integer toolId, String userId);

    /**
     * 获取标签统计
     *
     * @param toolId     工具ID，0表示"其他工具"
     * @param department 部门筛选，用于联动统计
     * @return 标签列表
     */
    TagListVO getTags(Integer toolId, String department);

    /**
     * 获取部门统计
     *
     * @param toolId 工具ID，0表示"其他工具"
     * @param tag    标签筛选，用于联动统计
     * @return 部门列表
     */
    DepartmentListVO getDepartments(Integer toolId, String tag);

    /**
     * 发布活动
     *
     * @param request 活动创建请求
     * @param userId  当前用户ID
     * @return 活动创建响应
     */
    ActivityCreateResponseVO createActivity(ActivityCreateRequestVO request, String userId);

    /**
     * 获取活动详情
     *
     * @param activityId 活动ID
     * @param userId     当前用户ID（可选）
     * @return 活动详情
     */
    ActivityDetailVO getActivityDetail(Integer activityId, String userId);

    /**
     * 报名活动
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 报名响应
     */
    ActivityJoinResponseVO joinActivity(Integer activityId, String userId);

    /**
     * 取消报名
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     */
    void cancelJoin(Integer activityId, String userId);

    /**
     * 编辑活动
     *
     * @param activityId 活动ID
     * @param request    活动更新请求
     * @param userId     当前用户ID
     * @return 活动更新响应
     */
    ActivityCreateResponseVO updateActivity(Integer activityId, ActivityUpdateRequestVO request, String userId);

    /**
     * 删除活动
     *
     * @param activityId 活动ID
     * @param userId     当前用户ID
     */
    void deleteActivity(Integer activityId, String userId);

    /**
     * 获取活动报名列表
     *
     * @param activityId 活动ID
     * @param userId     当前用户ID（用于权限检查）
     * @param page       页码
     * @param pageSize   每页数量
     * @return 报名列表
     */
    ActivityRegistrationListVO getActivityRegistrations(Integer activityId, String userId, Integer page, Integer pageSize);

    /**
     * 获取精华帖子（其他工具专有）
     *
     * @param toolId 工具ID，此接口仅支持toolId=0
     * @return 精华帖子响应
     */
    ToolFeaturedPostResponseVO getFeaturedPost(Integer toolId);

    /**
     * 设置精华帖子（其他工具专有）
     *
     * @param request 设置精华帖子请求
     * @param userId  当前用户ID
     * @return 设置响应
     */
    ToolFeaturedPostSetResponseVO setFeaturedPost(ToolFeaturedPostRequestVO request, String userId);
}
