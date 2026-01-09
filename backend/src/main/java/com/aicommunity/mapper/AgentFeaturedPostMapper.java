package com.aicommunity.mapper;

import com.aicommunity.entity.AgentFeaturedPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 扶摇Agent应用置顶帖子Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface AgentFeaturedPostMapper {

    /**
     * 查询置顶帖子（只有一条记录）
     *
     * @return 置顶帖子
     */
    AgentFeaturedPost selectOne();

    /**
     * 插入置顶帖子
     *
     * @param featuredPost 置顶帖子
     * @return 影响行数
     */
    int insert(AgentFeaturedPost featuredPost);

    /**
     * 更新置顶帖子
     *
     * @param featuredPost 置顶帖子
     * @return 影响行数
     */
    int update(AgentFeaturedPost featuredPost);
}
