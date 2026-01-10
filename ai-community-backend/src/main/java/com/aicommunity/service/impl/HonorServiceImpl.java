package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.HonorService;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 荣誉服务实现类
 *
 * @author AI Community Team
 */
@Service
public class HonorServiceImpl implements HonorService {

    @Autowired
    private HonorMapper honorMapper;

    @Autowired
    private HonorFlowerMapper honorFlowerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Override
    public PageResult<?> getHonors(String scope, String view, String user, String filterType,
                                   String filterValue, String search, PageQuery pageQuery) {
        Long currentUserId = getCurrentUserIdOrNull();

        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Map<String, Object>> list = honorMapper.selectByCondition(scope, view, user, filterType, filterValue, search, currentUserId);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

        // 如果是时光轴视图，需要按年份分组
        if ("timeline".equals(view)) {
            Map<String, List<Map<String, Object>>> groupedByYear = list.stream()
                    .collect(Collectors.groupingBy(m -> (String) m.get("year")));

            List<Map<String, Object>> timelineList = groupedByYear.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> yearGroup = new HashMap<>();
                        yearGroup.put("year", entry.getKey());
                        yearGroup.put("items", entry.getValue());
                        return yearGroup;
                    })
                    .sorted((a, b) -> ((String) b.get("year")).compareTo((String) a.get("year")))
                    .collect(Collectors.toList());

            return PageResult.of(timelineList, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
        }

        // 荣誉墙视图，判断当前用户是否已送花
        List<Map<String, Object>> resultList = list.stream()
                .map(map -> {
                    Long honorId = (Long) map.get("id");
                    if (currentUserId != null) {
                        HonorFlower flower = honorFlowerMapper.selectByHonorAndUser(honorId, currentUserId);
                        map.put("hasGivenFlower", flower != null);
                        map.put("isMine", currentUserId.equals(map.get("userId")));
                    } else {
                        map.put("hasGivenFlower", false);
                        map.put("isMine", false);
                    }
                    return map;
                })
                .collect(Collectors.toList());

        return PageResult.of(resultList, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public Object getHonorInfluence() {
        // 统计总荣誉数
        List<Map<String, Object>> allHonors = honorMapper.selectByCondition("all", "grid", null, null, null, null, null);
        int totalHonors = allHonors.size();

        // 统计总用户数（去重）
        Set<Long> userIds = allHonors.stream()
                .map(m -> (Long) m.get("userId"))
                .collect(Collectors.toSet());
        int totalUsers = userIds.size();

        // 统计总花朵数
        int totalFlowers = allHonors.stream()
                .mapToInt(m -> (Integer) m.getOrDefault("flowers", 0))
                .sum();

        // 按类别统计
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, String> categoryLabel = new HashMap<>();
        categoryLabel.put("innovation", "技术创新");
        categoryLabel.put("efficiency", "效能提升");
        categoryLabel.put("practice", "最佳实践");
        categoryLabel.put("community", "社区贡献");

        for (Map<String, Object> honor : allHonors) {
            String category = (String) honor.get("category");
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }

        List<Map<String, Object>> categories = categoryLabel.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("category", entry.getKey());
                    map.put("count", categoryCount.getOrDefault(entry.getKey(), 0));
                    map.put("label", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        // 统计Top部门
        Map<String, Integer> departmentCount = new HashMap<>();
        for (Map<String, Object> honor : allHonors) {
            String department = (String) honor.get("department");
            if (department != null) {
                departmentCount.put(department, departmentCount.getOrDefault(department, 0) + 1);
            }
        }

        List<Map<String, Object>> topDepartments = departmentCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", entry.getKey());
                    map.put("count", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("totalHonors", totalHonors);
        result.put("totalUsers", totalUsers);
        result.put("totalFlowers", totalFlowers);
        result.put("categories", categories);
        result.put("topDepartments", topDepartments);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object giveFlower(Long id) {
        Honor honor = honorMapper.selectById(id);
        if (honor == null) {
            throw new BusinessException("荣誉不存在");
        }

        Long userId = getCurrentUserId();

        // 检查是否已送花
        HonorFlower existing = honorFlowerMapper.selectByHonorAndUser(id, userId);
        if (existing != null) {
            throw new BusinessException("您已经送过花了");
        }

        // 插入送花记录
        HonorFlower flower = new HonorFlower();
        flower.setHonorId(id);
        flower.setUserId(userId);
        flower.setCreateTime(LocalDateTime.now());
        honorFlowerMapper.insert(flower);

        // 更新荣誉花朵数
        honorMapper.updateFlowers(id, 1);

        Honor updatedHonor = honorMapper.selectById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("flowers", updatedHonor.getFlowers());
        result.put("hasGivenFlower", true);

        return result;
    }

    /**
     * 获取当前用户ID（可为null）
     */
    private Long getCurrentUserIdOrNull() {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                return jwtUtil.getUserIdFromToken(token);
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return null;
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new BusinessException(401, "未授权，请先登录");
    }
}
