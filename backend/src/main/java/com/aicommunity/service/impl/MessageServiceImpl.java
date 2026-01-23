package com.aicommunity.service.impl;

import com.aicommunity.common.PageResult;
import com.aicommunity.entity.Message;
import com.aicommunity.mapper.MessageMapper;
import com.aicommunity.service.MessageService;
import com.aicommunity.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * 消息服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public PageResult<MessageVO> getMessages(String userId, String type, Integer page, Integer pageSize) {
        log.info("获取用户消息列表，用户ID：{}，消息类型：{}，页码：{}，每页数量：{}", userId, type, page, pageSize);

        // 参数校验和默认值设置
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }

        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询消息列表
        List<Message> messages = messageMapper.selectMessagesByUserId(userId, type, offset, pageSize);

        // 统计总数
        Long total = messageMapper.countMessagesByUserId(userId, type);

        // 转换为VO列表
        List<MessageVO> messageVOList = new ArrayList<>();
        for (Message message : messages) {
            MessageVO messageVO = convertToVO(message);
            messageVOList.add(messageVO);
        }

        return new PageResult<>(messageVOList, total, page, pageSize);
    }

    @Override
    public Long getUnreadCount(String userId) {
        log.info("获取未读消息数量，用户ID：{}", userId);
        return messageMapper.countUnreadMessages(userId);
    }

    @Override
    public void markAsRead(String userId, Long messageId) {
        log.info("标记消息为已读，用户ID：{}，消息ID：{}", userId, messageId);

        // 验证消息是否存在且属于该用户
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            throw new RuntimeException("消息不存在");
        }
        if (!userId.equals(message.getUserId())) {
            throw new RuntimeException("无权限操作他人消息");
        }

        // 标记为已读
        messageMapper.markAsRead(messageId);
    }

    @Override
    public Long markAllAsRead(String userId) {
        log.info("标记所有消息为已读，用户ID：{}", userId);

        // 先统计未读数量
        Long unreadCount = messageMapper.countUnreadMessages(userId);

        // 标记所有为已读
        messageMapper.markAllAsRead(userId);

        return unreadCount;
    }

    @Override
    public void deleteMessage(String userId, Long messageId) {
        log.info("删除消息，用户ID：{}，消息ID：{}", userId, messageId);

        // 验证消息是否存在且属于该用户
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            throw new RuntimeException("消息不存在");
        }
        if (!userId.equals(message.getUserId())) {
            throw new RuntimeException("无权限删除他人消息");
        }

        // 删除消息
        messageMapper.deleteMessage(messageId);
    }

    /**
     * 将Message实体转换为MessageVO
     *
     * @param message 消息实体
     * @return 消息VO
     */
    private MessageVO convertToVO(Message message) {
        MessageVO messageVO = new MessageVO();
        BeanUtils.copyProperties(message, messageVO);

        // 转换字段名
        messageVO.setRead(message.getIsRead() != null && message.getIsRead() == 1);

        // 转换relatedId为Long类型
        if (StringUtils.hasText(message.getRelatedId())) {
            try {
                messageVO.setRelatedId(Long.parseLong(message.getRelatedId()));
            } catch (NumberFormatException e) {
                log.warn("relatedId转换失败：{}", message.getRelatedId());
            }
        }

        // 转换fromUserId为Long类型
        if (StringUtils.hasText(message.getFromUserId())) {
            try {
                messageVO.setFromUserId(Long.parseLong(message.getFromUserId()));
            } catch (NumberFormatException e) {
                log.warn("fromUserId转换失败：{}", message.getFromUserId());
            }
        }

        // 设置前端兼容字段
        messageVO.setUserId(message.getFromUserId());
        messageVO.setUserName(message.getFromUserName());

        // 转换时间为ISO 8601格式
        if (message.getCreateTime() != null) {
            messageVO.setCreatedAt(ISO_DATE_FORMAT.format(message.getCreateTime()));
        }

        return messageVO;
    }

    @Override
    public void sendPostCommentNotification(String postId, String postTitle, String postAuthorId,
                                          String commenterId, String commenterName, Integer commentId) {
        log.info("发送帖子评论通知，帖子ID：{}，评论者：{}，帖子作者：{}", postId, commenterId, postAuthorId);

        // 不给自己发通知
        if (postAuthorId.equals(commenterId)) {
            return;
        }

        Message message = new Message();
        message.setUserId(postAuthorId);
        message.setType("post_comment");
        message.setTitle("帖子评论通知");
        message.setContent(commenterName + " 评论了您的帖子《" + postTitle + "》");
        message.setRelatedId(postId);
        message.setRelatedType("post");
        message.setCommentId(commentId);
        message.setFromUserId(commenterId);
        message.setFromUserName(commenterName);
        message.setIsRead(0);

        messageMapper.insertMessage(message);
    }

    @Override
    public void sendCommentReplyNotification(String postId, String commentAuthorId, String replierId,
                                           String replierName, Integer commentId, Integer replyId) {
        log.info("发送评论回复通知，帖子ID：{}，回复者：{}，评论作者：{}", postId, replierId, commentAuthorId);

        // 不给自己发通知
        if (commentAuthorId.equals(replierId)) {
            return;
        }

        Message message = new Message();
        message.setUserId(commentAuthorId);
        message.setType("comment_reply");
        message.setTitle("评论回复通知");
        message.setContent(replierName + " 回复了您的评论");
        message.setRelatedId(postId);
        message.setRelatedType("post");
        message.setCommentId(commentId);
        message.setReplyId(replyId);
        message.setFromUserId(replierId);
        message.setFromUserName(replierName);
        message.setIsRead(0);

        messageMapper.insertMessage(message);
    }

    @Override
    public void sendPostLikeNotification(String postId, String postTitle, String postAuthorId,
                                       String likerId, String likerName) {
        log.info("发送帖子点赞通知，帖子ID：{}，点赞者：{}，帖子作者：{}", postId, likerId, postAuthorId);

        // 不给自己发通知
        if (postAuthorId.equals(likerId)) {
            return;
        }

        Message message = new Message();
        message.setUserId(postAuthorId);
        message.setType("post_like");
        message.setTitle("点赞通知");
        message.setContent(likerName + " 赞了您的帖子《" + postTitle + "》");
        message.setRelatedId(postId);
        message.setRelatedType("post");
        message.setFromUserId(likerId);
        message.setFromUserName(likerName);
        message.setIsRead(0);

        messageMapper.insertMessage(message);
    }
}
