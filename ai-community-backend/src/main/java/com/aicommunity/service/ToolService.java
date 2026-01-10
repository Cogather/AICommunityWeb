package com.aicommunity.service;

import com.aicommunity.dto.ToolOwnerResponse;
import com.aicommunity.entity.Tool;

import java.util.List;

/**
 * 工具服务接口
 *
 * @author AI Community Team
 */
public interface ToolService {
    /**
     * 获取工具列表
     *
     * @param featured 是否只获取推荐工具
     * @return 工具列表
     */
    List<Tool> getTools(Boolean featured);

    /**
     * 获取工具详情
     *
     * @param id 工具ID
     * @return 工具详情
     */
    Tool getToolDetail(Long id);

    /**
     * 检查用户是否为工具Owner
     *
     * @param id 工具ID
     * @return Owner响应
     */
    ToolOwnerResponse checkToolOwner(Long id);
}
