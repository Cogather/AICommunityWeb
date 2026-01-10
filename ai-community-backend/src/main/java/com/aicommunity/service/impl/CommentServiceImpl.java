package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.dto.CommentLikeResponse;
import com.aicommunity.dto.CommentUpdateRequest;
import com.aicommunity.entity.Comment;
import com.aicommunity.mapper.CommentMapper;
import com.aicommunity.mapper.LikeMapper;
import com.aicommunity.service.CommentService;
import com.aicommunity.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 评论服务实现类
 *
 * @author AI Community Team
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private PointsService pointsService;

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentLikeResponse likeComment(Long id, String action) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND, "评论不存在");
        }

        boolean exists = likeMapper.existsByUserAndTarget(currentUserId, "comment", id);
        CommentLikeResponse response = new CommentLikeResponse();

        if ("like".equals(action)) {
            if (!exists) {
                likeMapper.insert(currentUserId, "comment", id);
                commentMapper.incrementLikes(id);

                // 计算积分（评论被点赞+1，管理员除外）
                if (!isAdmin(comment.getUserId())) {
                    pointsService.addPoints(comment.getUserId(), 1, "like_received", id, "comment");
                }
            }
            response.setLiked(true);
        } else if ("unlike".equals(action)) {
            if (exists) {
                likeMapper.deleteByUserAndTarget(currentUserId, "comment", id);
                commentMapper.decrementLikes(id);
            }
            response.setLiked(false);
        }

        Comment updatedComment = commentMapper.selectById(id);
        response.setLikes(updatedComment.getLikes());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateComment(Long id, CommentUpdateRequest request) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND, "评论不存在");
        }

        Long currentUserId = getCurrentUserId();
        if (!comment.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN);
        }

        comment.setContent(request.getContent());
        comment.setUpdateTime(new Date());
        commentMapper.updateById(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND, "评论不存在");
        }

        Long currentUserId = getCurrentUserId();
        if (!comment.getUserId().equals(currentUserId) && !isAdmin(currentUserId)) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN);
        }

        // 删除评论下的所有回复
        replyMapper.deleteByCommentId(id);
        // 删除评论的点赞记录
        likeMapper.deleteByTarget("comment", id);
        // 删除评论
        commentMapper.deleteById(id);
    }

    @Autowired
    private ReplyMapper replyMapper;

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
