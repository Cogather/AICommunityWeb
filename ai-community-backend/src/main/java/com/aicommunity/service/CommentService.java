package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.controller.CommentController;
import com.aicommunity.dto.CommentDTO;

/**
 * 评论服务接口
 *
 * @author AI Community Team
 */
public interface CommentService {

    /**
     * 获取帖子评论列表
     *
     * @param postId    帖子ID
     * @param pageQuery 分页参数
     * @return 评论列表
     */
    PageResult<CommentDTO> getComments(Long postId, PageQuery pageQuery);

    /**
     * 发表评论
     *
     * @param postId  帖子ID
     * @param request 评论请求
     * @return 评论
     */
    CommentDTO createComment(Long postId, CommentController.CreateCommentRequest request);

    /**
     * 点赞评论
     *
     * @param id      评论ID
     * @param request 点赞请求
     * @return 点赞结果
     */
    CommentController.LikeCommentResponse likeComment(Long id, CommentController.LikeCommentRequest request);

    /**
     * 更新评论
     *
     * @param id      评论ID
     * @param request 更新请求
     * @return 更新结果
     */
    CommentController.UpdateCommentResponse updateComment(Long id, CommentController.UpdateCommentRequest request);

    /**
     * 删除评论
     *
     * @param id 评论ID
     */
    void deleteComment(Long id);

    /**
     * 删除回复
     *
     * @param id 回复ID
     */
    void deleteReply(Long id);
}
