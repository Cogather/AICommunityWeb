package com.aicommunity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评论点赞Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface CommentLikeMapper {

    /**
     * 查询用户是否已点赞评论
     *
     * @param commentId 评论ID
     * @param userId    用户ID
     * @return 点赞记录数
     */
    Integer selectLikeCount(@Param("commentId") Integer commentId, @Param("userId") String userId);

    /**
     * 插入点赞记录
     *
     * @param commentId 评论ID
     * @param userId    用户ID
     * @return 影响行数
     */
    int insertLike(@Param("commentId") Integer commentId, @Param("userId") String userId);

    /**
     * 删除点赞记录
     *
     * @param commentId 评论ID
     * @param userId    用户ID
     * @return 影响行数
     */
    int deleteLike(@Param("commentId") Integer commentId, @Param("userId") String userId);
}
