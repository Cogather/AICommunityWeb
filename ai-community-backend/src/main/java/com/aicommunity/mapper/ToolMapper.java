package com.aicommunity.mapper;

import com.aicommunity.entity.Tool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工具Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ToolMapper {
    /**
     * 根据ID查询工具
     */
    Tool selectById(@Param("id") Long id);

    /**
     * 查询工具列表
     */
    List<Tool> selectAll(@Param("featured") Boolean featured);

    /**
     * 查询工具列表（包含Banner）
     */
    List<com.aicommunity.dto.ToolsConfigResponse.ToolItem> selectAllWithBanners();

    /**
     * 更新工具
     */
    void updateById(com.aicommunity.entity.Tool tool);
}
