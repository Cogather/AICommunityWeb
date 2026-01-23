package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.ReplyService;
import com.aicommunity.vo.ReplyCreateRequestVO;
import com.aicommunity.vo.ReplyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 回复控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "回复接口")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    /**
     * 创建回复
     *
     * @param commentId 评论ID
     * @param requestVO 创建回复请求
     * @param request   HTTP请求对象
     * @return 回复信息
     */
    @PostMapping("/comments/{commentId}/replies")
    @ApiOperation(value = "创建回复", notes = "在指定评论下创建回复")
    public Result<ReplyVO> createReply(
            @ApiParam(value = "评论ID", required = true, example = "1")
            @PathVariable Integer commentId,
            @ApiParam(value = "创建回复请求", required = true)
            @Valid @RequestBody ReplyCreateRequestVO requestVO,
            HttpServletRequest request) {
        log.info("创建回复, commentId={}", commentId);
        String userId = getCurrentUserId(request);
        ReplyVO result = replyService.createReply(commentId, requestVO, userId);
        return Result.success(result);
    }

    /**
     * 删除回复
     *
     * @param id      回复ID
     * @param request HTTP请求对象
     * @return 响应结果
     */
    @DeleteMapping("/replies/{id}")
    @ApiOperation(value = "删除回复", notes = "删除指定回复")
    public Result<Void> deleteReply(
            @ApiParam(value = "回复ID", required = true, example = "1")
            @PathVariable Integer id,
            HttpServletRequest request) {
        log.info("删除回复, replyId={}", id);
        String userId = getCurrentUserId(request);
        replyService.deleteReply(id, userId);
        return Result.success();
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
