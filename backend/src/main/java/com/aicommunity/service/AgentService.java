package com.aicommunity.service;

import com.aicommunity.vo.AgentFeaturedPostResponseVO;
import com.aicommunity.vo.AgentFeaturedPostSetResponseVO;

/**
 * 扶摇Agent应用服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface AgentService {

    /**
     * 获取置顶/精选帖子
     * 获取扶摇Agent应用页面的置顶/精选帖子
     *
     * @return 置顶帖子响应
     */
    AgentFeaturedPostResponseVO getFeaturedPost();

    /**
     * 设置置顶帖子
     * Owner或管理员设置/取消置顶帖子
     *
     * @param postId 帖子ID，设置为null或0表示取消置顶
     * @param userId 当前用户ID
     * @return 设置结果
     */
    AgentFeaturedPostSetResponseVO setFeaturedPost(Integer postId, String userId);
}
