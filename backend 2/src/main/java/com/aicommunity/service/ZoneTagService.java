package com.aicommunity.service;

import com.aicommunity.vo.ZoneTagListVO;

/**
 * 专区标签服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface ZoneTagService {

    /**
     * 获取专区已有标签
     *
     * @param zone   专区类型：practices/tools/agent/empowerment
     * @param toolId 工具ID（可选）
     * @return 标签列表
     */
    ZoneTagListVO getZoneTags(String zone, Integer toolId);
}
