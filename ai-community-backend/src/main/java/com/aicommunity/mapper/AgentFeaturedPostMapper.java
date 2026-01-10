package com.aicommunity.mapper;

import com.aicommunity.dto.AgentFeaturedPostResponse;
import org.apache.ibatis.annotations.Mapper;

/**
 * 扶摇Agent置顶帖子Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface AgentFeaturedPostMapper {
    /**
     * 查询置顶帖子
     */
    AgentFeaturedPostResponse selectFeaturedPost();

    /**
     * 更新置顶帖子
     */
    void updateFeaturedPost(AgentFeaturedPostResponse post);
}
