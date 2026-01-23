package com.aicommunity.mapper;

import com.aicommunity.entity.ToolBanner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工具Banner Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface ToolBannerMapper {

    /**
     * 查询工具Banner列表
     *
     * @param toolId 工具ID（可选）
     * @return Banner列表
     */
    List<ToolBanner> selectToolBanners(@Param("toolId") Long toolId);

    /**
     * 删除所有工具Banner配置
     *
     * @return 影响行数
     */
    int deleteAll();

    /**
     * 插入工具Banner配置
     *
     * @param banner Banner配置
     * @return 影响行数
     */
    int insert(ToolBanner banner);
}
