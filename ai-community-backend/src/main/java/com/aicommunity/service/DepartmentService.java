package com.aicommunity.service;

import com.aicommunity.dto.DepartmentStatsResponse;

import java.util.List;

/**
 * 部门服务接口
 *
 * @author AI Community Team
 */
public interface DepartmentService {
    /**
     * 获取部门统计
     *
     * @param zone 专区
     * @return 部门统计列表
     */
    List<DepartmentStatsResponse> getDepartmentStats(String zone);
}
