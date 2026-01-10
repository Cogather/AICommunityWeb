package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.UserController;
import com.aicommunity.dto.UserProfileDTO;
import com.aicommunity.dto.UserPointsDTO;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.*;
import com.aicommunity.service.UserService;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author AI Community Team
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Autowired
    private HonorMapper honorMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Override
    public UserProfileDTO getCurrentUser() {
        Long userId = getCurrentUserId();
        return buildUserProfile(userId);
    }

    @Override
    public UserPointsDTO calculatePoints() {
        Long userId = getCurrentUserId();
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 查询本月积分记录
        List<Map<String, Object>> records = pointsRecordMapper.selectByUserIdAndMonth(userId, currentMonth);

        // 按类型分组统计
        Map<String, Integer> breakdown = new HashMap<>();
        breakdown.put("posts", 0);
        breakdown.put("comments", 0);
        breakdown.put("likesReceived", 0);
        breakdown.put("favoritesReceived", 0);
        breakdown.put("activities", 0);

        int totalPoints = 0;
        for (Map<String, Object> record : records) {
            String type = (String) record.get("type");
            Integer points = (Integer) record.get("points");
            totalPoints += points;

            switch (type) {
                case "post":
                    breakdown.put("posts", breakdown.get("posts") + points);
                    break;
                case "comment":
                    breakdown.put("comments", breakdown.get("comments") + points);
                    break;
                case "like_received":
                    breakdown.put("likesReceived", breakdown.get("likesReceived") + points);
                    break;
                case "favorite_received":
                    breakdown.put("favoritesReceived", breakdown.get("favoritesReceived") + points);
                    break;
                case "activity":
                    breakdown.put("activities", breakdown.get("activities") + points);
                    break;
            }
        }

        UserPointsDTO dto = new UserPointsDTO();
        dto.setPoints(totalPoints);

        UserPointsDTO.PointsBreakdown pointsBreakdown = new UserPointsDTO.PointsBreakdown();
        pointsBreakdown.setPosts(breakdown.get("posts"));
        pointsBreakdown.setComments(breakdown.get("comments"));
        pointsBreakdown.setLikesReceived(breakdown.get("likesReceived"));
        pointsBreakdown.setFavoritesReceived(breakdown.get("favoritesReceived"));
        pointsBreakdown.setActivities(breakdown.get("activities"));

        dto.setBreakdown(pointsBreakdown);
        return dto;
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId, String name) {
        User user;
        if (userId != null) {
            user = userMapper.selectById(userId);
        } else if (StringUtils.hasText(name)) {
            user = userMapper.selectByName(name);
        } else {
            throw new BusinessException("用户ID或用户名不能同时为空");
        }

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return buildUserProfile(user.getId());
    }

    @Override
    public void updateProfile(UserController.UpdateProfileRequest request) {
        Long userId = getCurrentUserId();
        User user = new User();
        user.setId(userId);
        user.setBio(request.getBio());
        user.setAvatar(request.getAvatar());
        userMapper.update(user);
    }

    @Override
    public PageResult<?> getUserPosts(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<?> list = postMapper.selectByAuthorId(userId);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        return PageResult.of(list, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PageResult<?> getUserFavorites(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<?> list = favoriteMapper.selectPostsByUserId(userId);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        return PageResult.of(list, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PageResult<?> getUserComments(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<?> list = commentMapper.selectByUserId(userId);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        return PageResult.of(list, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PageResult<?> getUserActivities(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<?> list = activityMapper.selectByUserId(userId);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        return PageResult.of(list, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PageResult<?> getUserCreatedActivities(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<?> list = activityMapper.selectByAuthorId(userId);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        return PageResult.of(list, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public Object getTopContributors(String zone, Integer limit) {
        List<Map<String, Object>> contributors = postMapper.selectTopContributors(zone, limit);
        return Map.of("list", contributors);
    }

    /**
     * 构建用户个人资料
     *
     * @param userId 用户ID
     * @return 用户个人资料
     */
    private UserProfileDTO buildUserProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        dto.setBio(user.getBio());
        dto.setDepartment(user.getDepartment());

        // 统计数据
        dto.setPostsCount(postMapper.countByAuthorId(userId));
        dto.setFavoritesCount(favoriteMapper.countByUserId(userId));
        dto.setCommentsCount(commentMapper.countByUserId(userId));
        dto.setActivitiesCount(activityMapper.countByUserId(userId));

        // 计算积分
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        Integer points = pointsRecordMapper.sumPointsByUserIdAndMonth(userId, currentMonth);
        dto.setPoints(points != null ? points : 0);

        // 查询用户角色
        List<String> roles = userRoleMapper.selectRolesByUserId(userId);
        dto.setRoles(roles);

        // 查询拥有的工具
        List<UserRole> ownerRoles = userRoleMapper.selectByUserIdAndRole(userId, "owner");
        List<UserProfileDTO.OwnedTool> ownedTools = ownerRoles.stream()
                .map(role -> {
                    UserProfileDTO.OwnedTool tool = new UserProfileDTO.OwnedTool();
                    tool.setToolId(role.getToolId());
                    // TODO: 查询工具名称
                    return tool;
                })
                .collect(Collectors.toList());
        dto.setOwnedTools(ownedTools);

        // 计算花朵数
        Integer flowers = honorMapper.sumFlowersByUserId(userId);
        dto.setFlowersCount(flowers != null ? flowers : 0);

        return dto;
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
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
