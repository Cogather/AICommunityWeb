package com.aicommunity.mapper;

import com.aicommunity.vo.ZoneTagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专区标签Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface ZoneTagMapper {

    /**
     * 查询专区标签列表
     *
     * @param zoneId  专区ID
     * @param toolId  工具ID（可选）
     * @return 标签列表
     */
    List<ZoneTagVO> selectZoneTags(@Param("zoneId") Integer zoneId, @Param("toolId") Integer toolId);
}
