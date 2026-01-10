package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.UserProfileResponse;
import com.aicommunity.dto.UserPointsResponse;
import com.aicommunity.dto.UserUpdateRequest;
import com.aicommunity.entity.Post;
import com.aicommunity.entity.Comment;
import com.aicommunity.entity.Activity;
import com.aicommunity.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 *
 * @author AI Community Team
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     */
    @ApiOperation(value = "获取当前用户信息", notes = "获取当前登录用户的完整信息")
    @GetMapping("/current")
    public Result<UserProfileResponse> getCurrentUser() {
        UserProfileResponse response = userService.getCurrentUser();
        return Result.success(response);
    }

    /**
     * 获取用户个人积分详情
     */
    @ApiOperation(value = "获取用户个人积分详情", notes = "获取用户个人积分的详细计算信息")
    @GetMapping("/points/calculate")
    public Result<UserPointsResponse> getUserPoints() {
        UserPointsResponse response = userService.getUserPoints();
        return Result.success(response);
    }

    /**
     * 获取用户个人资料
     */
    @ApiOperation(value = "获取用户个人资料", notes = "获取指定用户的个人资料信息")
    @GetMapping({"/{userId}/profile", "/profile"})
    public Result<UserProfileResponse> getUserProfile(
            @ApiParam(value = "用户ID")
            @PathVariable(required = false) Long userId,
            @ApiParam(value = "用户名")
            @RequestParam(required = false) String name) {
        UserProfileResponse response = userService.getUserProfile(userId, name);
        return Result.success(response);
    }

    /**
     * 更新用户资料
     */
    @ApiOperation(value = "更新用户资料", notes = "更新当前用户的个人资料")
    @PutMapping("/profile")
    public Result<?> updateUserProfile(
            @ApiParam(value = "更新请求", required = true)
            @Valid @RequestBody UserUpdateRequest request) {
        userService.updateUserProfile(request);
        return Result.success();
    }

    /**
     * 获取用户发布的帖子
     */
    @ApiOperation(value = "获取用户发布的帖子", notes = "获取指定用户发布的帖子列表")
    @GetMapping("/{userId}/posts")
    public Result<PageResult<Post>> getUserPosts(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long userId,
            PageQuery pageQuery) {
        PageResult<Post> result = userService.getUserPosts(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取用户收藏的帖子
     */
    @ApiOperation(value = "获取用户收藏的帖子", notes = "获取指定用户收藏的帖子列表")
    @GetMapping("/{userId}/favorites")
    public Result<PageResult<Post>> getUserFavorites(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long userId,
            PageQuery pageQuery) {
        PageResult<Post> result = userService.getUserFavorites(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取用户评论列表
     */
    @ApiOperation(value = "获取用户评论列表", notes = "获取指定用户的评论列表")
    @GetMapping("/{userId}/comments")
    public Result<PageResult<Comment>> getUserComments(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long userId,
            PageQuery pageQuery) {
        PageResult<Comment> result = userService.getUserComments(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取用户参与的活动
     */
    @ApiOperation(value = "获取用户参与的活动", notes = "获取指定用户参与（已报名）的活动列表")
    @GetMapping("/{userId}/activities")
    public Result<PageResult<Activity>> getUserActivities(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long userId,
            PageQuery pageQuery) {
        PageResult<Activity> result = userService.getUserActivities(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取用户发布的活动
     */
    @ApiOperation(value = "获取用户发布的活动", notes = "获取指定用户发布的活动列表")
    @GetMapping("/{userId}/activities/created")
    public Result<PageResult<Activity>> getUserCreatedActivities(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long userId,
            @ApiParam(value = "活动状态")
            @RequestParam(required = false) String status,
            PageQuery pageQuery) {
        PageResult<Activity> result = userService.getUserCreatedActivities(userId, status, pageQuery);
        return Result.success(result);
    }
}
