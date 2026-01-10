package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.entity.Reply;
import com.aicommunity.mapper.ReplyMapper;
import com.aicommunity.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 回复服务实现类
 *
 * @author AI Community Team
 */
@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReply(Long id) {
        Reply reply = replyMapper.selectById(id);
        if (reply == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND, "回复不存在");
        }

        Long currentUserId = getCurrentUserId();
        if (!reply.getUserId().equals(currentUserId) && !isAdmin(currentUserId)) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN);
        }

        replyMapper.deleteById(id);
    }

    @Autowired
    private com.aicommunity.mapper.UserRoleMapper userRoleMapper;

    private Long getCurrentUserId() {
        return com.aicommunity.util.UserContext.getUserId();
    }

    private boolean isAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        List<String> roles = userRoleMapper.selectRolesByUserId(userId);
        return roles != null && roles.contains("admin");
    }
}
