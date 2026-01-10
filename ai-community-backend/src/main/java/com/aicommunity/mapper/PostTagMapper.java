package com.aicommunity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子标签关联Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface PostTagMapper {
    /**
     * 插入帖子标签关联
     */
    void insert(@Param("postId") Long postId, @Param("tagId") Long tagId);

    /**
     * 根据帖子ID删除标签关联
     */
    void deleteByPostId(@Param("postId") Long postId);
}
