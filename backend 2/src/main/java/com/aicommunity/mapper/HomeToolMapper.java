package com.aicommunity.mapper;

import com.aicommunity.entity.HomeTool;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 首页工具Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface HomeToolMapper {

    /**
     * 查询所有工具，按order排序
     *
     * @return 工具列表
     */
    List<HomeTool> selectAllOrderByOrder();

    /**
     * 删除所有工具配置
     *
     * @return 影响行数
     */
    int deleteAll();

    /**
     * 插入工具配置
     *
     * @param tool 工具配置
     * @return 影响行数
     */
    int insert(HomeTool tool);
}
