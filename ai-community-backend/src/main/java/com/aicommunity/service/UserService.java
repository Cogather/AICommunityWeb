package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.UserProfileResponse;
import com.aicommunity.dto.UserPointsResponse;
import com.aicommunity.dto.UserUpdateRequest;
import com.aicommunity.entity.Activity;
import com.aicommunity.entity.Comment;
import com.aicommunity.entity.Post;

/**
 * 用户服务接口
 *
 * @author AI Community Team
 */
public interface UserService {
    /**
     * 获取当前用户信息
     *
     * @return 用户资料
     */
    UserProfileResponse getCurrentUser();

    /**
     * 获取用户个人积分详情
     *
     * @return 积分详情
     */
    UserPointsResponse getUserPoints();

    /**
     * 获取用户个人资料
     *
     * @param userId 用户ID
     * @param name 用户名
     * @return 用户资料
     */
    UserProfileResponse getUserProfile(Long userId, String name);

    /**
     * 更新用户资料
     *
     * @param request 更新请求
     */
    void updateUserProfile(UserUpdateRequest request);

    /**
     * 获取用户发布的帖子
     *
     * @param userId 用户ID
     * @param pageQuery 分页参数
     * @return 帖子列表
     */
    PageResult<Post> getUserPosts(Long userId, PageQuery pageQuery);

    /**
     * 获取用户收藏的帖子
     *
     * @param userId 用户ID
     * @param pageQuery 分页参数
     * @return 帖子列表
     */
    PageResult<Post> getUserFavorites(Long userId, PageQuery pageQuery);

    /**
     * 获取用户评论列表
     *
     * @param userId 用户ID
     * @param pageQuery 分页参数
     * @return 评论列表
     */
    PageResult<Comment> getUserComments(Long userId, PageQuery pageQuery);

    /**
     * 获取用户参与的活动
     *
     * @param userId 用户ID
     * @param pageQuery 分页参数
     * @return 活动列表
     */
    PageResult<Activity> getUserActivities(Long userId, PageQuery pageQuery);

    /**
     * 获取用户发布的活动
     *
     * @param userId 用户ID
     * @param status 活动状态
     * @param pageQuery 分页参数
     * @return 活动列表
     */
    PageResult<Activity> getUserCreatedActivities(Long userId, String status, PageQuery pageQuery);
}
