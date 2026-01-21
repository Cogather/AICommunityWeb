package com.aicommunity.service;

import com.aicommunity.common.PageResult;
import com.aicommunity.vo.MessageVO;

/**
 * 消息服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface MessageService {

    /**
     * 获取用户消息列表（分页）
     *
     * @param userId   用户ID
     * @param type     消息类型（可选）
     * @param page     页码，默认1
     * @param pageSize 每页数量，默认15
     * @return 消息列表
     */
    PageResult<MessageVO> getMessages(String userId, String type, Integer page, Integer pageSize);

    /**
     * 获取未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Long getUnreadCount(String userId);

    /**
     * 标记消息为已读
     *
     * @param userId    用户ID
     * @param messageId 消息ID
     */
    void markAsRead(String userId, Long messageId);

    /**
     * 标记所有消息为已读
     *
     * @param userId 用户ID
     * @return 本次标记为已读的消息数量
     */
    Long markAllAsRead(String userId);

    /**
     * 删除消息
     *
     * @param userId    用户ID
     * @param messageId 消息ID
     */
    void deleteMessage(String userId, Long messageId);
}
