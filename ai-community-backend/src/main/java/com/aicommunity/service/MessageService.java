package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.MessageUnreadCountResponse;
import com.aicommunity.entity.Message;

/**
 * 消息服务接口
 *
 * @author AI Community Team
 */
public interface MessageService {
    /**
     * 获取消息列表
     *
     * @param type 消息类型
     * @param pageQuery 分页参数
     * @return 消息列表
     */
    PageResult<Message> getMessages(String type, PageQuery pageQuery);

    /**
     * 标记消息为已读
     *
     * @param id 消息ID
     */
    void markAsRead(Long id);

    /**
     * 全部标记为已读
     */
    void markAllAsRead();

    /**
     * 删除消息
     *
     * @param id 消息ID
     */
    void deleteMessage(Long id);

    /**
     * 获取未读消息数量
     *
     * @return 未读消息数量响应
     */
    MessageUnreadCountResponse getUnreadCount();
}
