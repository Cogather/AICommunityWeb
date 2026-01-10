package com.aicommunity.mapper;

import com.aicommunity.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface FavoriteMapper {
    /**
     * 插入收藏记录
     */
    void insert(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 删除收藏记录
     */
    void deleteByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 检查是否存在收藏记录
     */
    boolean existsByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 根据用户ID查询收藏的帖子
     */
    List<Post> selectPostsByUserId(@Param("userId") Long userId);

    /**
     * 统计用户收藏数
     */
    Integer countByUserId(@Param("userId") Long userId);

    /**
     * 根据帖子ID删除收藏记录
     */
    void deleteByPostId(@Param("postId") Long postId);
}
