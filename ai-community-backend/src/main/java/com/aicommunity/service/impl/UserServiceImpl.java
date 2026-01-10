package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.UserProfileResponse;
import com.aicommunity.dto.UserPointsResponse;
import com.aicommunity.dto.UserUpdateRequest;
import com.aicommunity.entity.Activity;
import com.aicommunity.entity.Comment;
import com.aicommunity.entity.Post;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.*;
import com.aicommunity.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    private PostMapper postMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ActivityRegistrationMapper activityRegistrationMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private HonorMapper honorMapper;

    @Autowired
    private PointsMapper pointsMapper;

    @Override
    public UserProfileResponse getCurrentUser() {
        // TODO: 从SecurityContext或ThreadLocal获取当前用户ID
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        return getUserProfile(currentUserId, null);
    }

    @Override
    public UserPointsResponse getUserPoints() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        UserPointsResponse response = new UserPointsResponse();
        
        // 计算总积分
        Integer totalPoints = pointsMapper.calculateUserPoints(currentUserId);
        response.setPoints(totalPoints != null ? totalPoints : 0);

        // 计算各项积分明细
        UserPointsResponse.PointsBreakdown breakdown = new UserPointsResponse.PointsBreakdown();
        breakdown.setPosts(pointsMapper.calculatePointsByType(currentUserId, "post"));
        breakdown.setComments(pointsMapper.calculatePointsByType(currentUserId, "comment"));
        breakdown.setLikesReceived(pointsMapper.calculatePointsByType(currentUserId, "like_received"));
        breakdown.setFavoritesReceived(pointsMapper.calculatePointsByType(currentUserId, "favorite_received"));
        breakdown.setActivities(pointsMapper.calculatePointsByType(currentUserId, "activity"));

        response.setBreakdown(breakdown);
        return response;
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId, String name) {
        User user;
        if (userId != null) {
            user = userMapper.selectById(userId);
        } else if (name != null && !name.isEmpty()) {
            user = userMapper.selectByName(name);
        } else {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST, "用户ID或用户名不能同时为空");
        }

        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }

        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(user, response);

        // 统计数据
        response.setPostsCount(postMapper.countByAuthorId(user.getId()));
        response.setFavoritesCount(favoriteMapper.countByUserId(user.getId()));
        response.setCommentsCount(commentMapper.countByUserId(user.getId()));
        response.setActivitiesCount(activityRegistrationMapper.countByUserId(user.getId()));
        response.setFlowersCount(honorMapper.countFlowersByUserId(user.getId()));

        // 计算积分
        Integer points = pointsMapper.calculateUserPoints(user.getId());
        response.setPoints(points != null ? points : 0);

        // 获取用户角色
        List<String> roles = userRoleMapper.selectRolesByUserId(user.getId());
        response.setRoles(roles);

        // 获取拥有的工具列表
        if (roles.contains("owner")) {
            List<UserRoleMapper.OwnedTool> ownedTools = userRoleMapper.selectOwnedToolsByUserId(user.getId());
            List<UserProfileResponse.OwnedTool> toolList = ownedTools.stream()
                    .map(tool -> {
                        UserProfileResponse.OwnedTool ownedTool = new UserProfileResponse.OwnedTool();
                        ownedTool.setToolId(tool.getToolId());
                        ownedTool.setToolName(tool.getToolName());
                        return ownedTool;
                    })
                    .collect(Collectors.toList());
            response.setOwnedTools(toolList);
        } else {
            response.setOwnedTools(new ArrayList<>());
        }

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserProfile(UserUpdateRequest request) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        User user = userMapper.selectById(currentUserId);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        userMapper.updateById(user);
    }

    @Override
    public PageResult<Post> getUserPosts(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Post> posts = postMapper.selectByAuthorId(userId);
        PageInfo<Post> pageInfo = new PageInfo<>(posts);
        return PageResult.of(posts, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PageResult<Post> getUserFavorites(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Post> posts = favoriteMapper.selectPostsByUserId(userId);
        PageInfo<Post> pageInfo = new PageInfo<>(posts);
        return PageResult.of(posts, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PageResult<Comment> getUserComments(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Comment> comments = commentMapper.selectByUserId(userId);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        return PageResult.of(comments, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PageResult<Activity> getUserActivities(Long userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Activity> activities = activityMapper.selectByRegisteredUserId(userId);
        PageInfo<Activity> pageInfo = new PageInfo<>(activities);
        return PageResult.of(activities, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PageResult<Activity> getUserCreatedActivities(Long userId, String status, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Activity> activities = activityMapper.selectByAuthorId(userId, status);
        PageInfo<Activity> pageInfo = new PageInfo<>(activities);
        return PageResult.of(activities, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        return com.aicommunity.util.UserContext.getUserId();
    }
}
