package com.aicommunity.mapper;

import com.aicommunity.entity.Tool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工具Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ToolMapper {

    /**
     * 查询工具列表
     *
     * @param featured 是否只获取推荐工具
     * @return 工具列表
     */
    List<Map<String, Object>> selectAll(@Param("featured") Boolean featured);

    /**
     * 根据ID查询工具
     *
     * @param id 工具ID
     * @return 工具
     */
    Tool selectById(@Param("id") Long id);

    /**
     * 插入工具
     *
     * @param tool 工具
     * @return 影响行数
     */
    int insert(Tool tool);

    /**
     * 更新工具
     *
     * @param tool 工具
     * @return 影响行数
     */
    int update(Tool tool);
}
