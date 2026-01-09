package com.aicommunity.mapper;

import com.aicommunity.entity.ToolBanner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工具Banner Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ToolBannerMapper {

    /**
     * 根据工具ID查询Banner列表
     *
     * @param toolId 工具ID
     * @return Banner列表
     */
    List<ToolBanner> selectByToolId(@Param("toolId") Long toolId);
}
