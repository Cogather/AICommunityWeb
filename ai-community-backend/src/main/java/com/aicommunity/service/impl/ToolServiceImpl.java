package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.entity.Tool;
import com.aicommunity.entity.ToolBanner;
import com.aicommunity.entity.UserRole;
import com.aicommunity.mapper.*;
import com.aicommunity.service.ToolService;
import com.aicommunity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工具服务实现类
 *
 * @author AI Community Team
 */
@Service
public class ToolServiceImpl implements ToolService {

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private ToolBannerMapper toolBannerMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Override
    public Object getTools(Boolean featured) {
        List<Map<String, Object>> tools = toolMapper.selectAll(featured);
        List<Map<String, Object>> result = tools.stream()
                .map(tool -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tool.get("id"));
                    map.put("name", tool.get("name"));
                    map.put("desc", tool.get("desc"));
                    map.put("logo", tool.get("logo"));
                    map.put("color", tool.get("color"));
                    map.put("link", tool.get("link"));

                    // 查询工具Banner
                    Long toolId = (Long) tool.get("id");
                    List<ToolBanner> banners = toolBannerMapper.selectByToolId(toolId);
                    List<Map<String, Object>> bannerList = banners.stream()
                            .map(banner -> {
                                Map<String, Object> bannerMap = new HashMap<>();
                                bannerMap.put("id", banner.getId());
                                bannerMap.put("image", banner.getImage());
                                bannerMap.put("title", banner.getTitle());
                                bannerMap.put("desc", banner.getDesc());
                                bannerMap.put("order", banner.getOrder());
                                return bannerMap;
                            })
                            .sorted(Comparator.comparing(m -> (Integer) m.get("order")))
                            .collect(Collectors.toList());
                    map.put("banners", bannerList);

                    return map;
                })
                .collect(Collectors.toList());

        return Map.of("list", result);
    }

    @Override
    public Object getToolDetail(Long id) {
        Tool tool = toolMapper.selectById(id);
        if (tool == null) {
            throw new BusinessException("工具不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", tool.getId());
        result.put("name", tool.getName());
        result.put("desc", tool.getDesc());
        result.put("logo", tool.getLogo());
        result.put("color", tool.getColor());
        result.put("link", tool.getLink());

        // 查询工具Banner
        List<ToolBanner> banners = toolBannerMapper.selectByToolId(id);
        List<Map<String, Object>> bannerList = banners.stream()
                .map(banner -> {
                    Map<String, Object> bannerMap = new HashMap<>();
                    bannerMap.put("id", banner.getId());
                    bannerMap.put("image", banner.getImage());
                    bannerMap.put("title", banner.getTitle());
                    bannerMap.put("desc", banner.getDesc());
                    bannerMap.put("order", banner.getOrder());
                    return bannerMap;
                })
                .sorted(Comparator.comparing(m -> (Integer) m.get("order")))
                .collect(Collectors.toList());
        result.put("banners", bannerList);

        return result;
    }

    @Override
    public Object checkOwner(Long id) {
        Long userId = getCurrentUserIdOrNull();
        boolean isOwner = false;
        String toolName = null;

        if (userId != null) {
            List<UserRole> ownerRoles = userRoleMapper.selectByUserIdAndRole(userId, "owner");
            isOwner = ownerRoles.stream()
                    .anyMatch(role -> role.getToolId() != null && role.getToolId().equals(id));

            if (isOwner) {
                Tool tool = toolMapper.selectById(id);
                if (tool != null) {
                    toolName = tool.getName();
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("isOwner", isOwner);
        result.put("toolId", id);
        result.put("toolName", toolName);
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
}
