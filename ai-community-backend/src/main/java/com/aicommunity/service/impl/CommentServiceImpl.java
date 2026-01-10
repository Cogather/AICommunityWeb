package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.CommentController;
import com.aicommunity.dto.CommentDTO;
import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.CommentService;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Override
    public PageResult<CommentDTO> getComments(Long postId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Map<String, Object>> list = commentMapper.selectByPostId(postId);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

        Long currentUserId = getCurrentUserIdOrNull();
        Long postAuthorId = null;
        Post post = postMapper.selectById(postId);
        if (post != null) {
            postAuthorId = post.getAuthorId();
        }

        // 转换为DTO
        List<CommentDTO> dtoList = list.stream()
                .map(map -> convertToCommentDTO(map, currentUserId, postAuthorId))
                .collect(Collectors.toList());

        return PageResult.of(dtoList, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentDTO createComment(Long postId, CommentController.CreateCommentRequest request) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        Long userId = getCurrentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 创建评论
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        comment.setParentId(request.getReplyTo());
        comment.setLikes(0);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());

        // 如果是回复，设置回复的用户ID
        if (request.getReplyTo() != null) {
            Comment parentComment = commentMapper.selectById(request.getReplyTo());
            if (parentComment != null) {
                comment.setReplyToUserId(parentComment.getUserId());
            }
        }

        commentMapper.insert(comment);

        // 更新帖子评论数
        postMapper.updateComments(postId, 1);

        // 计算积分（发表评论+1，管理员除外）
        if (!isAdmin(userId)) {
            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            PointsRecord record = new PointsRecord();
            record.setUserId(userId);
            record.setPoints(1);
            record.setType("comment");
            record.setTargetId(postId);
            record.setMonth(currentMonth);
            record.setCreateTime(LocalDateTime.now());
            pointsRecordMapper.insert(record);
        }

        // 发送消息通知（如果评论的是帖子，通知帖子作者；如果是回复，通知被回复的用户）
        if (request.getReplyTo() == null) {
            // 评论帖子，通知帖子作者
            if (!post.getAuthorId().equals(userId)) {
                createMessage(post.getAuthorId(), "post_comment", "新评论", 
                    user.getName() + "评论了你的帖子", "/post/" + postId, userId);
            }
        } else {
            // 回复评论，通知被回复的用户
            Comment parentComment = commentMapper.selectById(request.getReplyTo());
            if (parentComment != null && !parentComment.getUserId().equals(userId)) {
                createMessage(parentComment.getUserId(), "comment_reply", "新回复",
                    user.getName() + "回复了你的评论", "/post/" + postId, userId);
            }
        }

        // 构建返回DTO
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setUserId(userId);
        dto.setUserName(user.getName());
        dto.setUserAvatar(user.getAvatar());
        dto.setContent(comment.getContent());
        dto.setCreateTime(comment.getCreateTime().toString());
        dto.setLikes(0);
        dto.setIsLiked(false);
        dto.setReplies(new ArrayList<>());

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentController.LikeCommentResponse likeComment(Long id, CommentController.LikeCommentRequest request) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }

        Long userId = getCurrentUserId();
        LikeRecord likeRecord = likeRecordMapper.selectByUserAndTarget(userId, "comment", id);

        boolean isLike = "like".equals(request.getAction());
        boolean currentlyLiked = likeRecord != null;

        if (isLike && !currentlyLiked) {
            // 点赞
            LikeRecord newRecord = new LikeRecord();
            newRecord.setUserId(userId);
            newRecord.setTargetType("comment");
            newRecord.setTargetId(id);
            newRecord.setCreateTime(LocalDateTime.now());
            likeRecordMapper.insert(newRecord);

            // 更新评论点赞数
            commentMapper.updateLikes(id, 1);

            // 给评论作者加积分（+1，管理员除外）
            if (!isAdmin(comment.getUserId())) {
                String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                PointsRecord record = new PointsRecord();
                record.setUserId(comment.getUserId());
                record.setPoints(1);
                record.setType("like_received");
                record.setTargetId(id);
                record.setMonth(currentMonth);
                record.setCreateTime(LocalDateTime.now());
                pointsRecordMapper.insert(record);
            }
        } else if (!isLike && currentlyLiked) {
            // 取消点赞
            likeRecordMapper.delete(userId, "comment", id);
            commentMapper.updateLikes(id, -1);
        }

        Comment updatedComment = commentMapper.selectById(id);
        return new CommentController.LikeCommentResponse(
            isLike && !currentlyLiked || currentlyLiked && isLike, 
            updatedComment.getLikes());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentController.UpdateCommentResponse updateComment(Long id, CommentController.UpdateCommentRequest request) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }

        Long userId = getCurrentUserId();
        // 验证权限（只有评论作者可以更新）
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权限编辑此评论");
        }

        // 更新评论
        comment.setContent(request.getContent());
        comment.setUpdateTime(LocalDateTime.now());
        commentMapper.update(comment);

        String updateTime = comment.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new CommentController.UpdateCommentResponse(id, comment.getContent(), updateTime, "评论更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }

        Long userId = getCurrentUserId();
        // 验证权限（评论作者或管理员）
        if (!comment.getUserId().equals(userId) && !isAdmin(userId)) {
            throw new BusinessException(403, "无权限删除此评论");
        }

        // 删除评论（级联删除回复）
        commentMapper.deleteById(id);

        // 更新帖子评论数
        postMapper.updateComments(comment.getPostId(), -1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReply(Long id) {
        Comment reply = commentMapper.selectById(id);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }

        // 回复必须有父评论
        if (reply.getParentId() == null) {
            throw new BusinessException("该评论不是回复");
        }

        Long userId = getCurrentUserId();
        // 验证权限（回复作者或管理员）
        if (!reply.getUserId().equals(userId) && !isAdmin(userId)) {
            throw new BusinessException(403, "无权限删除此回复");
        }

        // 删除回复
        commentMapper.deleteById(id);

        // 更新帖子评论数
        postMapper.updateComments(reply.getPostId(), -1);
    }

    /**
     * 转换为CommentDTO
     */
    private CommentDTO convertToCommentDTO(Map<String, Object> map, Long currentUserId, Long postAuthorId) {
        CommentDTO dto = new CommentDTO();
        dto.setId((Long) map.get("id"));
        dto.setUserId((Long) map.get("userId"));
        dto.setUserName((String) map.get("userName"));
        dto.setUserAvatar((String) map.get("userAvatar"));
        dto.setContent((String) map.get("content"));
        dto.setCreateTime(map.get("createTime") != null ? map.get("createTime").toString() : null);
        dto.setUpdateTime(map.get("updateTime") != null ? map.get("updateTime").toString() : null);
        dto.setLikes((Integer) map.get("likes"));

        // 判断当前用户状态
        if (currentUserId != null) {
            dto.setIsLiked(likeRecordMapper.selectByUserAndTarget(currentUserId, "comment", dto.getId()) != null);
            dto.setIsAuthor(postAuthorId != null && postAuthorId.equals(dto.getUserId()));
            dto.setIsMyComment(currentUserId.equals(dto.getUserId()));
            dto.setCanEdit(dto.getIsMyComment());
            dto.setCanDelete(dto.getIsMyComment() || isAdmin(currentUserId));
        } else {
            dto.setIsLiked(false);
            dto.setIsAuthor(false);
            dto.setIsMyComment(false);
            dto.setCanEdit(false);
            dto.setCanDelete(false);
        }

        // 查询回复列表（需要单独查询）
        // TODO: 在Mapper中实现查询回复列表的方法
        dto.setReplies(new ArrayList<>());

        return dto;
    }

    /**
     * 创建消息
     */
    private void createMessage(Long userId, String type, String title, String content, String link, Long fromUserId) {
        Message message = new Message();
        message.setUserId(userId);
        message.setType(type);
        message.setTitle(title);
        message.setContent(content);
        message.setLink(link);
        message.setFromUserId(fromUserId);
        message.setRead(false);
        message.setCreateTime(LocalDateTime.now());
        messageMapper.insert(message);
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

    /**
     * 获取当前用户ID（可为null）
     */
    private Long getCurrentUserIdOrNull() {
        try {
            return getCurrentUserId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否是管理员
     */
    private boolean isAdmin(Long userId) {
        List<String> roles = userRoleMapper.selectRolesByUserId(userId);
        return roles != null && roles.contains("admin");
    }
}
