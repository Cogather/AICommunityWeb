package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.CommentService;
import com.aicommunity.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 评论控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "评论接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取帖子评论列表
     *
     * @param postId   帖子ID
     * @param page     页码
     * @param pageSize 每页数量
     * @param sortBy   排序方式：newest-最新，hot-最热
     * @return 评论列表
     */
    @GetMapping("/posts/{postId}/comments")
    @ApiOperation(value = "获取帖子评论列表", notes = "获取指定帖子的评论列表")
    public Result<CommentListVO> getComments(
            @ApiParam(value = "帖子ID", required = true, example = "1")
            @PathVariable String postId,
            @ApiParam(value = "页码，默认1", example = "1")
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页条数，默认15", example = "15")
            @RequestParam(required = false, defaultValue = "15") Integer pageSize,
            @ApiParam(value = "排序方式：newest-最新，hot-最热", example = "newest")
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            HttpServletRequest request) {
        log.info("获取帖子评论列表, postId={}, page={}, pageSize={}, sortBy={}", postId, page, pageSize, sortBy);
        String userId = getCurrentUserId(request);
        CommentListVO result = commentService.getComments(postId, page, pageSize, sortBy, userId);
        return Result.success(result);
    }

    /**
     * 创建评论
     *
     * @param postId    帖子ID
     * @param requestVO 创建评论请求
     * @param request   HTTP请求对象
     * @return 评论信息
     */
    @PostMapping("/posts/{postId}/comments")
    @ApiOperation(value = "创建评论", notes = "在指定帖子下创建评论")
    public Result<CommentVO> createComment(
            @ApiParam(value = "帖子ID", required = true, example = "1")
            @PathVariable String postId,
            @ApiParam(value = "创建评论请求", required = true)
            @Valid @RequestBody CommentCreateRequestVO requestVO,
            HttpServletRequest request) {
        log.info("创建评论, postId={}", postId);
        String userId = getCurrentUserId(request);
        CommentVO result = commentService.createComment(postId, requestVO, userId);
        return Result.success(result);
    }

    /**
     * 更新评论
     *
     * @param id        评论ID
     * @param requestVO 更新评论请求
     * @param request   HTTP请求对象
     * @return 评论信息
     */
    @PutMapping("/comments/{id}")
    @ApiOperation(value = "更新评论", notes = "更新指定评论的内容")
    public Result<CommentVO> updateComment(
            @ApiParam(value = "评论ID", required = true, example = "1")
            @PathVariable Integer id,
            @ApiParam(value = "更新评论请求", required = true)
            @Valid @RequestBody CommentUpdateRequestVO requestVO,
            HttpServletRequest request) {
        log.info("更新评论, commentId={}", id);
        String userId = getCurrentUserId(request);
        CommentVO result = commentService.updateComment(id, requestVO, userId);
        return Result.success(result);
    }

    /**
     * 删除评论
     *
     * @param id      评论ID
     * @param request HTTP请求对象
     * @return 响应结果
     */
    @DeleteMapping("/comments/{id}")
    @ApiOperation(value = "删除评论", notes = "删除指定评论（同时删除该评论下的所有回复）")
    public Result<Void> deleteComment(
            @ApiParam(value = "评论ID", required = true, example = "1")
            @PathVariable Integer id,
            HttpServletRequest request) {
        log.info("删除评论, commentId={}", id);
        String userId = getCurrentUserId(request);
        commentService.deleteComment(id, userId);
        return Result.success();
    }

    /**
     * 点赞/取消点赞评论
     *
     * @param id        评论ID
     * @param requestVO 点赞操作请求
     * @param request   HTTP请求对象
     * @return 点赞响应
     */
    @PostMapping("/comments/{id}/like")
    @ApiOperation(value = "点赞/取消点赞评论", notes = "对评论进行点赞或取消点赞操作")
    public Result<LikeResponseVO> likeComment(
            @ApiParam(value = "评论ID", required = true, example = "1")
            @PathVariable Integer id,
            @ApiParam(value = "点赞操作请求", required = true)
            @Valid @RequestBody LikeActionRequestVO requestVO,
            HttpServletRequest request) {
        log.info("点赞评论, commentId={}, action={}", id, requestVO.getAction());
        String userId = getCurrentUserId(request);
        LikeResponseVO result = commentService.likeComment(id, requestVO.getAction(), userId);
        return Result.success(result);
    }

    /**
     * 从HTTP请求头获取当前登录用户ID
     *
     * @param request HTTP请求对象
     * @return 用户ID，如果未登录则返回null
     */
    private String getCurrentUserId(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }
}
