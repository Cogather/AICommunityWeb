package com.aicommunity.mapper;

import com.aicommunity.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface CommentMapper {

    /**
     * 根据帖子ID查询评论列表
     *
     * @param postId  帖子ID
     * @param sortBy  排序方式：newest-最新，hot-最热
     * @param offset  偏移量
     * @param pageSize 每页数量
     * @return 评论列表
     */
    List<Comment> selectCommentsByPostId(@Param("postId") String postId,
                                         @Param("sortBy") String sortBy,
                                         @Param("offset") Integer offset,
                                         @Param("pageSize") Integer pageSize);

    /**
     * 统计帖子评论总数
     *
     * @param postId 帖子ID
     * @return 评论总数
     */
    Long countCommentsByPostId(@Param("postId") String postId);

    /**
     * 根据ID查询评论
     *
     * @param id 评论ID
     * @return 评论信息
     */
    Comment selectById(@Param("id") Integer id);

    /**
     * 插入评论
     *
     * @param comment 评论信息
     * @return 影响行数
     */
    int insertComment(Comment comment);

    /**
     * 更新评论
     *
     * @param comment 评论信息
     * @return 影响行数
     */
    int updateComment(Comment comment);

    /**
     * 删除评论（逻辑删除）
     *
     * @param id 评论ID
     * @return 影响行数
     */
    int deleteComment(@Param("id") Integer id);

    /**
     * 查询评论点赞数
     *
     * @param commentId 评论ID
     * @return 点赞数
     */
    Integer countLikes(@Param("commentId") Integer commentId);

    /**
     * 查询用户发表的评论列表（分页）
     *
     * @param userId   用户ID
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 评论列表
     */
    List<Comment> selectUserComments(@Param("userId") String userId,
                                     @Param("offset") Integer offset,
                                     @Param("pageSize") Integer pageSize);

    /**
     * 统计用户发表的评论总数
     *
     * @param userId 用户ID
     * @return 评论总数
     */
    Long countUserComments(@Param("userId") String userId);
}
