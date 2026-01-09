package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.dto.MessageDTO;
import com.aicommunity.entity.Message;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.MessageMapper;
import com.aicommunity.mapper.UserMapper;
import com.aicommunity.service.MessageService;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息服务实现类
 *
 * @author AI Community Team
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Override
    public PageResult<MessageDTO> getMessages(String type, PageQuery pageQuery) {
        Long userId = getCurrentUserId();

        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Message> messages = messageMapper.selectByCondition(userId, type);
        PageInfo<Message> pageInfo = new PageInfo<>(messages);

        // 转换为DTO
        List<MessageDTO> dtoList = messages.stream()
                .map(message -> {
                    MessageDTO dto = new MessageDTO();
                    dto.setId(message.getId());
                    dto.setType(message.getType());
                    dto.setTitle(message.getTitle());
                    dto.setContent(message.getContent());
                    dto.setRead(message.getRead());
                    dto.setCreatedAt(message.getCreateTime().toString());
                    dto.setLink(message.getLink());

                    // 查询发送用户信息
                    if (message.getFromUserId() != null) {
                        User fromUser = userMapper.selectById(message.getFromUserId());
                        if (fromUser != null) {
                            dto.setFromUserName(fromUser.getName());
                        }
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        // 统计未读消息数
        int unreadCount = messageMapper.countUnread(userId);

        // 构建返回结果（包含未读消息数）
        Map<String, Object> result = Map.of(
            "list", dtoList,
            "total", pageInfo.getTotal(),
            "unreadCount", unreadCount
        );

        return PageResult.of(dtoList, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id) {
        Long userId = getCurrentUserId();
        Message message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }

        // 验证权限（只能标记自己的消息）
        if (!message.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权限操作此消息");
        }

        messageMapper.markAsRead(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead() {
        Long userId = getCurrentUserId();
        messageMapper.markAllAsRead(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Long id) {
        Long userId = getCurrentUserId();
        Message message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }

        // 验证权限（只能删除自己的消息）
        if (!message.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权限删除此消息");
        }

        messageMapper.deleteById(id);
    }

    @Override
    public Object getUnreadCount() {
        Long userId = getCurrentUserId();
        int count = messageMapper.countUnread(userId);
        return Map.of("count", count);
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new BusinessException(401, "未授权，请先登录");
    }
}
