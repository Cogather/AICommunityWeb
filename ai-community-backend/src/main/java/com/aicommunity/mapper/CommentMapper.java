package com.aicommunity.mapper;

import com.aicommunity.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 评论Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface CommentMapper {

    /**
     * 根据帖子ID查询评论列表
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    List<Map<String, Object>> selectByPostId(@Param("postId") Long postId);

    /**
     * 根据ID查询评论
     *
     * @param id 评论ID
     * @return 评论
     */
    Comment selectById(@Param("id") Long id);

    /**
     * 插入评论
     *
     * @param comment 评论
     * @return 影响行数
     */
    int insert(Comment comment);

    /**
     * 更新评论
     *
     * @param comment 评论
     * @return 影响行数
     */
    int update(Comment comment);

    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新点赞数
     *
     * @param id    评论ID
     * @param delta 变化量
     * @return 影响行数
     */
    int updateLikes(@Param("id") Long id, @Param("delta") Integer delta);

    /**
     * 根据用户ID查询评论
     *
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Map<String, Object>> selectByUserId(@Param("userId") Long userId);

    /**
     * 统计用户评论数
     *
     * @param userId 用户ID
     * @return 评论数
     */
    int countByUserId(@Param("userId") Long userId);
}
