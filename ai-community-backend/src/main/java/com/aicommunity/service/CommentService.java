package com.aicommunity.service;

import com.aicommunity.dto.CommentLikeResponse;
import com.aicommunity.dto.CommentUpdateRequest;

/**
 * 评论服务接口
 *
 * @author AI Community Team
 */
public interface CommentService {
    /**
     * 点赞评论
     *
     * @param id 评论ID
     * @param action 操作：like-点赞，unlike-取消点赞
     * @return 点赞响应
     */
    CommentLikeResponse likeComment(Long id, String action);

    /**
     * 更新评论
     *
     * @param id 评论ID
     * @param request 更新请求
     */
    void updateComment(Long id, CommentUpdateRequest request);

    /**
     * 删除评论
     *
     * @param id 评论ID
     */
    void deleteComment(Long id);
}
