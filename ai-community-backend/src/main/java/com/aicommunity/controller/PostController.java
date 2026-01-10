package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.PostDetailDTO;
import com.aicommunity.dto.PostListDTO;
import com.aicommunity.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 帖子控制器
 *
 * @author AI Community Team
 */
@Api(tags = "帖子相关接口")
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 获取帖子列表
     *
     * @param zone       专区
     * @param toolId     工具ID
     * @param tag        标签
     * @param department 部门
     * @param author     作者
     * @param sort       排序方式
     * @param search     搜索关键词
     * @param page       页码
     * @param pageSize   每页数量
     * @param isFeatured 是否精选
     * @return 帖子列表
     */
    @ApiOperation(value = "获取帖子列表", notes = "支持搜索、排序、筛选功能的帖子列表接口")
    @GetMapping
    public Result<PageResult<PostListDTO>> getPosts(
            @ApiParam(value = "专区", required = true) @RequestParam String zone,
            @ApiParam(value = "工具ID") @RequestParam(required = false) Long toolId,
            @ApiParam(value = "标签") @RequestParam(required = false) String tag,
            @ApiParam(value = "部门") @RequestParam(required = false) String department,
            @ApiParam(value = "作者") @RequestParam(required = false) String author,
            @ApiParam(value = "排序方式") @RequestParam(defaultValue = "newest") String sort,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String search,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "是否精选") @RequestParam(required = false) Boolean isFeatured) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<PostListDTO> result = postService.getPosts(zone, toolId, tag, department, author, sort, search, isFeatured, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取帖子详情
     *
     * @param id 帖子ID
     * @return 帖子详情
     */
    @ApiOperation(value = "获取帖子详情", notes = "获取指定帖子的详细信息")
    @GetMapping("/{id}")
    public Result<PostDetailDTO> getPostDetail(
            @ApiParam(value = "帖子ID", required = true) @PathVariable Long id) {
        PostDetailDTO post = postService.getPostDetail(id);
        return Result.success(post);
    }

    /**
     * 创建帖子
     *
     * @param request 创建请求
     * @return 创建结果
     */
    @ApiOperation(value = "创建帖子", notes = "发布新帖子")
    @PostMapping
    public Result<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request);
        return Result.success(response);
    }

    /**
     * 更新帖子
     *
     * @param id      帖子ID
     * @param request 更新请求
     * @return 更新结果
     */
    @ApiOperation(value = "更新帖子", notes = "更新帖子内容")
    @PutMapping("/{id}")
    public Result<UpdatePostResponse> updatePost(
            @ApiParam(value = "帖子ID", required = true) @PathVariable Long id,
            @Valid @RequestBody CreatePostRequest request) {
        UpdatePostResponse response = postService.updatePost(id, request);
        return Result.success(response);
    }

    /**
     * 删除帖子
     *
     * @param id 帖子ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除帖子", notes = "删除帖子内容")
    @DeleteMapping("/{id}")
    public Result<?> deletePost(
            @ApiParam(value = "帖子ID", required = true) @PathVariable Long id) {
        postService.deletePost(id);
        return Result.success();
    }

    /**
     * 点赞/取消点赞帖子
     *
     * @param id     帖子ID
     * @param request 点赞请求
     * @return 点赞结果
     */
    @ApiOperation(value = "点赞/取消点赞帖子", notes = "更新帖子的点赞状态")
    @PostMapping("/{id}/like")
    public Result<LikePostResponse> likePost(
            @ApiParam(value = "帖子ID", required = true) @PathVariable Long id,
            @RequestBody LikePostRequest request) {
        LikePostResponse response = postService.likePost(id, request);
        return Result.success(response);
    }

    /**
     * 收藏/取消收藏帖子
     *
     * @param id      帖子ID
     * @param request 收藏请求
     * @return 收藏结果
     */
    @ApiOperation(value = "收藏/取消收藏帖子", notes = "更新帖子的收藏状态")
    @PostMapping("/{id}/collect")
    public Result<CollectPostResponse> collectPost(
            @ApiParam(value = "帖子ID", required = true) @PathVariable Long id,
            @RequestBody CollectPostRequest request) {
        CollectPostResponse response = postService.collectPost(id, request);
        return Result.success(response);
    }

    /**
     * 获取推荐封面列表
     *
     * @return 推荐封面列表
     */
    @ApiOperation(value = "获取推荐封面列表", notes = "获取发帖页面的推荐封面图片列表")
    @GetMapping("/recommended-covers")
    public Result<?> getRecommendedCovers() {
        return Result.success(postService.getRecommendedCovers());
    }

    /**
     * 保存草稿
     *
     * @param request 草稿请求
     * @return 保存结果
     */
    @ApiOperation(value = "保存草稿", notes = "保存帖子草稿")
    @PostMapping("/draft")
    public Result<SaveDraftResponse> saveDraft(@RequestBody SaveDraftRequest request) {
        SaveDraftResponse response = postService.saveDraft(request);
        return Result.success(response);
    }

    /**
     * 获取最热帖子
     *
     * @param zone  专区
     * @param limit 返回数量
     * @return 帖子列表
     */
    @ApiOperation(value = "获取最热帖子", notes = "获取指定专区的最热门帖子列表")
    @GetMapping("/hot")
    public Result<?> getHotPosts(
            @ApiParam(value = "专区") @RequestParam(required = false) String zone,
            @ApiParam(value = "返回数量") @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(postService.getHotPosts(zone, limit));
    }

    /**
     * 创建帖子请求DTO
     */
    public static class CreatePostRequest {
        private String title;
        private String summary;
        private String content;
        private java.util.List<String> tags;
        private String cover;
        private String zone;
        private Long toolId;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public java.util.List<String> getTags() { return tags; }
        public void setTags(java.util.List<String> tags) { this.tags = tags; }
        public String getCover() { return cover; }
        public void setCover(String cover) { this.cover = cover; }
        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
        public Long getToolId() { return toolId; }
        public void setToolId(Long toolId) { this.toolId = toolId; }
    }

    /**
     * 创建帖子响应DTO
     */
    public static class CreatePostResponse {
        private Long id;
        private String message;

        public CreatePostResponse(Long id, String message) {
            this.id = id;
            this.message = message;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * 更新帖子响应DTO
     */
    public static class UpdatePostResponse {
        private Long id;
        private String message;

        public UpdatePostResponse(Long id, String message) {
            this.id = id;
            this.message = message;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * 点赞帖子请求DTO
     */
    public static class LikePostRequest {
        private String action; // "like" | "unlike"

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }

    /**
     * 点赞帖子响应DTO
     */
    public static class LikePostResponse {
        private Boolean liked;
        private Integer likes;

        public LikePostResponse(Boolean liked, Integer likes) {
            this.liked = liked;
            this.likes = likes;
        }

        public Boolean getLiked() { return liked; }
        public void setLiked(Boolean liked) { this.liked = liked; }
        public Integer getLikes() { return likes; }
        public void setLikes(Integer likes) { this.likes = likes; }
    }

    /**
     * 收藏帖子请求DTO
     */
    public static class CollectPostRequest {
        private String action; // "collect" | "uncollect"

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }

    /**
     * 收藏帖子响应DTO
     */
    public static class CollectPostResponse {
        private Boolean collected;

        public CollectPostResponse(Boolean collected) {
            this.collected = collected;
        }

        public Boolean getCollected() { return collected; }
        public void setCollected(Boolean collected) { this.collected = collected; }
    }

    /**
     * 保存草稿请求DTO
     */
    public static class SaveDraftRequest {
        private String title;
        private String summary;
        private String content;
        private java.util.List<String> tags;
        private String cover;
        private String zone;
        private Long toolId;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public java.util.List<String> getTags() { return tags; }
        public void setTags(java.util.List<String> tags) { this.tags = tags; }
        public String getCover() { return cover; }
        public void setCover(String cover) { this.cover = cover; }
        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
        public Long getToolId() { return toolId; }
        public void setToolId(Long toolId) { this.toolId = toolId; }
    }

    /**
     * 保存草稿响应DTO
     */
    public static class SaveDraftResponse {
        private String draftId;
        private String savedAt;
        private String message;

        public SaveDraftResponse(String draftId, String savedAt, String message) {
            this.draftId = draftId;
            this.savedAt = savedAt;
            this.message = message;
        }

        public String getDraftId() { return draftId; }
        public void setDraftId(String draftId) { this.draftId = draftId; }
        public String getSavedAt() { return savedAt; }
        public void setSavedAt(String savedAt) { this.savedAt = savedAt; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
