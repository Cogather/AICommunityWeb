package com.aicommunity.service.impl;

import com.aicommunity.mapper.PostMapper;
import com.aicommunity.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 *
 * @author AI Community Team
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public Object getStats(String zone) {
        // 查询部门统计（需要关联用户表查询部门）
        List<Map<String, Object>> stats = postMapper.selectDepartmentStats(zone);

        List<Map<String, Object>> list = stats.stream()
                .map(stat -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", stat.get("id"));
                    map.put("name", stat.get("name"));
                    map.put("postCount", stat.get("postCount"));
                    map.put("contributorCount", stat.get("contributorCount"));
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of("list", list);
    }
}
