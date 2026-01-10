package com.aicommunity.mapper;

import com.aicommunity.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 收藏Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 查询用户是否已收藏
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 收藏记录
     */
    Favorite selectByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 插入收藏记录
     *
     * @param favorite 收藏记录
     * @return 影响行数
     */
    int insert(Favorite favorite);

    /**
     * 删除收藏记录
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    int delete(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 根据用户ID查询收藏的帖子
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    List<Map<String, Object>> selectPostsByUserId(@Param("userId") Long userId);

    /**
     * 统计用户收藏数
     *
     * @param userId 用户ID
     * @return 收藏数
     */
    int countByUserId(@Param("userId") Long userId);
}
