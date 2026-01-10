package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.MessageUnreadCountResponse;
import com.aicommunity.entity.Message;
import com.aicommunity.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息控制器
 *
 * @author AI Community Team
 */
@Api(tags = "消息管理")
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取消息列表
     */
    @ApiOperation(value = "获取消息列表", notes = "获取消息列表，支持按类型筛选")
    @GetMapping
    public Result<PageResult<Message>> getMessages(
            @ApiParam(value = "消息类型")
            @RequestParam(required = false) String type,
            PageQuery pageQuery) {
        PageResult<Message> result = messageService.getMessages(type, pageQuery);
        return Result.success(result);
    }

    /**
     * 标记消息为已读
     */
    @ApiOperation(value = "标记消息为已读", notes = "标记指定消息为已读")
    @PutMapping("/{id}/read")
    public Result<?> markAsRead(
            @ApiParam(value = "消息ID", required = true)
            @PathVariable Long id) {
        messageService.markAsRead(id);
        return Result.success();
    }

    /**
     * 全部标记为已读
     */
    @ApiOperation(value = "全部标记为已读", notes = "将所有消息标记为已读")
    @PutMapping("/read-all")
    public Result<?> markAllAsRead() {
        messageService.markAllAsRead();
        return Result.success();
    }

    /**
     * 删除消息
     */
    @ApiOperation(value = "删除消息", notes = "删除指定消息")
    @DeleteMapping("/{id}")
    public Result<?> deleteMessage(
            @ApiParam(value = "消息ID", required = true)
            @PathVariable Long id) {
        messageService.deleteMessage(id);
        return Result.success();
    }

    /**
     * 获取未读消息数量
     */
    @ApiOperation(value = "获取未读消息数量", notes = "获取当前用户的未读消息数量")
    @GetMapping("/unread-count")
    public Result<MessageUnreadCountResponse> getUnreadCount() {
        MessageUnreadCountResponse response = messageService.getUnreadCount();
        return Result.success(response);
    }
}
