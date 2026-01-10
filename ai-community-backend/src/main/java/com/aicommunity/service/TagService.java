package com.aicommunity.service;

import com.aicommunity.dto.TagInfo;

import java.util.List;

/**
 * 标签服务接口
 *
 * @author AI Community Team
 */
public interface TagService {
    /**
     * 获取标签列表
     *
     * @param zone 专区
     * @param toolId 工具ID
     * @return 标签列表
     */
    List<TagInfo> getTags(String zone, Long toolId);
}
