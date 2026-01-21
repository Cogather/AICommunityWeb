package com.aicommunity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子点赞Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface PostLikeMapper {

    /**
     * 查询用户是否已点赞帖子
     *
     * @param postId 帖子ID
     * @param userId  用户ID
     * @return 点赞记录数
     */
    Integer selectLikeCount(@Param("postId") String postId, @Param("userId") String userId);

    /**
     * 插入点赞记录
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int insertLike(@Param("postId") String postId, @Param("userId") String userId);

    /**
     * 删除点赞记录
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteLike(@Param("postId") String postId, @Param("userId") String userId);
}
