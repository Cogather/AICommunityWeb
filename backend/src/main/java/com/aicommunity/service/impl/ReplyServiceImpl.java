package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.entity.Reply;
import com.aicommunity.entity.UserInfo;
import com.aicommunity.mapper.*;
import com.aicommunity.service.MessageService;
import com.aicommunity.service.ReplyService;
import com.aicommunity.vo.ReplyCreateRequestVO;
import com.aicommunity.vo.ReplyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 回复服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReplyVO createReply(Integer commentId, ReplyCreateRequestVO request, String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "请先登录");
        }

        // 创建回复
        Reply reply = new Reply();
        reply.setCommentId(commentId);
        reply.setUserId(userId);
        reply.setReplyToUserId(request.getReplyToUserId());
        reply.setReplyToId(request.getReplyToId());
        reply.setContent(request.getContent());
        reply.setLikes(0);
        reply.setStatus("0");
        Date now = new Date();
        reply.setCreateTime(now);
        reply.setUpdateTime(now);

        int result = replyMapper.insertReply(reply);
        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "创建回复失败");
        }

        // 发送回复通知
        try {
            // 获取评论信息
            Comment comment = commentMapper.selectById(commentId);
            if (comment != null) {
                UserInfo replier = userInfoMapper.selectByUserId(userId);
                String replierName = replier != null ? replier.getChnName() : "用户";
                messageService.sendCommentReplyNotification(comment.getPostId(), comment.getUserId(),
                                                         userId, replierName, commentId, reply.getId());
            }
        } catch (Exception e) {
            log.warn("发送回复通知失败", e);
            // 不影响主流程
        }

        return convertToReplyVO(reply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReply(Integer replyId, String userId) {
        // 查询回复
        Reply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "回复不存在");
        }

        // 权限校验
        if (!userId.equals(reply.getUserId())) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN.getCode(), "无权限删除此回复");
        }

        // 删除回复（逻辑删除）
        int result = replyMapper.deleteReply(replyId);
        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "删除回复失败");
        }
    }

    /**
     * 转换为ReplyVO
     */
    private ReplyVO convertToReplyVO(Reply reply) {
        ReplyVO vo = new ReplyVO();
        vo.setId(reply.getId());
        vo.setCommentId(reply.getCommentId());
        vo.setUserId(reply.getUserId());
        vo.setReplyToUserId(reply.getReplyToUserId());
        vo.setReplyToId(reply.getReplyToId());

        // 查询用户信息
        UserInfo user = userInfoMapper.selectByUserId(reply.getUserId());
        if (user != null) {
            vo.setUserName(user.getChnName());
            vo.setUserAvatar(user.getAuthorAvatar());
        }

        // 查询被回复者信息
        if (StringUtils.hasText(reply.getReplyToUserId())) {
            UserInfo replyToUser = userInfoMapper.selectByUserId(reply.getReplyToUserId());
            if (replyToUser != null) {
                vo.setReplyTo(replyToUser.getChnName());
            }
        }

        vo.setContent(reply.getContent());
        vo.setLikes(reply.getLikes() != null ? reply.getLikes() : 0);
        vo.setCreateTime(formatDate(reply.getCreateTime()));
        return vo;
    }

    /**
     * 格式化日期
     */
    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return ISO_DATE_FORMAT.format(date);
    }
}
