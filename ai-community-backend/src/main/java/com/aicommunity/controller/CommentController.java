package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.CommentDTO;
import com.aicommunity.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 评论控制器
 *
 * @author AI Community Team
 */
@Api(tags = "评论相关接口")
@RestController
@RequestMapping
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取帖子评论列表
     *
     * @param postId   帖子ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 评论列表
     */
    @ApiOperation(value = "获取帖子评论列表", notes = "获取指定帖子的评论列表")
    @GetMapping("/posts/{postId}/comments")
    public Result<PageResult<CommentDTO>> getComments(
            @ApiParam(value = "帖子ID", required = true) @PathVariable Long postId,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<CommentDTO> result = commentService.getComments(postId, pageQuery);
        return Result.success(result);
    }

    /**
     * 发表评论
     *
     * @param postId  帖子ID
     * @param request 评论请求
     * @return 评论结果
     */
    @ApiOperation(value = "发表评论", notes = "在指定帖子下发表评论")
    @PostMapping("/posts/{postId}/comments")
    public Result<CommentDTO> createComment(
            @ApiParam(value = "帖子ID", required = true) @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request) {
        CommentDTO comment = commentService.createComment(postId, request);
        return Result.success(comment);
    }

    /**
     * 点赞评论
     *
     * @param id      评论ID
     * @param request 点赞请求
     * @return 点赞结果
     */
    @ApiOperation(value = "点赞评论", notes = "点赞或取消点赞评论")
    @PostMapping("/comments/{id}/like")
    public Result<LikeCommentResponse> likeComment(
            @ApiParam(value = "评论ID", required = true) @PathVariable Long id,
            @RequestBody LikeCommentRequest request) {
        LikeCommentResponse response = commentService.likeComment(id, request);
        return Result.success(response);
    }

    /**
     * 更新评论
     *
     * @param id      评论ID
     * @param request 更新请求
     * @return 更新结果
     */
    @ApiOperation(value = "更新评论", notes = "更新评论内容")
    @PutMapping("/comments/{id}")
    public Result<UpdateCommentResponse> updateComment(
            @ApiParam(value = "评论ID", required = true) @PathVariable Long id,
            @RequestBody UpdateCommentRequest request) {
        UpdateCommentResponse response = commentService.updateComment(id, request);
        return Result.success(response);
    }

    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除评论", notes = "删除评论")
    @DeleteMapping("/comments/{id}")
    public Result<?> deleteComment(
            @ApiParam(value = "评论ID", required = true) @PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }

    /**
     * 删除回复
     *
     * @param id 回复ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除回复", notes = "删除回复")
    @DeleteMapping("/replies/{id}")
    public Result<?> deleteReply(
            @ApiParam(value = "回复ID", required = true) @PathVariable Long id) {
        commentService.deleteReply(id);
        return Result.success();
    }

    /**
     * 创建评论请求DTO
     */
    public static class CreateCommentRequest {
        private String content;
        private Long replyTo; // 回复的评论ID（如果是回复评论则必填）

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Long getReplyTo() { return replyTo; }
        public void setReplyTo(Long replyTo) { this.replyTo = replyTo; }
    }

    /**
     * 点赞评论请求DTO
     */
    public static class LikeCommentRequest {
        private String action; // "like" | "unlike"

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }

    /**
     * 点赞评论响应DTO
     */
    public static class LikeCommentResponse {
        private Boolean liked;
        private Integer likes;

        public LikeCommentResponse(Boolean liked, Integer likes) {
            this.liked = liked;
            this.likes = likes;
        }

        public Boolean getLiked() { return liked; }
        public void setLiked(Boolean liked) { this.liked = liked; }
        public Integer getLikes() { return likes; }
        public void setLikes(Integer likes) { this.likes = likes; }
    }

    /**
     * 更新评论请求DTO
     */
    public static class UpdateCommentRequest {
        private String content;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    /**
     * 更新评论响应DTO
     */
    public static class UpdateCommentResponse {
        private Long id;
        private String content;
        private String updateTime;
        private String message;

        public UpdateCommentResponse(Long id, String content, String updateTime, String message) {
            this.id = id;
            this.content = content;
            this.updateTime = updateTime;
            this.message = message;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getUpdateTime() { return updateTime; }
        public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
