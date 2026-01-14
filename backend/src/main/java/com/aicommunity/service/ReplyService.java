package com.aicommunity.service;

import com.aicommunity.vo.ReplyCreateRequestVO;
import com.aicommunity.vo.ReplyVO;

/**
 * 回复服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface ReplyService {

    /**
     * 创建回复
     *
     * @param commentId 评论ID
     * @param request   创建回复请求
     * @param userId    用户ID
     * @return 回复信息
     */
    ReplyVO createReply(Integer commentId, ReplyCreateRequestVO request, String userId);

    /**
     * 删除回复
     *
     * @param replyId 回复ID
     * @param userId  用户ID
     */
    void deleteReply(Integer replyId, String userId);
}
