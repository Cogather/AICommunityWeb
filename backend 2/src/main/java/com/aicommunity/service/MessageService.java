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

    /**
     * 发送帖子评论通知
     *
     * @param postId         帖子ID
     * @param postTitle      帖子标题
     * @param postAuthorId   帖子作者ID
     * @param commenterId    评论者ID
     * @param commenterName  评论者姓名
     * @param commentId      评论ID
     */
    void sendPostCommentNotification(String postId, String postTitle, String postAuthorId,
                                   String commenterId, String commenterName, Integer commentId);

    /**
     * 发送评论回复通知
     *
     * @param postId          帖子ID
     * @param commentAuthorId 评论作者ID
     * @param replierId       回复者ID
     * @param replierName     回复者姓名
     * @param commentId       评论ID
     * @param replyId         回复ID
     */
    void sendCommentReplyNotification(String postId, String commentAuthorId, String replierId,
                                    String replierName, Integer commentId, Integer replyId);

    /**
     * 发送帖子点赞通知
     *
     * @param postId      帖子ID
     * @param postTitle   帖子标题
     * @param postAuthorId 帖子作者ID
     * @param likerId     点赞者ID
     * @param likerName   点赞者姓名
     */
    void sendPostLikeNotification(String postId, String postTitle, String postAuthorId,
                                String likerId, String likerName);
}
