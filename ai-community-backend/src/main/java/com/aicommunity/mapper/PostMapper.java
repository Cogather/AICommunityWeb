package com.aicommunity.mapper;

import com.aicommunity.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface PostMapper {
    /**
     * 根据ID查询帖子
     */
    Post selectById(@Param("id") Long id);

    /**
     * 查询帖子列表（支持筛选、排序、搜索）
     */
    List<Post> selectPosts(@Param("zone") String zone, 
                           @Param("toolId") Long toolId,
                           @Param("tag") String tag,
                           @Param("department") String department,
                           @Param("author") String author,
                           @Param("sort") String sort,
                           @Param("search") String search);

    /**
     * 插入帖子
     */
    void insert(Post post);

    /**
     * 更新帖子
     */
    void updateById(Post post);

    /**
     * 删除帖子
     */
    void deleteById(@Param("id") Long id);

    /**
     * 根据作者ID查询帖子
     */
    List<Post> selectByAuthorId(@Param("authorId") Long authorId);

    /**
     * 统计作者发布的帖子数
     */
    Integer countByAuthorId(@Param("authorId") Long authorId);

    /**
     * 增加浏览量
     */
    void incrementViews(@Param("id") Long id);

    /**
     * 增加点赞数
     */
    void incrementLikes(@Param("id") Long id);

    /**
     * 减少点赞数
     */
    void decrementLikes(@Param("id") Long id);

    /**
     * 增加评论数
     */
    void incrementComments(@Param("id") Long id);

    /**
     * 查询最热帖子
     */
    List<Post> selectHotPosts(@Param("zone") String zone, @Param("limit") Integer limit);
}
