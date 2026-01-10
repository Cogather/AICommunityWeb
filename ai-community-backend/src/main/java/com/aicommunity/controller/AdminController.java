package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.*;
import com.aicommunity.entity.User;
import com.aicommunity.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理后台控制器
 *
 * @author AI Community Team
 */
@Api(tags = "管理后台")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ==================== 首页管理 ====================

    /**
     * 获取首页轮播图配置
     */
    @ApiOperation(value = "获取首页轮播图配置", notes = "获取当前配置的轮播图列表")
    @GetMapping("/home/carousel")
    public Result<CarouselResponse> getCarouselConfig() {
        CarouselResponse response = adminService.getCarouselConfig();
        return Result.success(response);
    }

    /**
     * 保存首页轮播图配置
     */
    @ApiOperation(value = "保存首页轮播图配置", notes = "保存轮播图配置")
    @PutMapping("/home/carousel")
    public Result<?> saveCarouselConfig(
            @ApiParam(value = "轮播图配置", required = true)
            @Valid @RequestBody CarouselResponse request) {
        adminService.saveCarouselConfig(request);
        return Result.success();
    }

    /**
     * 获取荣誉殿堂Banner配置
     */
    @ApiOperation(value = "获取荣誉殿堂Banner配置", notes = "获取荣誉殿堂Banner配置")
    @GetMapping("/home/honor-banner")
    public Result<HonorBannerResponse> getHonorBannerConfig() {
        HonorBannerResponse response = adminService.getHonorBannerConfig();
        return Result.success(response);
    }

    /**
     * 保存荣誉殿堂Banner配置
     */
    @ApiOperation(value = "保存荣誉殿堂Banner配置", notes = "保存荣誉殿堂Banner配置")
    @PutMapping("/home/honor-banner")
    public Result<?> saveHonorBannerConfig(
            @ApiParam(value = "Banner配置", required = true)
            @Valid @RequestBody HonorBannerResponse request) {
        adminService.saveHonorBannerConfig(request);
        return Result.success();
    }

    /**
     * 获取荣誉殿堂奖项列表配置
     */
    @ApiOperation(value = "获取荣誉殿堂奖项列表配置", notes = "获取荣誉殿堂奖项列表配置")
    @GetMapping("/home/honor-awards")
    public Result<HonorAwardsResponse> getHonorAwardsConfig() {
        HonorAwardsResponse response = adminService.getHonorAwardsConfig();
        return Result.success(response);
    }

    /**
     * 保存荣誉殿堂奖项列表配置
     */
    @ApiOperation(value = "保存荣誉殿堂奖项列表配置", notes = "保存荣誉殿堂奖项列表配置")
    @PutMapping("/home/honor-awards")
    public Result<?> saveHonorAwardsConfig(
            @ApiParam(value = "奖项配置", required = true)
            @Valid @RequestBody HonorAwardsResponse request) {
        adminService.saveHonorAwardsConfig(request);
        return Result.success();
    }

    /**
     * 获取社区头条配置
     */
    @ApiOperation(value = "获取社区头条配置", notes = "获取社区头条配置")
    @GetMapping("/home/news")
    public Result<NewsResponse> getNewsConfig() {
        NewsResponse response = adminService.getNewsConfig();
        return Result.success(response);
    }

    /**
     * 保存社区头条配置
     */
    @ApiOperation(value = "保存社区头条配置", notes = "保存社区头条配置")
    @PutMapping("/home/news")
    public Result<?> saveNewsConfig(
            @ApiParam(value = "头条配置", required = true)
            @Valid @RequestBody NewsResponse request) {
        adminService.saveNewsConfig(request);
        return Result.success();
    }

    // ==================== 工具管理 ====================

    /**
     * 获取AI工具配置
     */
    @ApiOperation(value = "获取AI工具配置", notes = "获取AI工具配置")
    @GetMapping("/tools")
    public Result<ToolsConfigResponse> getToolsConfig() {
        ToolsConfigResponse response = adminService.getToolsConfig();
        return Result.success(response);
    }

    /**
     * 保存AI工具配置
     */
    @ApiOperation(value = "保存AI工具配置", notes = "保存AI工具配置")
    @PutMapping("/tools")
    public Result<?> saveToolsConfig(
            @ApiParam(value = "工具配置", required = true)
            @Valid @RequestBody ToolsConfigResponse request) {
        adminService.saveToolsConfig(request);
        return Result.success();
    }

    // ==================== 扶摇Agent应用 ====================

    /**
     * 获取扶摇Agent应用置顶帖子配置
     */
    @ApiOperation(value = "获取扶摇Agent应用置顶帖子配置", notes = "获取扶摇Agent应用置顶帖子配置")
    @GetMapping("/agent/featured-post")
    public Result<AgentFeaturedPostResponse> getAgentFeaturedPostConfig() {
        AgentFeaturedPostResponse response = adminService.getAgentFeaturedPostConfig();
        return Result.success(response);
    }

    /**
     * 保存扶摇Agent应用置顶帖子配置
     */
    @ApiOperation(value = "保存扶摇Agent应用置顶帖子配置", notes = "保存扶摇Agent应用置顶帖子配置")
    @PutMapping("/agent/featured-post")
    public Result<?> saveAgentFeaturedPostConfig(
            @ApiParam(value = "置顶帖子配置", required = true)
            @Valid @RequestBody AgentFeaturedPostResponse request) {
        adminService.saveAgentFeaturedPostConfig(request);
        return Result.success();
    }

    // ==================== 推荐封面 ====================

    /**
     * 获取推荐封面配置
     */
    @ApiOperation(value = "获取推荐封面配置", notes = "获取推荐封面配置")
    @GetMapping("/posts/recommended-covers")
    public Result<RecommendedCoversResponse> getRecommendedCoversConfig() {
        RecommendedCoversResponse response = adminService.getRecommendedCoversConfig();
        return Result.success(response);
    }

    /**
     * 保存推荐封面配置
     */
    @ApiOperation(value = "保存推荐封面配置", notes = "保存推荐封面配置")
    @PutMapping("/posts/recommended-covers")
    public Result<?> saveRecommendedCoversConfig(
            @ApiParam(value = "封面配置", required = true)
            @Valid @RequestBody RecommendedCoversResponse request) {
        adminService.saveRecommendedCoversConfig(request);
        return Result.success();
    }

    // ==================== 人员管理 ====================

    /**
     * 搜索用户
     */
    @ApiOperation(value = "搜索用户", notes = "通过工号、姓名或邮箱搜索用户")
    @GetMapping("/users/search")
    public Result<List<User>> searchUsers(
            @ApiParam(value = "工号")
            @RequestParam(required = false) String employeeId,
            @ApiParam(value = "姓名")
            @RequestParam(required = false) String name,
            @ApiParam(value = "邮箱")
            @RequestParam(required = false) String email,
            @ApiParam(value = "角色")
            @RequestParam(required = false) String role) {
        List<User> users = adminService.searchUsers(employeeId, name, email, role);
        return Result.success(users);
    }

    /**
     * 获取用户列表
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表，支持筛选")
    @GetMapping("/users")
    public Result<PageResult<User>> getUsers(
            @ApiParam(value = "角色")
            @RequestParam(required = false) String role,
            @ApiParam(value = "工具ID")
            @RequestParam(required = false) Long toolId,
            @ApiParam(value = "搜索关键词")
            @RequestParam(required = false) String search,
            PageQuery pageQuery) {
        PageResult<User> result = adminService.getUsers(role, toolId, search, pageQuery);
        return Result.success(result);
    }

    /**
     * 添加用户角色
     */
    @ApiOperation(value = "添加用户角色", notes = "添加用户角色（管理员或工具Owner）")
    @PostMapping("/users/{userId}/role")
    public Result<?> addUserRole(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long userId,
            @ApiParam(value = "角色请求", required = true)
            @Valid @RequestBody UserRoleRequest request) {
        adminService.addUserRole(userId, request);
        return Result.success();
    }

    /**
     * 移除用户角色
     */
    @ApiOperation(value = "移除用户角色", notes = "移除用户的指定角色")
    @DeleteMapping("/users/{userId}/role")
    public Result<?> removeUserRole(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long userId,
            @ApiParam(value = "角色请求", required = true)
            @Valid @RequestBody UserRoleRequest request) {
        adminService.removeUserRole(userId, request);
        return Result.success();
    }

    // ==================== 获奖者推荐 ====================

    /**
     * 获取本月积分排行榜
     */
    @ApiOperation(value = "获取本月积分排行榜", notes = "获取本月积分靠前的用户，用于管理员推荐评奖")
    @GetMapping("/honors/recommended-winners")
    public Result<RecommendedWinnersResponse> getRecommendedWinners(
            @ApiParam(value = "月份（格式：YYYY-MM）")
            @RequestParam(required = false) String month,
            @ApiParam(value = "返回数量", example = "3")
            @RequestParam(required = false, defaultValue = "3") Integer limit) {
        RecommendedWinnersResponse response = adminService.getRecommendedWinners(month, limit);
        return Result.success(response);
    }

    /**
     * 设置用户获奖
     */
    @ApiOperation(value = "设置用户获奖", notes = "为推荐的用户设置获奖记录")
    @PostMapping("/honors")
    public Result<HonorCreateResponse> createHonor(
            @ApiParam(value = "获奖请求", required = true)
            @Valid @RequestBody HonorCreateRequest request) {
        HonorCreateResponse response = adminService.createHonor(request);
        return Result.success(response);
    }

    /**
     * 取消用户获奖
     */
    @ApiOperation(value = "取消用户获奖", notes = "删除已设置的获奖记录")
    @DeleteMapping("/honors/{id}")
    public Result<?> deleteHonor(
            @ApiParam(value = "荣誉ID", required = true)
            @PathVariable Long id) {
        adminService.deleteHonor(id);
        return Result.success();
    }

    // ==================== 文件上传 ====================

    /**
     * 上传图片
     */
    @ApiOperation(value = "上传图片", notes = "上传图片文件")
    @PostMapping("/upload/image")
    public Result<ImageUploadResponse> uploadImage(
            @ApiParam(value = "图片文件", required = true)
            @RequestParam("file") MultipartFile file) {
        ImageUploadResponse response = adminService.uploadImage(file);
        return Result.success(response);
    }
}
