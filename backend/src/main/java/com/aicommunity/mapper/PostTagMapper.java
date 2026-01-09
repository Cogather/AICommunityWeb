package com.aicommunity.mapper;

import com.aicommunity.entity.PostTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子标签Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface PostTagMapper {

    /**
     * 批量插入标签
     *
     * @param tags 标签列表
     * @return 影响行数
     */
    int insertBatch(@Param("tags") List<PostTag> tags);

    /**
     * 根据帖子ID删除标签
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int deleteByPostId(@Param("postId") Long postId);

    /**
     * 根据帖子ID查询标签
     *
     * @param postId 帖子ID
     * @return 标签列表
     */
    List<String> selectTagsByPostId(@Param("postId") Long postId);

    /**
     * 根据专区和工具查询标签
     *
     * @param zone   专区
     * @param toolId 工具ID
     * @return 标签列表
     */
    List<String> selectTagsByZoneAndTool(@Param("zone") String zone, @Param("toolId") Long toolId);
}
