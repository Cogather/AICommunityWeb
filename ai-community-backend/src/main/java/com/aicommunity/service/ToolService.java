package com.aicommunity.service;

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
    Object getTools(Boolean featured);

    /**
     * 获取工具详情
     *
     * @param id 工具ID
     * @return 工具详情
     */
    Object getToolDetail(Long id);

    /**
     * 检查用户是否为工具Owner
     *
     * @param id 工具ID
     * @return 是否为Owner
     */
    Object checkOwner(Long id);
}
