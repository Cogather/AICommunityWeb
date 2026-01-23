package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.AdminService;
import com.aicommunity.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 管理后台控制器
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@Api(tags = "管理后台接口")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ========== 首页管理 ==========

    /**
     * 获取轮播图配置
     *
     * @return 轮播图列表
     */
    @GetMapping("/carousel")
    @ApiOperation(value = "获取轮播图配置", notes = "获取首页轮播图配置列表")
    public Result<AdminCarouselListVO> getCarousel() {
        log.info("获取轮播图配置");
        AdminCarouselListVO result = adminService.getCarouselConfig();
        return Result.success(result);
    }

    /**
     * 保存轮播图配置
     *
     * @param request 轮播图列表
     * @return 保存结果
     */
    @PutMapping("/carousel")
    @ApiOperation(value = "保存轮播图配置", notes = "保存首页轮播图配置")
    public Result<Void> saveCarousel(@ApiParam(value = "轮播图列表", required = true)
                                      @Valid @RequestBody AdminCarouselListVO request) {
        log.info("保存轮播图配置, list size={}", request.getList() != null ? request.getList().size() : 0);
        adminService.saveCarouselConfig(request.getList());
        return Result.success();
    }

    /**
     * 获取荣誉殿堂Banner配置
     *
     * @return Banner配置
     */
    @GetMapping("/honor/banner")
    @ApiOperation(value = "获取荣誉殿堂Banner配置", notes = "获取荣誉殿堂Banner图片配置")
    public Result<AdminHonorBannerVO> getHonorBanner() {
        log.info("获取荣誉殿堂Banner配置");
        AdminHonorBannerVO result = adminService.getHonorBannerConfig();
        return Result.success(result);
    }

    /**
     * 保存荣誉殿堂Banner配置
     *
     * @param request Banner配置
     * @return 保存结果
     */
    @PutMapping("/honor/banner")
    @ApiOperation(value = "保存荣誉殿堂Banner配置", notes = "保存荣誉殿堂Banner图片配置")
    public Result<Void> saveHonorBanner(@ApiParam(value = "Banner配置", required = true)
                                         @Valid @RequestBody AdminHonorBannerVO request) {
        log.info("保存荣誉殿堂Banner配置, bannerImage={}", request.getBannerImage());
        adminService.saveHonorBannerConfig(request.getBannerImage());
        return Result.success();
    }

    /**
     * 获取AI工具配置
     *
     * @return 工具列表
     */
    @GetMapping("/tools")
    @ApiOperation(value = "获取AI工具配置", notes = "获取首页AI工具配置列表")
    public Result<AdminToolListVO> getTools() {
        log.info("获取AI工具配置");
        AdminToolListVO result = adminService.getToolsConfig();
        return Result.success(result);
    }

    /**
     * 保存AI工具配置
     *
     * @param request 工具列表
     * @return 保存结果
     */
    @PutMapping("/tools")
    @ApiOperation(value = "保存AI工具配置", notes = "保存首页AI工具配置")
    public Result<Void> saveTools(@ApiParam(value = "工具列表", required = true)
                                   @Valid @RequestBody AdminToolListVO request) {
        log.info("保存AI工具配置, list size={}", request.getList() != null ? request.getList().size() : 0);
        adminService.saveToolsConfig(request.getList());
        return Result.success();
    }

    /**
     * 获取AI工具专区Banner配置
     *
     * @return Banner列表
     */
    @GetMapping("/tool-banners")
    @ApiOperation(value = "获取AI工具专区Banner配置", notes = "获取AI工具专区Banner配置列表")
    public Result<AdminToolBannerListVO> getToolBanners() {
        log.info("获取AI工具专区Banner配置");
        AdminToolBannerListVO result = adminService.getToolBannersConfig();
        return Result.success(result);
    }

    /**
     * 保存AI工具专区Banner配置
     *
     * @param request Banner列表
     * @return 保存结果
     */
    @PutMapping("/tool-banners")
    @ApiOperation(value = "保存AI工具专区Banner配置", notes = "保存AI工具专区Banner配置")
    public Result<Void> saveToolBanners(@ApiParam(value = "Banner列表", required = true)
                                        @Valid @RequestBody AdminToolBannerListVO request) {
        log.info("保存AI工具专区Banner配置, list size={}", request.getList() != null ? request.getList().size() : 0);
        adminService.saveToolBannersConfig(request.getList());
        return Result.success();
    }

    // ========== 精华/置顶帖子管理 ==========

    /**
     * 获取所有精华/置顶帖子列表
     *
     * @return 精华/置顶帖子列表（按区域分组）
     */
    @GetMapping("/featured-posts/all")
    @ApiOperation(value = "获取所有精华/置顶帖子列表", notes = "按区域分组返回所有精华/置顶帖子")
    public Result<AdminFeaturedPostListVO> getAllFeaturedPosts() {
        log.info("获取所有精华/置顶帖子列表");
        AdminFeaturedPostListVO result = adminService.getAllFeaturedPosts();
        return Result.success(result);
    }

    /**
     * 移除帖子精华/置顶状态
     *
     * @param postId 帖子ID（路径参数）
     * @return 移除结果
     */
    @DeleteMapping("/featured-posts/{postId}")
    @ApiOperation(value = "移除帖子精华/置顶状态", notes = "取消帖子的精华/置顶状态")
    public Result<Void> removeFeaturedPost(@ApiParam(value = "帖子ID", required = true, example = "1")
                                           @PathVariable String postId) {
        log.info("移除帖子精华/置顶状态, postId={}", postId);
        adminService.removeFeaturedPost(postId);
        return Result.success();
    }

    /**
     * 获取赋能交流精选合集配置
     *
     * @return 精选合集列表
     */
    @GetMapping("/empowerment/collections")
    @ApiOperation(value = "获取赋能交流精选合集配置", notes = "获取赋能交流精选合集配置列表")
    public Result<AdminCollectionListVO> getEmpowermentCollections() {
        log.info("获取赋能交流精选合集配置");
        AdminCollectionListVO result = adminService.getEmpowermentCollections();
        return Result.success(result);
    }

    /**
     * 保存赋能交流精选合集配置
     *
     * @param request 精选合集列表
     * @return 保存结果
     */
    @PutMapping("/empowerment/collections")
    @ApiOperation(value = "保存赋能交流精选合集配置", notes = "保存赋能交流精选合集配置")
    public Result<Void> saveEmpowermentCollections(@ApiParam(value = "精选合集列表", required = true)
                                                    @Valid @RequestBody AdminCollectionListVO request) {
        log.info("保存赋能交流精选合集配置, list size={}", request.getList() != null ? request.getList().size() : 0);
        adminService.saveEmpowermentCollections(request.getList());
        return Result.success();
    }

    // ========== AI使用达人管理 ==========

    /**
     * 获取奖项设置列表
     *
     * @return 奖项列表
     */
    @GetMapping("/awards")
    @ApiOperation(value = "获取奖项设置列表", notes = "获取个人奖项设置列表，包含名称、描述、评选标准和评选周期")
    public Result<AdminAwardListVO> getAwards() {
        log.info("获取奖项设置列表");
        AdminAwardListVO result = adminService.getAwards();
        return Result.success(result);
    }

    /**
     * 保存奖项设置列表
     *
     * @param request 奖项列表
     * @return 保存结果
     */
    @PutMapping("/awards")
    @ApiOperation(value = "保存奖项设置列表", notes = "保存个人奖项设置列表")
    public Result<Void> saveAwards(@ApiParam(value = "奖项列表", required = true)
                                   @Valid @RequestBody AdminAwardListVO request) {
        log.info("保存奖项设置列表, list size={}", request.getList() != null ? request.getList().size() : 0);
        adminService.saveAwards(request.getList());
        return Result.success();
    }

    /**
     * 获取奖项名称列表（下拉选择用）
     *
     * @return 奖项名称列表
     */
    @GetMapping("/awards/names")
    @ApiOperation(value = "获取奖项名称列表", notes = "获取奖项名称列表，用于获奖者管理页面的下拉选择")
    public Result<AdminAwardNameListVO> getAwardNames() {
        log.info("获取奖项名称列表");
        AdminAwardNameListVO result = adminService.getAwardNames();
        return Result.success(result);
    }

    /**
     * 获取获奖者列表
     *
     * @return 获奖者列表
     */
    @GetMapping("/winners")
    @ApiOperation(value = "获取获奖者列表", notes = "获取个人获奖者列表")
    public Result<AdminWinnerListVO> getWinners() {
        log.info("获取获奖者列表");
        AdminWinnerListVO result = adminService.getWinners();
        return Result.success(result);
    }

    /**
     * 保存获奖者列表
     *
     * @param request 获奖者列表
     * @return 保存结果
     */
    @PutMapping("/winners")
    @ApiOperation(value = "保存获奖者列表", notes = "保存个人获奖者列表")
    public Result<Void> saveWinners(@ApiParam(value = "获奖者列表", required = true)
                                   @Valid @RequestBody AdminWinnerListVO request) {
        log.info("保存获奖者列表, list size={}", request.getList() != null ? request.getList().size() : 0);
        adminService.saveWinners(request.getList());
        return Result.success();
    }

    /**
     * 获取获奖者推荐列表
     *
     * @param month 月份，格式YYYY-MM，默认当前月
     * @return 推荐获奖者列表
     */
    @GetMapping("/winners/recommended")
    @ApiOperation(value = "获取获奖者推荐列表", notes = "根据积分自动推荐Top用户")
    public Result<AdminRecommendedWinnerListVO> getRecommendedWinners(
            @ApiParam(value = "月份，格式YYYY-MM，默认当前月", example = "2024-01")
            @RequestParam(required = false) String month) {
        log.info("获取获奖者推荐列表, month={}", month);
        AdminRecommendedWinnerListVO result = adminService.getRecommendedWinners(month);
        return Result.success(result);
    }

    /**
     * 设置用户获奖
     *
     * @param request 设置获奖请求
     * @return 设置结果（包含荣誉ID）
     */
    @PostMapping("/winners/set-award")
    @ApiOperation(value = "设置用户获奖", notes = "设置用户获得指定奖项")
    public Result<AdminSetAwardResponseVO> setUserAward(@ApiParam(value = "设置获奖请求", required = true)
                                                         @Valid @RequestBody AdminSetAwardRequestVO request) {
        log.info("设置用户获奖, userId={}, awardId={}, awardDate={}", 
                request.getUserId(), request.getAwardId(), request.getAwardDate());
        AdminSetAwardResponseVO result = adminService.setUserAward(request);
        return Result.success(result);
    }

    /**
     * 取消用户获奖
     *
     * @param request 取消获奖请求
     * @return 取消结果
     */
    @DeleteMapping("/winners/cancel-award")
    @ApiOperation(value = "取消用户获奖", notes = "取消用户的获奖记录")
    public Result<Void> cancelUserAward(@ApiParam(value = "取消获奖请求", required = true)
                                        @Valid @RequestBody AdminCancelAwardRequestVO request) {
        log.info("取消用户获奖, userId={}, honorId={}", request.getUserId(), request.getHonorId());
        adminService.cancelUserAward(request);
        return Result.success();
    }

    /**
     * 获取团队奖项列表
     *
     * @return 团队奖项列表
     */
    @GetMapping("/team-awards")
    @ApiOperation(value = "获取团队奖项列表", notes = "获取团队奖项列表")
    public Result<AdminTeamAwardListVO> getTeamAwards() {
        log.info("获取团队奖项列表");
        AdminTeamAwardListVO result = adminService.getTeamAwards();
        return Result.success(result);
    }

    /**
     * 保存团队奖项列表
     *
     * @param request 团队奖项列表
     * @return 保存结果
     */
    @PutMapping("/team-awards")
    @ApiOperation(value = "保存团队奖项列表", notes = "保存团队奖项列表")
    public Result<Void> saveTeamAwards(@ApiParam(value = "团队奖项列表", required = true)
                                        @Valid @RequestBody AdminTeamAwardListVO request) {
        log.info("保存团队奖项列表, list size={}", request.getList() != null ? request.getList().size() : 0);
        adminService.saveTeamAwards(request.getList());
        return Result.success();
    }

    // ========== 人员管理 ==========

    /**
     * 获取用户列表
     *
     * @param keyword 搜索关键词（姓名、邮箱）
     * @param role    角色筛选：admin/tool_owner
     * @return 用户列表
     */
    @GetMapping("/users")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表，支持关键词搜索和角色筛选")
    public Result<AdminUserListVO> getUsers(
            @ApiParam(value = "搜索关键词（姓名、邮箱）", example = "张三")
            @RequestParam(required = false) String keyword,
            @ApiParam(value = "角色筛选：admin/tool_owner", example = "admin")
            @RequestParam(required = false) String role) {
        log.info("获取用户列表, keyword={}, role={}", keyword, role);
        AdminUserListVO result = adminService.getUsers(keyword, role);
        return Result.success(result);
    }

    /**
     * 根据用户ID查找用户
     *
     * @param userId 用户ID（路径参数）
     * @return 用户信息
     */
    @GetMapping("/users/{userId}")
    @ApiOperation(value = "根据用户ID查找用户", notes = "根据用户ID查找用户详细信息")
    public Result<AdminUserItemVO> getUserById(@ApiParam(value = "用户ID", required = true, example = "1")
                                               @PathVariable Integer userId) {
        log.info("根据用户ID查找用户, userId={}", userId);
        AdminUserItemVO result = adminService.getUserById(userId);
        return Result.success(result);
    }

    /**
     * 添加管理员/工具Owner
     *
     * @param request 用户角色请求
     * @return 设置结果
     */
    @PostMapping("/users/role")
    @ApiOperation(value = "添加管理员/工具Owner", notes = "为用户添加管理员或工具Owner角色")
    public Result<Void> addUserRole(@ApiParam(value = "用户角色请求", required = true)
                                    @Valid @RequestBody AdminUserRoleRequestVO request) {
        log.info("添加用户角色, userId={}, role={}", request.getUserId(), request.getRole());
        adminService.addUserRole(request);
        return Result.success();
    }

    /**
     * 移除管理员/工具Owner
     *
     * @param request 用户角色请求
     * @return 移除结果
     */
    @DeleteMapping("/users/role")
    @ApiOperation(value = "移除管理员/工具Owner", notes = "移除用户的管理员或工具Owner角色")
    public Result<Void> removeUserRole(@ApiParam(value = "用户角色请求", required = true)
                                       @Valid @RequestBody AdminUserRoleRequestVO request) {
        log.info("移除用户角色, userId={}, role={}", request.getUserId(), request.getRole());
        adminService.removeUserRole(request);
        return Result.success();
    }

    // ========== 图片上传 ==========

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 图片URL
     */
    @PostMapping("/upload/image")
    @ApiOperation(value = "上传图片", notes = "上传图片文件，返回图片URL")
    public Result<AdminUploadImageResponseVO> uploadImage(@ApiParam(value = "图片文件", required = true)
                                                          @RequestParam("file") MultipartFile file) {
        log.info("上传图片, fileName={}, fileSize={}", file.getOriginalFilename(), file.getSize());
        try {
            String url = adminService.uploadImage(file.getBytes(), file.getOriginalFilename());
            AdminUploadImageResponseVO response = new AdminUploadImageResponseVO();
            response.setUrl(url);
            return Result.success(response);
        } catch (Exception e) {
            log.error("上传图片失败", e);
            return Result.error("上传图片失败：" + e.getMessage());
        }
    }
}
