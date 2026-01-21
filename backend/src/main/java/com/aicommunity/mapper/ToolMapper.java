package com.aicommunity.mapper;

import com.aicommunity.entity.Tool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工具Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface ToolMapper {

    /**
     * 查询工具列表
     *
     * @param featured 是否只返回推荐工具
     * @return 工具列表
     */
    List<Tool> selectTools(@Param("featured") Boolean featured);
}
