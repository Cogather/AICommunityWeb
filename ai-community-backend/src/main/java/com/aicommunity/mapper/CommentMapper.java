package com.aicommunity.mapper;

import com.aicommunity.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface CommentMapper {
    /**
     * 根据ID查询评论
     */
    Comment selectById(@Param("id") Long id);

    /**
     * 根据帖子ID查询评论列表
     */
    List<Comment> selectByPostId(@Param("postId") Long postId);

    /**
     * 根据用户ID查询评论列表
     */
    List<Comment> selectByUserId(@Param("userId") Long userId);

    /**
     * 统计用户评论数
     */
    Integer countByUserId(@Param("userId") Long userId);

    /**
     * 插入评论
     */
    void insert(Comment comment);

    /**
     * 更新评论
     */
    void updateById(Comment comment);

    /**
     * 删除评论
     */
    void deleteById(@Param("id") Long id);

    /**
     * 根据帖子ID删除评论
     */
    void deleteByPostId(@Param("postId") Long postId);

    /**
     * 增加点赞数
     */
    void incrementLikes(@Param("id") Long id);

    /**
     * 减少点赞数
     */
    void decrementLikes(@Param("id") Long id);
}
