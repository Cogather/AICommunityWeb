package com.aicommunity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 工具Banner Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ToolBannerMapper {
    /**
     * 根据工具ID删除Banner
     */
    void deleteByToolId(@Param("toolId") Long toolId);

    /**
     * 插入Banner
     */
    void insert(com.aicommunity.dto.ToolsConfigResponse.BannerItem item);

    /**
     * 设置工具ID（用于插入时）
     */
    default void setToolId(com.aicommunity.dto.ToolsConfigResponse.BannerItem item, Long toolId) {
        // 这个方法在XML中通过#{toolId}参数传递
    }
}
