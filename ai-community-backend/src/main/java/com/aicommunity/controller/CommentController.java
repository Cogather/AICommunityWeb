package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.CommentLikeResponse;
import com.aicommunity.dto.CommentUpdateRequest;
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
@Api(tags = "评论管理")
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 点赞评论
     */
    @ApiOperation(value = "点赞评论", notes = "点赞或取消点赞评论")
    @PostMapping("/{id}/like")
    public Result<CommentLikeResponse> likeComment(
            @ApiParam(value = "评论ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "操作：like-点赞，unlike-取消点赞", required = true)
            @RequestParam String action) {
        CommentLikeResponse response = commentService.likeComment(id, action);
        return Result.success(response);
    }

    /**
     * 更新评论
     */
    @ApiOperation(value = "更新评论", notes = "更新评论内容")
    @PutMapping("/{id}")
    public Result<?> updateComment(
            @ApiParam(value = "评论ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "更新请求", required = true)
            @Valid @RequestBody CommentUpdateRequest request) {
        commentService.updateComment(id, request);
        return Result.success();
    }

    /**
     * 删除评论
     */
    @ApiOperation(value = "删除评论", notes = "删除评论")
    @DeleteMapping("/{id}")
    public Result<?> deleteComment(
            @ApiParam(value = "评论ID", required = true)
            @PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }
}
