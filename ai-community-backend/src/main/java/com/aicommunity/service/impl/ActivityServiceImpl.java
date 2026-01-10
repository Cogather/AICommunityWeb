package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.ActivityCreateRequest;
import com.aicommunity.dto.ActivityRegistrationResponse;
import com.aicommunity.dto.ActivityUpdateRequest;
import com.aicommunity.entity.Activity;
import com.aicommunity.mapper.*;
import com.aicommunity.service.ActivityService;
import com.aicommunity.service.PointsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.aicommunity.entity.User;
import com.aicommunity.entity.Tool;

/**
 * 活动服务实现类
 *
 * @author AI Community Team
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private ActivityRegistrationMapper activityRegistrationMapper;

    @Autowired
    private PointsService pointsService;

    @Autowired
    private ToolMapper toolMapper;

    @Override
    public PageResult<Activity> getActivities(Long toolId, String type, String status, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Activity> activities = activityMapper.selectActivities(toolId, type, status);
        
        for (Activity activity : activities) {
            fillActivityInfo(activity);
        }
        
        PageInfo<Activity> pageInfo = new PageInfo<>(activities);
        return PageResult.of(activities, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public Activity getActivityDetail(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException(ErrorCodeEnum.ACTIVITY_NOT_FOUND);
        }

        fillActivityInfo(activity);

        // 检查当前用户是否已报名
        Long currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            activity.setIsRegistered(activityRegistrationMapper.existsByUserAndActivity(currentUserId, id));
            activity.setIsAuthor(activity.getAuthorId().equals(currentUserId));
        }

        // 计算活动状态
        activity.setStatus(calculateActivityStatus(activity.getDate()));

        return activity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Activity createActivity(ActivityCreateRequest request) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Activity activity = new Activity();
        activity.setTitle(request.getTitle());
        activity.setContent(request.getContent());
        activity.setCover(request.getCover());
        activity.setToolId(request.getToolId());
        activity.setType(request.getType());
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            activity.setDate(sdf.parse(request.getDate()));
        } catch (ParseException e) {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST, "日期格式错误");
        }

        activity.setAuthorId(currentUserId);
        activity.setRegisteredCount(0);
        activity.setCreateTime(new Date());
        activity.setUpdateTime(new Date());

        activityMapper.insert(activity);
        return getActivityDetail(activity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Activity updateActivity(Long id, ActivityUpdateRequest request) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException(ErrorCodeEnum.ACTIVITY_NOT_FOUND);
        }

        Long currentUserId = getCurrentUserId();
        if (!activity.getAuthorId().equals(currentUserId) && !isAdmin(currentUserId)) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN);
        }

        activity.setTitle(request.getTitle());
        activity.setContent(request.getContent());
        activity.setCover(request.getCover());
        activity.setToolId(request.getToolId());
        activity.setType(request.getType());
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            activity.setDate(sdf.parse(request.getDate()));
        } catch (ParseException e) {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST, "日期格式错误");
        }

        activity.setUpdateTime(new Date());
        activityMapper.updateById(activity);
        return getActivityDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteActivity(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException(ErrorCodeEnum.ACTIVITY_NOT_FOUND);
        }

        Long currentUserId = getCurrentUserId();
        if (!activity.getAuthorId().equals(currentUserId) && !isAdmin(currentUserId)) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN);
        }

        // 删除报名记录
        activityRegistrationMapper.deleteByActivityId(id);
        // 删除活动
        activityMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityRegistrationResponse registerActivity(Long id) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException(ErrorCodeEnum.ACTIVITY_NOT_FOUND);
        }

        boolean exists = activityRegistrationMapper.existsByUserAndActivity(currentUserId, id);
        if (exists) {
            throw new BusinessException(ErrorCodeEnum.ALREADY_REGISTERED);
        }

        activityRegistrationMapper.insert(currentUserId, id);
        activityMapper.incrementRegisteredCount(id);

        // 计算积分（参加活动+10，管理员除外）
        if (!isAdmin(currentUserId)) {
            pointsService.addPoints(currentUserId, 10, "activity", id, "activity");
        }

        ActivityRegistrationResponse response = new ActivityRegistrationResponse();
        response.setSuccess(true);
        response.setMessage("报名成功");
        response.setIsRegistered(true);
        response.setRegisteredCount(activity.getRegisteredCount() + 1);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityRegistrationResponse cancelRegistration(Long id) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException(ErrorCodeEnum.ACTIVITY_NOT_FOUND);
        }

        boolean exists = activityRegistrationMapper.existsByUserAndActivity(currentUserId, id);
        if (!exists) {
            throw new BusinessException(ErrorCodeEnum.NOT_REGISTERED);
        }

        activityRegistrationMapper.deleteByUserAndActivity(currentUserId, id);
        activityMapper.decrementRegisteredCount(id);

        ActivityRegistrationResponse response = new ActivityRegistrationResponse();
        response.setSuccess(true);
        response.setMessage("取消报名成功");
        response.setIsRegistered(false);
        response.setRegisteredCount(Math.max(0, activity.getRegisteredCount() - 1));
        return response;
    }

    @Override
    public PageResult<ActivityRegistrationResponse.RegistrationUser> getRegistrations(Long id, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<ActivityRegistrationResponse.RegistrationUser> users = activityRegistrationMapper.selectUsersByActivityId(id);
        PageInfo<ActivityRegistrationResponse.RegistrationUser> pageInfo = new PageInfo<>(users);
        return PageResult.of(users, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    /**
     * 填充活动信息
     */
    private void fillActivityInfo(Activity activity) {
        // 填充发布者信息
        if (activity.getAuthorId() != null) {
            com.aicommunity.entity.User user = userMapper.selectById(activity.getAuthorId());
            if (user != null) {
                activity.setAuthorName(user.getName());
                activity.setAuthorAvatar(user.getAvatar());
            }
        }

        // 填充工具名称
        if (activity.getToolId() != null) {
            com.aicommunity.entity.Tool tool = toolMapper.selectById(activity.getToolId());
            if (tool != null) {
                activity.setToolName(tool.getName());
            }
        }
    }

    /**
     * 计算活动状态
     */
    private String calculateActivityStatus(Date date) {
        Date now = new Date();
        if (date.before(now)) {
            return "ended";
        } else if (date.equals(now) || (date.getTime() - now.getTime()) < 24 * 60 * 60 * 1000) {
            return "ongoing";
        } else {
            return "upcoming";
        }
    }

    @Autowired
    private com.aicommunity.mapper.UserRoleMapper userRoleMapper;

    private Long getCurrentUserId() {
        return com.aicommunity.util.UserContext.getUserId();
    }

    private boolean isAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        List<String> roles = userRoleMapper.selectRolesByUserId(userId);
        return roles != null && roles.contains("admin");
    }
}
