package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.UserProfileDTO;
import com.aicommunity.dto.UserPointsDTO;

/**
 * 用户服务接口
 *
 * @author AI Community Team
 */
public interface UserService {

    /**
     * 获取当前用户信息
     *
     * @return 当前用户信息
     */
    UserProfileDTO getCurrentUser();

    /**
     * 计算用户积分
     *
     * @return 用户积分详情
     */
    UserPointsDTO calculatePoints();

    /**
     * 获取用户个人资料
     *
     * @param userId 用户ID
     * @param name   用户名
     * @return 用户个人资料
     */
    UserProfileDTO getUserProfile(Long userId, String name);

    /**
     * 更新用户资料
     *
     * @param request 更新请求
     */
    void updateProfile(UserController.UpdateProfileRequest request);

    /**
     * 获取用户发布的帖子
     *
     * @param userId    用户ID
     * @param pageQuery 分页参数
     * @return 帖子列表
     */
    PageResult<?> getUserPosts(Long userId, PageQuery pageQuery);

    /**
     * 获取用户收藏的帖子
     *
     * @param userId    用户ID
     * @param pageQuery 分页参数
     * @return 帖子列表
     */
    PageResult<?> getUserFavorites(Long userId, PageQuery pageQuery);

    /**
     * 获取用户评论列表
     *
     * @param userId    用户ID
     * @param pageQuery 分页参数
     * @return 评论列表
     */
    PageResult<?> getUserComments(Long userId, PageQuery pageQuery);

    /**
     * 获取用户参与的活动
     *
     * @param userId    用户ID
     * @param pageQuery 分页参数
     * @return 活动列表
     */
    PageResult<?> getUserActivities(Long userId, PageQuery pageQuery);

    /**
     * 获取用户发布的活动
     *
     * @param userId    用户ID
     * @param pageQuery 分页参数
     * @return 活动列表
     */
    PageResult<?> getUserCreatedActivities(Long userId, PageQuery pageQuery);

    /**
     * 获取热门贡献者
     *
     * @param zone  专区
     * @param limit 返回数量
     * @return 贡献者列表
     */
    Object getTopContributors(String zone, Integer limit);
}
