package com.aicommunity.controller;

import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.service.MessageService;
import com.aicommunity.vo.MessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息中心控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/messages")
@Api(tags = "消息中心接口", description = "提供消息相关的API接口，包括获取消息列表、标记已读、删除消息等功能")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取消息列表
     * GET /messages
     * 获取当前登录用户的消息列表（分页），支持按消息类型筛选
     *
     * @param userId   用户ID（从请求头获取）
     * @param type     消息类型筛选（可选）：post_comment-帖子评论，comment_reply-评论回复，post_like-点赞通知，award_notification-奖项通知
     * @param page     页码，默认1
     * @param pageSize 每页数量，默认15
     * @return 消息列表
     */
    @GetMapping
    @ApiOperation(value = "获取消息列表", notes = "获取当前登录用户的消息列表（分页），支持按消息类型筛选")
    public Result<PageResult<MessageVO>> getMessages(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @ApiParam(value = "消息类型筛选", example = "post_comment")
            @RequestParam(value = "type", required = false) String type,
            @ApiParam(value = "页码", example = "1")
            @RequestParam(value = "page", required = false) Integer page,
            @ApiParam(value = "每页数量", example = "15")
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        log.info("获取消息列表，用户ID：{}，消息类型：{}，页码：{}，每页数量：{}", userId, type, page, pageSize);
        try {
            if (userId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            PageResult<MessageVO> result = messageService.getMessages(userId, type, page, pageSize);
            return Result.success(result);
        } catch (RuntimeException e) {
            log.error("获取消息列表失败：{}", e.getMessage());
            return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("获取消息列表异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取消息列表失败");
        }
    }

    /**
     * 获取未读消息数量
     * GET /messages/unread-count
     * 获取当前用户的未读消息数量，用于导航栏红点提示
     *
     * @param userId 用户ID（从请求头获取）
     * @return 未读消息数量 {count: number}
     */
    @GetMapping("/unread-count")
    @ApiOperation(value = "获取未读消息数量", notes = "获取当前用户的未读消息数量，用于导航栏红点提示")
    public Result<Map<String, Long>> getUnreadCount(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("获取未读消息数量，用户ID：{}", userId);
        try {
            if (userId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            Long count = messageService.getUnreadCount(userId);
            Map<String, Long> result = new HashMap<>();
            result.put("count", count);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取未读消息数量异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "获取未读消息数量失败");
        }
    }

    /**
     * 标记消息已读
     * PUT /messages/{id}/read
     * 标记单条消息为已读状态
     *
     * @param userId    用户ID（从请求头获取）
     * @param messageId 消息ID
     * @return 操作结果
     */
    @PutMapping("/{id}/read")
    @ApiOperation(value = "标记消息已读", notes = "标记单条消息为已读状态")
    public Result<Void> markAsRead(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @ApiParam(value = "消息ID", required = true, example = "1")
            @PathVariable("id") Long messageId) {
        log.info("标记消息已读，用户ID：{}，消息ID：{}", userId, messageId);
        try {
            if (userId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            messageService.markAsRead(userId, messageId);
            return Result.success();
        } catch (RuntimeException e) {
            log.error("标记消息已读失败：{}", e.getMessage());
            if ("消息不存在".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.NOT_FOUND.getCode(), "消息不存在");
            }
            if ("无权限操作他人消息".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.FORBIDDEN.getCode(), "无权限操作他人消息");
            }
            return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("标记消息已读异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "标记消息已读失败");
        }
    }

    /**
     * 标记全部已读
     * PUT /messages/read-all
     * 将当前用户所有未读消息标记为已读
     *
     * @param userId 用户ID（从请求头获取）
     * @return 操作结果（包含本次标记为已读的消息数量）{count: number}
     */
    @PutMapping("/read-all")
    @ApiOperation(value = "标记全部已读", notes = "将当前用户所有未读消息标记为已读")
    public Result<Map<String, Long>> markAllAsRead(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("标记全部已读，用户ID：{}", userId);
        try {
            if (userId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            Long count = messageService.markAllAsRead(userId);
            Map<String, Long> result = new HashMap<>();
            result.put("count", count);
            return Result.success(result);
        } catch (Exception e) {
            log.error("标记全部已读异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "标记全部已读失败");
        }
    }

    /**
     * 删除消息
     * DELETE /messages/{id}
     * 删除单条消息
     *
     * @param userId    用户ID（从请求头获取）
     * @param messageId 消息ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除消息", notes = "删除单条消息")
    public Result<Void> deleteMessage(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @ApiParam(value = "消息ID", required = true, example = "1")
            @PathVariable("id") Long messageId) {
        log.info("删除消息，用户ID：{}，消息ID：{}", userId, messageId);
        try {
            if (userId == null) {
                return Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未登录，请先登录");
            }
            messageService.deleteMessage(userId, messageId);
            return Result.success();
        } catch (RuntimeException e) {
            log.error("删除消息失败：{}", e.getMessage());
            if ("消息不存在".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.NOT_FOUND.getCode(), "消息不存在");
            }
            if ("无权限删除他人消息".equals(e.getMessage())) {
                return Result.error(ErrorCodeEnum.FORBIDDEN.getCode(), "无权限删除他人消息");
            }
            return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("删除消息异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "删除消息失败");
        }
    }
}
