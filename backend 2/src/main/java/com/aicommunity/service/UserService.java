package com.aicommunity.service;

import com.aicommunity.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求参数
     * @return 登录响应（包含token和用户信息）
     */
    LoginResponseVO login(LoginRequestVO loginRequest);

    /**
     * 用户退出登录
     *
     * @param userId 用户ID
     */
    void logout(String userId);

    /**
     * 获取当前用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserProfileVO getCurrentUser(String userId);

    /**
     * 根据用户ID获取用户信息（公开信息）
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserProfileVO getUserById(String userId);

    /**
     * 根据工号获取用户信息
     *
     * @param employeeId 工号
     * @return 用户信息
     */
    UserProfileVO getUserByEmployeeId(String employeeId);

    /**
     * 更新当前用户信息
     *
     * @param userId           用户ID
     * @param updateRequestVO 更新请求参数
     */
    void updateCurrentUser(String userId, UserUpdateRequestVO updateRequestVO);

    /**
     * 获取用户发布的帖子列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 帖子列表
     */
    PostListVO getUserPosts(String userId, Integer page, Integer pageSize);

    /**
     * 获取用户收藏的帖子列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 收藏的帖子列表
     */
    PostListVO getUserFavorites(String userId, Integer page, Integer pageSize);

    /**
     * 获取用户评论列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 评论列表
     */
    CommentListVO getUserComments(String userId, Integer page, Integer pageSize);

    /**
     * 获取用户参与的活动列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 活动列表
     */
    ActivityListVO getUserActivities(String userId, Integer page, Integer pageSize);

    /**
     * 获取用户发布的活动列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 活动列表
     */
    ActivityListVO getUserCreatedActivities(String userId, Integer page, Integer pageSize);

    /**
     * 获取用户积分信息
     *
     * @param userId   用户ID
     * @param page     页码（积分历史记录）
     * @param pageSize 每页数量（积分历史记录）
     * @return 积分信息
     */
    UserPointsVO getUserPoints(String userId, Integer page, Integer pageSize);
}
