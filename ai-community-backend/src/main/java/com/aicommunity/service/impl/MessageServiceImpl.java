package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.MessageUnreadCountResponse;
import com.aicommunity.entity.Message;
import com.aicommunity.mapper.MessageMapper;
import com.aicommunity.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息服务实现类
 *
 * @author AI Community Team
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public PageResult<Message> getMessages(String type, PageQuery pageQuery) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Message> messages = messageMapper.selectByUserId(currentUserId, type);
        PageInfo<Message> pageInfo = new PageInfo<>(messages);
        return PageResult.of(messages, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id) {
        messageMapper.updateReadStatus(id, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }
        messageMapper.updateAllReadStatus(currentUserId, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Long id) {
        messageMapper.deleteById(id);
    }

    @Override
    public MessageUnreadCountResponse getUnreadCount() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Integer count = messageMapper.countUnreadByUserId(currentUserId);
        MessageUnreadCountResponse response = new MessageUnreadCountResponse();
        response.setCount(count != null ? count : 0);
        return response;
    }

    private Long getCurrentUserId() {
        return com.aicommunity.util.UserContext.getUserId();
    }
}
