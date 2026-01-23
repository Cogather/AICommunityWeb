package com.aicommunity.controller;

import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.Result;
import com.aicommunity.service.UserService;
import com.aicommunity.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户信息控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户信息接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     *
     * @param userId 用户ID（从请求头或token中获取）
     * @return 用户信息
     */
    @GetMapping("/current")
    @ApiOperation(value = "获取当前用户信息", notes = "获取当前登录用户的完整信息，包括角色、部门、统计数据等")
    public Result<UserProfileVO> getCurrentUser(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("获取当前用户信息，用户ID：{}", userId);
        try {
            if (userId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            UserProfileVO userProfile = userService.getCurrentUser(userId);
            return Result.success(userProfile);
        } catch (RuntimeException e) {
            log.error("获取当前用户信息失败：{}", e.getMessage());
            if ("用户不存在".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.NOT_FOUND.getCode(), "用户不存在");
            }
            return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("获取当前用户信息异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户信息失败");
        }
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息（公开信息）
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取用户信息", notes = "获取指定用户的公开信息（用于查看他人主页）")
    public Result<UserProfileVO> getUserById(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable("id") String id) {
        log.info("根据ID获取用户信息，用户ID：{}", id);
        try {
            UserProfileVO userProfile = userService.getUserById(id);
            return Result.success(userProfile);
        } catch (RuntimeException e) {
            log.error("获取用户信息失败：{}", e.getMessage());
            if ("用户不存在".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.NOT_FOUND.getCode(), "用户不存在");
            }
            return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户信息失败");
        }
    }

    /**
     * 根据工号获取用户信息
     *
     * @param employeeId 工号
     * @return 用户信息
     */
    @GetMapping("/by-employee-id/{employeeId}")
    @ApiOperation(value = "根据工号获取用户信息", notes = "通过工号查询用户信息（用于管理后台按工号搜索）")
    public Result<UserProfileVO> getUserByEmployeeId(
            @ApiParam(value = "工号", required = true, example = "E001")
            @PathVariable("employeeId") String employeeId) {
        log.info("根据工号获取用户信息，工号：{}", employeeId);
        try {
            UserProfileVO userProfile = userService.getUserByEmployeeId(employeeId);
            return Result.success(userProfile);
        } catch (RuntimeException e) {
            log.error("获取用户信息失败：{}", e.getMessage());
            if ("用户不存在".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.NOT_FOUND.getCode(), "用户不存在");
            }
            return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户信息失败");
        }
    }

    /**
     * 更新当前用户信息
     *
     * @param userId         用户ID（从请求头或token中获取）
     * @param updateRequestVO 更新请求参数
     * @return 更新结果
     */
    @PutMapping("/current")
    @ApiOperation(value = "更新用户信息", notes = "更新当前用户的个人信息（头像、简介等）")
    public Result<Void> updateCurrentUser(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody UserUpdateRequestVO updateRequestVO) {
        log.info("更新用户信息，用户ID：{}", userId);
        try {
            if (userId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            userService.updateCurrentUser(userId, updateRequestVO);
            return Result.success();
        } catch (RuntimeException e) {
            log.error("更新用户信息失败：{}", e.getMessage());
            if ("用户不存在".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.NOT_FOUND.getCode(), "用户不存在");
            }
            return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("更新用户信息异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "更新用户信息失败");
        }
    }

    /**
     * 获取用户平台信息
     *
     * @param userId 用户ID（可选，不传则获取当前登录用户）
     * @return 用户平台信息
     */
    @GetMapping("/profile")
    @ApiOperation(value = "获取用户平台信息", notes = "获取当前用户或指定用户的平台信息，包括统计数据、积分、花朵数等")
    public Result<UserProfileVO> getUserProfile(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @ApiParam(value = "用户ID", required = false) @RequestParam(value = "userId", required = false) String targetUserId) {
        log.info("获取用户平台信息，当前用户ID：{}，目标用户ID：{}", userId, targetUserId);
        try {
            String finalUserId = targetUserId != null ? targetUserId : userId;
            if (finalUserId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            UserProfileVO userProfile = userService.getCurrentUser(finalUserId);
            return Result.success(userProfile);
        } catch (RuntimeException e) {
            log.error("获取用户平台信息失败：{}", e.getMessage());
            if ("用户不存在".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.NOT_FOUND.getCode(), "用户不存在");
            }
            return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("获取用户平台信息异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户平台信息失败");
        }
    }

    /**
     * 获取用户积分信息
     *
     * @param userId   用户ID（从请求头或token中获取）
     * @param page     页码
     * @param pageSize 每页数量
     * @return 积分信息
     */
    @GetMapping("/points")
    @ApiOperation(value = "获取用户积分信息", notes = "获取用户的积分详情，包括当前积分、积分来源、积分历史等")
    public Result<UserPointsVO> getUserPoints(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "15") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        log.info("获取用户积分信息，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);
        try {
            if (userId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            UserPointsVO pointsVO = userService.getUserPoints(userId, page, pageSize);
            return Result.success(pointsVO);
        } catch (Exception e) {
            log.error("获取用户积分信息异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户积分信息失败");
        }
    }

    /**
     * 获取用户发布的帖子列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 帖子列表
     */
    @GetMapping("/{userId}/posts")
    @ApiOperation(value = "获取用户发布的帖子列表", notes = "获取指定用户发布的所有帖子")
    public Result<PostListVO> getUserPosts(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable("userId") String userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "15") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        log.info("获取用户发布的帖子列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);
        try {
            PostListVO postList = userService.getUserPosts(userId, page, pageSize);
            return Result.success(postList);
        } catch (Exception e) {
            log.error("获取用户发布的帖子列表异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户发布的帖子列表失败");
        }
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 收藏的帖子列表
     */
    @GetMapping("/{userId}/favorites")
    @ApiOperation(value = "获取用户收藏的帖子列表", notes = "获取指定用户收藏的所有帖子")
    public Result<PostListVO> getUserFavorites(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable("userId") String userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "15") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        log.info("获取用户收藏的帖子列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);
        try {
            PostListVO postList = userService.getUserFavorites(userId, page, pageSize);
            return Result.success(postList);
        } catch (Exception e) {
            log.error("获取用户收藏的帖子列表异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户收藏的帖子列表失败");
        }
    }

    /**
     * 获取用户评论列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 评论列表
     */
    @GetMapping("/{userId}/comments")
    @ApiOperation(value = "获取用户评论列表", notes = "获取指定用户发表的所有评论")
    public Result<CommentListVO> getUserComments(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable("userId") String userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "15") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        log.info("获取用户评论列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);
        try {
            CommentListVO commentList = userService.getUserComments(userId, page, pageSize);
            return Result.success(commentList);
        } catch (Exception e) {
            log.error("获取用户评论列表异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户评论列表失败");
        }
    }

    /**
     * 获取用户参与的活动列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 活动列表
     */
    @GetMapping("/{userId}/activities")
    @ApiOperation(value = "获取用户参与的活动列表", notes = "获取指定用户报名参与的所有活动")
    public Result<ActivityListVO> getUserActivities(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable("userId") String userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "15") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        log.info("获取用户参与的活动列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);
        try {
            ActivityListVO activityList = userService.getUserActivities(userId, page, pageSize);
            return Result.success(activityList);
        } catch (Exception e) {
            log.error("获取用户参与的活动列表异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户参与的活动列表失败");
        }
    }

    /**
     * 获取用户发布的活动列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 活动列表
     */
    @GetMapping("/{userId}/created-activities")
    @ApiOperation(value = "获取用户发布的活动列表", notes = "获取指定用户作为活动发起人发布的所有活动")
    public Result<ActivityListVO> getUserCreatedActivities(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable("userId") String userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "15") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        log.info("获取用户发布的活动列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);
        try {
            ActivityListVO activityList = userService.getUserCreatedActivities(userId, page, pageSize);
            return Result.success(activityList);
        } catch (Exception e) {
            log.error("获取用户发布的活动列表异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取用户发布的活动列表失败");
        }
    }
}
