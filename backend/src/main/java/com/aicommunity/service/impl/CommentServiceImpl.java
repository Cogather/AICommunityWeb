package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.entity.Comment;
import com.aicommunity.entity.Reply;
import com.aicommunity.entity.UserInfo;
import com.aicommunity.mapper.*;
import com.aicommunity.service.CommentService;
import com.aicommunity.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public CommentListVO getComments(String postId, Integer page, Integer pageSize, String sortBy, String userId) {
        // 参数校验
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "newest";
        }

        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询评论列表
        List<Comment> comments = commentMapper.selectCommentsByPostId(postId, sortBy, offset, pageSize);
        Long total = commentMapper.countCommentsByPostId(postId);

        // 转换为VO
        List<CommentVO> commentVOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVO vo = convertToCommentVO(comment, userId);
            // 查询回复列表
            List<Reply> replies = replyMapper.selectRepliesByCommentId(comment.getId());
            List<ReplyVO> replyVOs = replies.stream()
                    .map(this::convertToReplyVO)
                    .collect(Collectors.toList());
            vo.setReplies(replyVOs);
            commentVOs.add(vo);
        }

        CommentListVO result = new CommentListVO();
        result.setList(commentVOs);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO createComment(String postId, CommentCreateRequestVO request, String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "请先登录");
        }

        // 创建评论
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        comment.setLikes(0);
        comment.setStatus("0");
        Date now = new Date();
        comment.setCreateTime(now);
        comment.setUpdateTime(now);

        int result = commentMapper.insertComment(comment);
        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "创建评论失败");
        }

        return convertToCommentVO(comment, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO updateComment(Integer commentId, CommentUpdateRequestVO request, String userId) {
        // 查询评论
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "评论不存在");
        }

        // 权限校验
        if (!userId.equals(comment.getUserId())) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN.getCode(), "无权限修改此评论");
        }

        // 更新评论
        comment.setContent(request.getContent());
        comment.setUpdateTime(new Date());

        int result = commentMapper.updateComment(comment);
        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "更新评论失败");
        }

        return convertToCommentVO(comment, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Integer commentId, String userId) {
        // 查询评论
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "评论不存在");
        }

        // 权限校验
        if (!userId.equals(comment.getUserId())) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN.getCode(), "无权限删除此评论");
        }

        // 删除评论（逻辑删除）
        int result = commentMapper.deleteComment(commentId);
        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "删除评论失败");
        }

        // 删除该评论下的所有回复（逻辑删除）
        replyMapper.deleteRepliesByCommentId(commentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LikeResponseVO likeComment(Integer commentId, String action, String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "请先登录");
        }

        // 查询评论是否存在
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "评论不存在");
        }

        // 执行点赞或取消点赞
        if ("like".equals(action)) {
            Integer likeCount = commentLikeMapper.selectLikeCount(commentId, userId);
            if (likeCount == null || likeCount == 0) {
                commentLikeMapper.insertLike(commentId, userId);
            }
        } else if ("unlike".equals(action)) {
            commentLikeMapper.deleteLike(commentId, userId);
        } else {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST.getCode(), "操作类型错误");
        }

        // 查询当前点赞数
        Integer likes = commentMapper.countLikes(commentId);
        Boolean liked = "like".equals(action);

        LikeResponseVO vo = new LikeResponseVO();
        vo.setLiked(liked);
        vo.setLikes(likes != null ? likes : 0);
        return vo;
    }

    /**
     * 转换为CommentVO
     */
    private CommentVO convertToCommentVO(Comment comment, String userId) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setPostId(Integer.parseInt(comment.getPostId()));
        vo.setUserId(Integer.parseInt(comment.getUserId()));

        // 查询用户信息
        UserInfo user = userInfoMapper.selectByUserId(comment.getUserId());
        if (user != null) {
            vo.setUserName(user.getChnName());
            vo.setUserAvatar(user.getAuthorAvatar());
        }

        vo.setContent(comment.getContent());
        vo.setLikes(comment.getLikes() != null ? comment.getLikes() : 0);
        vo.setCreateTime(formatDate(comment.getCreateTime()));

        // 查询是否已点赞
        if (StringUtils.hasText(userId)) {
            Integer likeCount = commentLikeMapper.selectLikeCount(comment.getId(), userId);
            vo.setIsLiked(likeCount != null && likeCount > 0);
        } else {
            vo.setIsLiked(false);
        }

        return vo;
    }

    /**
     * 转换为ReplyVO
     */
    private ReplyVO convertToReplyVO(Reply reply) {
        ReplyVO vo = new ReplyVO();
        vo.setId(reply.getId());
        vo.setCommentId(reply.getCommentId());
        vo.setUserId(Integer.parseInt(reply.getUserId()));
        vo.setReplyToUserId(reply.getReplyToUserId() != null ? Integer.parseInt(reply.getReplyToUserId()) : null);

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
