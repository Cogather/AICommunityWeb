package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.controller.ActivityController;
import com.aicommunity.dto.ActivityDetailDTO;
import com.aicommunity.dto.ActivityListDTO;

import java.util.Map;

/**
 * 活动服务接口
 *
 * @author AI Community Team
 */
public interface ActivityService {

    /**
     * 获取活动列表
     *
     * @param toolId    工具ID
     * @param type      活动类型
     * @param status    活动状态
     * @param pageQuery 分页参数
     * @return 活动列表
     */
    PageResult<ActivityListDTO> getActivities(Long toolId, String type, String status, PageQuery pageQuery);

    /**
     * 获取活动详情
     *
     * @param id 活动ID
     * @return 活动详情
     */
    ActivityDetailDTO getActivityDetail(Long id);

    /**
     * 创建活动
     *
     * @param request 创建请求
     * @return 创建结果
     */
    ActivityController.CreateActivityResponse createActivity(ActivityController.CreateActivityRequest request);

    /**
     * 更新活动
     *
     * @param id      活动ID
     * @param request 更新请求
     * @return 更新结果
     */
    ActivityController.UpdateActivityResponse updateActivity(Long id, ActivityController.CreateActivityRequest request);

    /**
     * 删除活动
     *
     * @param id 活动ID
     */
    void deleteActivity(Long id);

    /**
     * 报名活动
     *
     * @param id 活动ID
     * @return 报名结果
     */
    ActivityController.RegisterActivityResponse registerActivity(Long id);

    /**
     * 取消报名
     *
     * @param id 活动ID
     * @return 取消报名结果
     */
    ActivityController.CancelRegistrationResponse cancelRegistration(Long id);

    /**
     * 获取活动报名用户列表
     *
     * @param id        活动ID
     * @param pageQuery 分页参数
     * @return 报名用户列表
     */
    PageResult<Map<String, Object>> getRegistrations(Long id, PageQuery pageQuery);
}
