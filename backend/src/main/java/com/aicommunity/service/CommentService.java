package com.aicommunity.service;

import com.aicommunity.vo.*;

/**
 * 评论服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface CommentService {

    /**
     * 获取帖子评论列表
     *
     * @param postId   帖子ID
     * @param page     页码
     * @param pageSize 每页数量
     * @param sortBy   排序方式：newest-最新，hot-最热
     * @param userId   当前用户ID（用于判断是否点赞）
     * @return 评论列表
     */
    CommentListVO getComments(String postId, Integer page, Integer pageSize, String sortBy, String userId);

    /**
     * 创建评论
     *
     * @param postId  帖子ID
     * @param request 创建评论请求
     * @param userId  用户ID
     * @return 评论信息
     */
    CommentVO createComment(String postId, CommentCreateRequestVO request, String userId);

    /**
     * 更新评论
     *
     * @param commentId 评论ID
     * @param request   更新评论请求
     * @param userId    用户ID
     * @return 评论信息
     */
    CommentVO updateComment(Integer commentId, CommentUpdateRequestVO request, String userId);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @param userId    用户ID
     */
    void deleteComment(Integer commentId, String userId);

    /**
     * 点赞/取消点赞评论
     *
     * @param commentId 评论ID
     * @param action    操作类型：like-点赞，unlike-取消点赞
     * @param userId    用户ID
     * @return 点赞响应
     */
    LikeResponseVO likeComment(Integer commentId, String action, String userId);
}
