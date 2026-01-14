package com.aicommunity.mapper;

import com.aicommunity.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 回复Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface ReplyMapper {

    /**
     * 根据评论ID查询回复列表
     *
     * @param commentId 评论ID
     * @return 回复列表
     */
    List<Reply> selectRepliesByCommentId(@Param("commentId") Integer commentId);

    /**
     * 根据ID查询回复
     *
     * @param id 回复ID
     * @return 回复信息
     */
    Reply selectById(@Param("id") Integer id);

    /**
     * 插入回复
     *
     * @param reply 回复信息
     * @return 影响行数
     */
    int insertReply(Reply reply);

    /**
     * 删除回复（逻辑删除）
     *
     * @param id 回复ID
     * @return 影响行数
     */
    int deleteReply(@Param("id") Integer id);

    /**
     * 根据评论ID删除所有回复（逻辑删除）
     *
     * @param commentId 评论ID
     * @return 影响行数
     */
    int deleteRepliesByCommentId(@Param("commentId") Integer commentId);

    /**
     * 查询回复点赞数
     *
     * @param replyId 回复ID
     * @return 点赞数
     */
    Integer countLikes(@Param("replyId") Integer replyId);
}
