package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.UserProfileDTO;
import com.aicommunity.dto.UserPointsDTO;
import com.aicommunity.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author AI Community Team
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     *
     * @return 当前用户信息
     */
    @ApiOperation(value = "获取当前用户信息", notes = "获取当前登录用户的完整信息")
    @GetMapping("/current")
    public Result<UserProfileDTO> getCurrentUser() {
        UserProfileDTO user = userService.getCurrentUser();
        return Result.success(user);
    }

    /**
     * 计算用户积分
     *
     * @return 用户积分详情
     */
    @ApiOperation(value = "计算用户积分", notes = "获取用户个人积分的详细计算信息")
    @GetMapping("/points/calculate")
    public Result<UserPointsDTO> calculatePoints() {
        UserPointsDTO points = userService.calculatePoints();
        return Result.success(points);
    }

    /**
     * 获取用户个人资料
     *
     * @param userId 用户ID（路径参数）
     * @param name   用户名（查询参数）
     * @return 用户个人资料
     */
    @ApiOperation(value = "获取用户个人资料", notes = "获取指定用户的个人资料信息")
    @GetMapping({"/{userId}/profile", "/profile"})
    public Result<UserProfileDTO> getUserProfile(
            @ApiParam(value = "用户ID", required = false) @PathVariable(required = false) Long userId,
            @ApiParam(value = "用户名", required = false) @RequestParam(required = false) String name) {
        UserProfileDTO user = userService.getUserProfile(userId, name);
        return Result.success(user);
    }

    /**
     * 更新用户资料
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @ApiOperation(value = "更新用户资料", notes = "更新当前用户的个人资料")
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        userService.updateProfile(request);
        return Result.success();
    }

    /**
     * 获取用户发布的帖子
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 帖子列表
     */
    @ApiOperation(value = "获取用户发布的帖子", notes = "获取指定用户发布的帖子列表")
    @GetMapping("/{userId}/posts")
    public Result<PageResult<?>> getUserPosts(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", required = false) @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<?> result = userService.getUserPosts(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取用户收藏的帖子
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 帖子列表
     */
    @ApiOperation(value = "获取用户收藏的帖子", notes = "获取指定用户收藏的帖子列表")
    @GetMapping("/{userId}/favorites")
    public Result<PageResult<?>> getUserFavorites(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", required = false) @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<?> result = userService.getUserFavorites(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取用户评论列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 评论列表
     */
    @ApiOperation(value = "获取用户评论列表", notes = "获取指定用户的评论列表")
    @GetMapping("/{userId}/comments")
    public Result<PageResult<?>> getUserComments(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", required = false) @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<?> result = userService.getUserComments(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取用户参与的活动
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 活动列表
     */
    @ApiOperation(value = "获取用户参与的活动", notes = "获取指定用户参与（已报名）的活动列表")
    @GetMapping("/{userId}/activities")
    public Result<PageResult<?>> getUserActivities(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", required = false) @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<?> result = userService.getUserActivities(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取用户发布的活动
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 活动列表
     */
    @ApiOperation(value = "获取用户发布的活动", notes = "获取指定用户发布的活动列表")
    @GetMapping("/{userId}/activities/created")
    public Result<PageResult<?>> getUserCreatedActivities(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", required = false) @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<?> result = userService.getUserCreatedActivities(userId, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取热门贡献者
     *
     * @param zone  专区
     * @param limit 返回数量
     * @return 贡献者列表
     */
    @ApiOperation(value = "获取热门贡献者", notes = "获取指定专区的热门贡献者列表")
    @GetMapping("/top-contributors")
    public Result<?> getTopContributors(
            @ApiParam(value = "专区", required = false) @RequestParam(required = false) String zone,
            @ApiParam(value = "返回数量", required = false) @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(userService.getTopContributors(zone, limit));
    }

    /**
     * 更新用户资料请求DTO
     */
    public static class UpdateProfileRequest {
        private String bio;
        private String avatar;

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
