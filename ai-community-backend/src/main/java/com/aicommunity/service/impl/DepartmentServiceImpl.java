package com.aicommunity.service.impl;

import com.aicommunity.dto.DepartmentStatsResponse;
import com.aicommunity.mapper.DepartmentMapper;
import com.aicommunity.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门服务实现类
 *
 * @author AI Community Team
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentStatsResponse> getDepartmentStats(String zone) {
        return departmentMapper.selectStats(zone);
    }
}
