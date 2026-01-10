package com.aicommunity.service;

import com.aicommunity.dto.AgentFeaturedPostResponse;

/**
 * 扶摇Agent服务接口
 *
 * @author AI Community Team
 */
public interface AgentService {
    /**
     * 获取扶摇Agent应用置顶帖子
     *
     * @return 置顶帖子响应
     */
    AgentFeaturedPostResponse getFeaturedPost();
}
