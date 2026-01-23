package com.aicommunity.mapper;

import com.aicommunity.vo.PostCollectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子收藏Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface PostCollectMapper {

    /**
     * 查询用户是否已收藏帖子
     *
     * @param postId 帖子ID
     * @param userId  用户ID
     * @return 收藏记录数
     */
    Integer selectCollectCount(@Param("postId") String postId, @Param("userId") String userId);

    /**
     * 插入收藏记录
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int insertCollect(@Param("postId") String postId, @Param("userId") String userId);

    /**
     * 删除收藏记录
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteCollect(@Param("postId") String postId, @Param("userId") String userId);

    /**
     * 查询用户收藏的帖子列表（分页）
     *
     * @param userId   用户ID
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 帖子ID列表（带收藏时间）
     */
    List<PostCollectVO> selectUserFavorites(@Param("userId") String userId,
                                           @Param("offset") Integer offset,
                                           @Param("pageSize") Integer pageSize);

    /**
     * 统计用户收藏的帖子总数
     *
     * @param userId 用户ID
     * @return 收藏总数
     */
    Long countUserFavorites(@Param("userId") String userId);
}
