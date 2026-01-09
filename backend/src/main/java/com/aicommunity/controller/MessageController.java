package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.MessageDTO;
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
@Api(tags = "消息相关接口")
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取消息列表
     *
     * @param type     消息类型
     * @param page     页码
     * @param pageSize 每页数量
     * @return 消息列表
     */
    @ApiOperation(value = "获取消息列表", notes = "获取消息列表，支持按类型筛选")
    @GetMapping
    public Result<PageResult<MessageDTO>> getMessages(
            @ApiParam(value = "消息类型") @RequestParam(required = false) String type,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<MessageDTO> result = messageService.getMessages(type, pageQuery);
        return Result.success(result);
    }

    /**
     * 标记消息为已读
     *
     * @param id 消息ID
     * @return 标记结果
     */
    @ApiOperation(value = "标记消息为已读", notes = "标记指定消息为已读")
    @PutMapping("/{id}/read")
    public Result<?> markAsRead(
            @ApiParam(value = "消息ID", required = true) @PathVariable Long id) {
        messageService.markAsRead(id);
        return Result.success();
    }

    /**
     * 全部标记为已读
     *
     * @return 标记结果
     */
    @ApiOperation(value = "全部标记为已读", notes = "标记当前用户所有消息为已读")
    @PutMapping("/read-all")
    public Result<?> markAllAsRead() {
        messageService.markAllAsRead();
        return Result.success();
    }

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除消息", notes = "删除指定消息")
    @DeleteMapping("/{id}")
    public Result<?> deleteMessage(
            @ApiParam(value = "消息ID", required = true) @PathVariable Long id) {
        messageService.deleteMessage(id);
        return Result.success();
    }

    /**
     * 获取未读消息数量
     *
     * @return 未读消息数量
     */
    @ApiOperation(value = "获取未读消息数量", notes = "获取当前用户的未读消息数量")
    @GetMapping("/unread-count")
    public Result<?> getUnreadCount() {
        return Result.success(messageService.getUnreadCount());
    }
}
