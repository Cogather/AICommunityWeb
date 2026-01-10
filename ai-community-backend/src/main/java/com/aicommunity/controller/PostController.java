package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.PostCreateRequest;
import com.aicommunity.dto.PostUpdateRequest;
import com.aicommunity.entity.Post;
import com.aicommunity.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 帖子控制器
 *
 * @author AI Community Team
 */
@Api(tags = "帖子管理")
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 获取帖子列表
     *
     * @param zone 专区
     * @param toolId 工具ID
     * @param tag 标签
     * @param department 部门
     * @param author 作者
     * @param sort 排序方式
     * @param search 搜索关键词
     * @param pageQuery 分页参数
     * @return 帖子列表
     */
    @ApiOperation(value = "获取帖子列表", notes = "支持搜索、排序、筛选功能的帖子列表接口")
    @GetMapping
    public Result<PageResult<Post>> getPosts(
            @ApiParam(value = "专区", example = "practices")
            @RequestParam(required = false) String zone,
            @ApiParam(value = "工具ID")
            @RequestParam(required = false) Long toolId,
            @ApiParam(value = "标签")
            @RequestParam(required = false) String tag,
            @ApiParam(value = "部门")
            @RequestParam(required = false) String department,
            @ApiParam(value = "作者")
            @RequestParam(required = false) String author,
            @ApiParam(value = "排序方式：newest-最新，hot-最热，comments-评论数，likes-点赞数")
            @RequestParam(required = false, defaultValue = "newest") String sort,
            @ApiParam(value = "搜索关键词")
            @RequestParam(required = false) String search,
            PageQuery pageQuery) {
        PageResult<Post> result = postService.getPosts(zone, toolId, tag, department, author, sort, search, pageQuery);
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
    public Result<Post> getPostDetail(
            @ApiParam(value = "帖子ID", required = true)
            @PathVariable Long id) {
        Post post = postService.getPostDetail(id);
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
    public Result<Post> createPost(
            @ApiParam(value = "创建请求", required = true)
            @Valid @RequestBody PostCreateRequest request) {
        Post post = postService.createPost(request);
        return Result.success(post);
    }

    /**
     * 更新帖子
     *
     * @param id 帖子ID
     * @param request 更新请求
     * @return 更新结果
     */
    @ApiOperation(value = "更新帖子", notes = "更新帖子内容")
    @PutMapping("/{id}")
    public Result<Post> updatePost(
            @ApiParam(value = "帖子ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "更新请求", required = true)
            @Valid @RequestBody PostUpdateRequest request) {
        Post post = postService.updatePost(id, request);
        return Result.success(post);
    }

    /**
     * 删除帖子
     *
     * @param id 帖子ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除帖子", notes = "删除帖子")
    @DeleteMapping("/{id}")
    public Result<?> deletePost(
            @ApiParam(value = "帖子ID", required = true)
            @PathVariable Long id) {
        postService.deletePost(id);
        return Result.success();
    }

    /**
     * 点赞/取消点赞帖子
     *
     * @param id 帖子ID
     * @param action 操作：like-点赞，unlike-取消点赞
     * @return 操作结果
     */
    @ApiOperation(value = "点赞/取消点赞帖子", notes = "更新帖子的点赞状态")
    @PostMapping("/{id}/like")
    public Result<LikeResponse> likePost(
            @ApiParam(value = "帖子ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "操作：like-点赞，unlike-取消点赞", required = true)
            @RequestParam String action) {
        LikeResponse response = postService.likePost(id, action);
        return Result.success(response);
    }

    /**
     * 收藏/取消收藏帖子
     *
     * @param id 帖子ID
     * @param action 操作：collect-收藏，uncollect-取消收藏
     * @return 操作结果
     */
    @ApiOperation(value = "收藏/取消收藏帖子", notes = "更新帖子的收藏状态")
    @PostMapping("/{id}/collect")
    public Result<CollectResponse> collectPost(
            @ApiParam(value = "帖子ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "操作：collect-收藏，uncollect-取消收藏", required = true)
            @RequestParam String action) {
        CollectResponse response = postService.collectPost(id, action);
        return Result.success(response);
    }

    /**
     * 获取推荐封面列表
     *
     * @return 推荐封面列表
     */
    @ApiOperation(value = "获取推荐封面列表", notes = "获取发帖页面的推荐封面图片列表")
    @GetMapping("/recommended-covers")
    public Result<List<CoverInfo>> getRecommendedCovers() {
        List<CoverInfo> covers = postService.getRecommendedCovers();
        return Result.success(covers);
    }

    /**
     * 保存草稿
     */
    @ApiOperation(value = "保存草稿", notes = "保存帖子草稿")
    @PostMapping("/draft")
    public Result<DraftResponse> saveDraft(
            @ApiParam(value = "草稿请求", required = true)
            @Valid @RequestBody DraftRequest request) {
        DraftResponse response = postService.saveDraft(request);
        return Result.success(response);
    }

    /**
     * 获取最热帖子
     */
    @ApiOperation(value = "获取最热帖子", notes = "获取指定专区的最热门帖子列表")
    @GetMapping("/hot")
    public Result<List<Post>> getHotPosts(
            @ApiParam(value = "专区")
            @RequestParam(required = false) String zone,
            @ApiParam(value = "返回数量", example = "10")
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<Post> posts = postService.getHotPosts(zone, limit);
        return Result.success(posts);
    }

    /**
     * 获取帖子评论列表
     */
    @ApiOperation(value = "获取帖子评论列表", notes = "获取指定帖子的评论列表")
    @GetMapping("/{id}/comments")
    public Result<PageResult<Comment>> getPostComments(
            @ApiParam(value = "帖子ID", required = true)
            @PathVariable Long id,
            PageQuery pageQuery) {
        PageResult<Comment> result = postService.getPostComments(id, pageQuery);
        return Result.success(result);
    }

    /**
     * 发表评论
     */
    @ApiOperation(value = "发表评论", notes = "在指定帖子下发表评论")
    @PostMapping("/{id}/comments")
    public Result<Comment> createComment(
            @ApiParam(value = "帖子ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "评论请求", required = true)
            @Valid @RequestBody CommentCreateRequest request) {
        Comment comment = postService.createComment(id, request);
        return Result.success(comment);
    }

    /**
     * 草稿请求
     */
    public static class DraftRequest {
        private String title;
        private String summary;
        private String content;
        private String[] tags;
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
        public String[] getTags() { return tags; }
        public void setTags(String[] tags) { this.tags = tags; }
        public String getCover() { return cover; }
        public void setCover(String cover) { this.cover = cover; }
        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
        public Long getToolId() { return toolId; }
        public void setToolId(Long toolId) { this.toolId = toolId; }
    }

    /**
     * 草稿响应
     */
    public static class DraftResponse {
        private String draftId;
        private String savedAt;
        private String message;

        public String getDraftId() { return draftId; }
        public void setDraftId(String draftId) { this.draftId = draftId; }
        public String getSavedAt() { return savedAt; }
        public void setSavedAt(String savedAt) { this.savedAt = savedAt; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * 评论创建请求
     */
    public static class CommentCreateRequest {
        private String content;
        private Long replyTo;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Long getReplyTo() { return replyTo; }
        public void setReplyTo(Long replyTo) { this.replyTo = replyTo; }
    }

    /**
     * 点赞响应
     */
    public static class LikeResponse {
        private Boolean liked;
        private Integer likes;

        public Boolean getLiked() {
            return liked;
        }

        public void setLiked(Boolean liked) {
            this.liked = liked;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }
    }

    /**
     * 收藏响应
     */
    public static class CollectResponse {
        private Boolean collected;

        public Boolean getCollected() {
            return collected;
        }

        public void setCollected(Boolean collected) {
            this.collected = collected;
        }
    }

    /**
     * 封面信息
     */
    public static class CoverInfo {
        private Long id;
        private String url;
        private String thumbnail;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}
