package com.aicommunity.mapper;

import com.aicommunity.dto.DepartmentStatsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface DepartmentMapper {
    /**
     * 查询部门统计
     */
    List<DepartmentStatsResponse> selectStats(@Param("zone") String zone);
}
