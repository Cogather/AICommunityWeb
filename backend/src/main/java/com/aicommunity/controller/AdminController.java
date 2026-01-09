package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 管理后台控制器
 *
 * @author AI Community Team
 */
@Api(tags = "管理后台接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ==================== 首页配置管理 ====================

    /**
     * 获取首页轮播图配置
     */
    @ApiOperation(value = "获取首页轮播图配置", notes = "获取当前配置的轮播图列表")
    @GetMapping("/home/carousel")
    public Result<?> getCarousel() {
        return Result.success(adminService.getCarousel());
    }

    /**
     * 保存首页轮播图配置
     */
    @ApiOperation(value = "保存首页轮播图配置", notes = "保存轮播图配置")
    @PutMapping("/home/carousel")
    public Result<?> saveCarousel(@RequestBody SaveCarouselRequest request) {
        adminService.saveCarousel(request);
        return Result.success();
    }

    /**
     * 获取荣誉殿堂Banner配置
     */
    @ApiOperation(value = "获取荣誉殿堂Banner配置", notes = "获取荣誉殿堂Banner配置")
    @GetMapping("/home/honor-banner")
    public Result<?> getHonorBanner() {
        return Result.success(adminService.getHonorBanner());
    }

    /**
     * 保存荣誉殿堂Banner配置
     */
    @ApiOperation(value = "保存荣誉殿堂Banner配置", notes = "保存荣誉殿堂Banner配置")
    @PutMapping("/home/honor-banner")
    public Result<?> saveHonorBanner(@RequestBody SaveHonorBannerRequest request) {
        adminService.saveHonorBanner(request);
        return Result.success();
    }

    /**
     * 获取荣誉殿堂奖项列表配置
     */
    @ApiOperation(value = "获取荣誉殿堂奖项列表配置", notes = "获取荣誉殿堂奖项列表配置")
    @GetMapping("/home/honor-awards")
    public Result<?> getHonorAwards() {
        return Result.success(adminService.getHonorAwards());
    }

    /**
     * 保存荣誉殿堂奖项列表配置
     */
    @ApiOperation(value = "保存荣誉殿堂奖项列表配置", notes = "保存荣誉殿堂奖项列表配置")
    @PutMapping("/home/honor-awards")
    public Result<?> saveHonorAwards(@RequestBody SaveHonorAwardsRequest request) {
        adminService.saveHonorAwards(request);
        return Result.success();
    }

    /**
     * 获取社区头条配置
     */
    @ApiOperation(value = "获取社区头条配置", notes = "获取社区头条配置")
    @GetMapping("/home/news")
    public Result<?> getNews() {
        return Result.success(adminService.getNews());
    }

    /**
     * 保存社区头条配置
     */
    @ApiOperation(value = "保存社区头条配置", notes = "保存社区头条配置")
    @PutMapping("/home/news")
    public Result<?> saveNews(@RequestBody SaveNewsRequest request) {
        adminService.saveNews(request);
        return Result.success();
    }

    // ==================== 工具配置管理 ====================

    /**
     * 获取AI工具配置
     */
    @ApiOperation(value = "获取AI工具配置", notes = "获取AI工具配置")
    @GetMapping("/tools")
    public Result<?> getTools() {
        return Result.success(adminService.getTools());
    }

    /**
     * 保存AI工具配置
     */
    @ApiOperation(value = "保存AI工具配置", notes = "保存AI工具配置")
    @PutMapping("/tools")
    public Result<?> saveTools(@RequestBody SaveToolsRequest request) {
        adminService.saveTools(request);
        return Result.success();
    }

    // ==================== Agent配置管理 ====================

    /**
     * 获取扶摇Agent应用置顶帖子配置
     */
    @ApiOperation(value = "获取扶摇Agent应用置顶帖子配置", notes = "获取扶摇Agent应用置顶帖子配置")
    @GetMapping("/agent/featured-post")
    public Result<?> getFeaturedPost() {
        return Result.success(adminService.getFeaturedPost());
    }

    /**
     * 保存扶摇Agent应用置顶帖子配置
     */
    @ApiOperation(value = "保存扶摇Agent应用置顶帖子配置", notes = "保存扶摇Agent应用置顶帖子配置")
    @PutMapping("/agent/featured-post")
    public Result<?> saveFeaturedPost(@RequestBody SaveFeaturedPostRequest request) {
        adminService.saveFeaturedPost(request);
        return Result.success();
    }

    // ==================== 推荐封面配置管理 ====================

    /**
     * 获取推荐封面配置
     */
    @ApiOperation(value = "获取推荐封面配置", notes = "获取推荐封面配置")
    @GetMapping("/posts/recommended-covers")
    public Result<?> getRecommendedCovers() {
        return Result.success(adminService.getRecommendedCovers());
    }

    /**
     * 保存推荐封面配置
     */
    @ApiOperation(value = "保存推荐封面配置", notes = "保存推荐封面配置")
    @PutMapping("/posts/recommended-covers")
    public Result<?> saveRecommendedCovers(@RequestBody SaveRecommendedCoversRequest request) {
        adminService.saveRecommendedCovers(request);
        return Result.success();
    }

    // ==================== 用户管理 ====================

    /**
     * 搜索用户
     */
    @ApiOperation(value = "搜索用户", notes = "通过工号、姓名或邮箱搜索用户")
    @GetMapping("/users/search")
    public Result<?> searchUsers(
            @ApiParam(value = "工号") @RequestParam(required = false) String employeeId,
            @ApiParam(value = "姓名") @RequestParam(required = false) String name,
            @ApiParam(value = "邮箱") @RequestParam(required = false) String email,
            @ApiParam(value = "角色") @RequestParam(required = false) String role) {
        return Result.success(adminService.searchUsers(employeeId, name, email, role));
    }

    /**
     * 获取用户列表
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表，支持按角色筛选")
    @GetMapping("/users")
    public Result<PageResult<?>> getUsers(
            @ApiParam(value = "角色") @RequestParam(required = false) String role,
            @ApiParam(value = "工具ID") @RequestParam(required = false) Long toolId,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String search,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<?> result = adminService.getUsers(role, toolId, search, pageQuery);
        return Result.success(result);
    }

    /**
     * 添加用户角色
     */
    @ApiOperation(value = "添加用户角色", notes = "添加管理员或工具Owner角色")
    @PostMapping("/users/{userId}/role")
    public Result<?> addUserRole(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @RequestBody AddUserRoleRequest request) {
        adminService.addUserRole(userId, request);
        return Result.success();
    }

    /**
     * 移除用户角色
     */
    @ApiOperation(value = "移除用户角色", notes = "移除用户的指定角色")
    @DeleteMapping("/users/{userId}/role")
    public Result<?> removeUserRole(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @RequestBody RemoveUserRoleRequest request) {
        adminService.removeUserRole(userId, request);
        return Result.success();
    }

    // ==================== 获奖者推荐 ====================

    /**
     * 获取本月积分排行榜
     */
    @ApiOperation(value = "获取本月积分排行榜", notes = "获取本月积分靠前的用户，用于管理员推荐评奖")
    @GetMapping("/honors/recommended-winners")
    public Result<?> getRecommendedWinners(
            @ApiParam(value = "月份") @RequestParam(required = false) String month,
            @ApiParam(value = "返回数量") @RequestParam(defaultValue = "3") Integer limit) {
        return Result.success(adminService.getRecommendedWinners(month, limit));
    }

    /**
     * 设置用户获奖
     */
    @ApiOperation(value = "设置用户获奖", notes = "为推荐的用户设置获奖记录")
    @PostMapping("/honors")
    public Result<?> createHonor(@Valid @RequestBody CreateHonorRequest request) {
        return Result.success(adminService.createHonor(request));
    }

    /**
     * 取消用户获奖
     */
    @ApiOperation(value = "取消用户获奖", notes = "删除已设置的获奖记录")
    @DeleteMapping("/honors/{id}")
    public Result<?> deleteHonor(
            @ApiParam(value = "荣誉ID", required = true) @PathVariable Long id) {
        adminService.deleteHonor(id);
        return Result.success();
    }

    // ==================== 文件上传 ====================

    /**
     * 上传图片
     */
    @ApiOperation(value = "上传图片", notes = "上传图片文件")
    @PostMapping("/upload/image")
    public Result<?> uploadImage(@RequestParam("file") MultipartFile file) {
        return Result.success(adminService.uploadImage(file));
    }

    // ==================== 请求DTO类 ====================

    public static class SaveCarouselRequest {
        private List<CarouselItem> list;

        public List<CarouselItem> getList() { return list; }
        public void setList(List<CarouselItem> list) { this.list = list; }

        public static class CarouselItem {
            private Long id;
            private String image;
            private String title;
            private String desc;
            private String link;
            private Boolean showContent;
            private Integer order;

            // Getters and Setters
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            public String getImage() { return image; }
            public void setImage(String image) { this.image = image; }
            public String getTitle() { return title; }
            public void setTitle(String title) { this.title = title; }
            public String getDesc() { return desc; }
            public void setDesc(String desc) { this.desc = desc; }
            public String getLink() { return link; }
            public void setLink(String link) { this.link = link; }
            public Boolean getShowContent() { return showContent; }
            public void setShowContent(Boolean showContent) { this.showContent = showContent; }
            public Integer getOrder() { return order; }
            public void setOrder(Integer order) { this.order = order; }
        }
    }

    public static class SaveHonorBannerRequest {
        private String bannerImage;

        public String getBannerImage() { return bannerImage; }
        public void setBannerImage(String bannerImage) { this.bannerImage = bannerImage; }
    }

    public static class SaveHonorAwardsRequest {
        private List<AwardItem> list;

        public List<AwardItem> getList() { return list; }
        public void setList(List<AwardItem> list) { this.list = list; }

        public static class AwardItem {
            private Long id;
            private String name;
            private String desc;
            private String image;
            private Integer order;

            // Getters and Setters
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            public String getDesc() { return desc; }
            public void setDesc(String desc) { this.desc = desc; }
            public String getImage() { return image; }
            public void setImage(String image) { this.image = image; }
            public Integer getOrder() { return order; }
            public void setOrder(Integer order) { this.order = order; }
        }
    }

    public static class SaveNewsRequest {
        private List<NewsItem> list;

        public List<NewsItem> getList() { return list; }
        public void setList(List<NewsItem> list) { this.list = list; }

        public static class NewsItem {
            private Long id;
            private String title;
            private String image;
            private String date;
            private String link;
            private Integer order;

            // Getters and Setters
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            public String getTitle() { return title; }
            public void setTitle(String title) { this.title = title; }
            public String getImage() { return image; }
            public void setImage(String image) { this.image = image; }
            public String getDate() { return date; }
            public void setDate(String date) { this.date = date; }
            public String getLink() { return link; }
            public void setLink(String link) { this.link = link; }
            public Integer getOrder() { return order; }
            public void setOrder(Integer order) { this.order = order; }
        }
    }

    public static class SaveToolsRequest {
        private List<ToolItem> list;

        public List<ToolItem> getList() { return list; }
        public void setList(List<ToolItem> list) { this.list = list; }

        public static class ToolItem {
            private Long id;
            private String name;
            private String desc;
            private String logo;
            private String color;
            private String link;
            private List<BannerItem> banners;

            // Getters and Setters
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            public String getDesc() { return desc; }
            public void setDesc(String desc) { this.desc = desc; }
            public String getLogo() { return logo; }
            public void setLogo(String logo) { this.logo = logo; }
            public String getColor() { return color; }
            public void setColor(String color) { this.color = color; }
            public String getLink() { return link; }
            public void setLink(String link) { this.link = link; }
            public List<BannerItem> getBanners() { return banners; }
            public void setBanners(List<BannerItem> banners) { this.banners = banners; }

            public static class BannerItem {
                private Long id;
                private String image;
                private String title;
                private String desc;
                private Integer order;

                // Getters and Setters
                public Long getId() { return id; }
                public void setId(Long id) { this.id = id; }
                public String getImage() { return image; }
                public void setImage(String image) { this.image = image; }
                public String getTitle() { return title; }
                public void setTitle(String title) { this.title = title; }
                public String getDesc() { return desc; }
                public void setDesc(String desc) { this.desc = desc; }
                public Integer getOrder() { return order; }
                public void setOrder(Integer order) { this.order = order; }
            }
        }
    }

    public static class SaveFeaturedPostRequest {
        private Long id;
        private String title;
        private String description;
        private String author;
        private String authorAvatar;
        private String createTime;
        private Integer views;
        private Integer comments;
        private Integer likes;
        private List<String> tags;
        private String cover;
        private String link;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getAuthorAvatar() { return authorAvatar; }
        public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
        public Integer getViews() { return views; }
        public void setViews(Integer views) { this.views = views; }
        public Integer getComments() { return comments; }
        public void setComments(Integer comments) { this.comments = comments; }
        public Integer getLikes() { return likes; }
        public void setLikes(Integer likes) { this.likes = likes; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public String getCover() { return cover; }
        public void setCover(String cover) { this.cover = cover; }
        public String getLink() { return link; }
        public void setLink(String link) { this.link = link; }
    }

    public static class SaveRecommendedCoversRequest {
        private List<CoverItem> list;

        public List<CoverItem> getList() { return list; }
        public void setList(List<CoverItem> list) { this.list = list; }

        public static class CoverItem {
            private Long id;
            private String url;
            private String thumbnail;
            private Integer order;

            // Getters and Setters
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            public String getUrl() { return url; }
            public void setUrl(String url) { this.url = url; }
            public String getThumbnail() { return thumbnail; }
            public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
            public Integer getOrder() { return order; }
            public void setOrder(Integer order) { this.order = order; }
        }
    }

    public static class AddUserRoleRequest {
        private String role;
        private Long toolId;
        private String employeeId;

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getToolId() { return toolId; }
        public void setToolId(Long toolId) { this.toolId = toolId; }
        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    }

    public static class RemoveUserRoleRequest {
        private String role;
        private Long toolId;

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getToolId() { return toolId; }
        public void setToolId(Long toolId) { this.toolId = toolId; }
    }

    public static class CreateHonorRequest {
        private Long userId;
        private Long awardId;
        private String awardName;
        private String awardDate; // YYYY-MM
        private String category;
        private String year; // YYYY

        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getAwardId() { return awardId; }
        public void setAwardId(Long awardId) { this.awardId = awardId; }
        public String getAwardName() { return awardName; }
        public void setAwardName(String awardName) { this.awardName = awardName; }
        public String getAwardDate() { return awardDate; }
        public void setAwardDate(String awardDate) { this.awardDate = awardDate; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
    }
}
