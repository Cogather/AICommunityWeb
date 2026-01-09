package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.ActivityController;
import com.aicommunity.dto.ActivityDetailDTO;
import com.aicommunity.dto.ActivityListDTO;
import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.ActivityService;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    private ActivityRegistrationMapper activityRegistrationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Override
    public PageResult<ActivityListDTO> getActivities(Long toolId, String type, String status, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Map<String, Object>> list = activityMapper.selectByCondition(toolId, type, status);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

        // 转换为DTO
        List<ActivityListDTO> dtoList = list.stream()
                .map(this::convertToActivityListDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtoList, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public ActivityDetailDTO getActivityDetail(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        // 查询发布者信息
        User author = userMapper.selectById(activity.getAuthorId());
        if (author == null) {
            throw new BusinessException("发布者不存在");
        }

        // 查询工具信息
        String toolName = null;
        if (activity.getToolId() != null) {
            Tool tool = toolMapper.selectById(activity.getToolId());
            if (tool != null) {
                toolName = tool.getName();
            }
        }

        // 构建DTO
        ActivityDetailDTO dto = new ActivityDetailDTO();
        dto.setId(activity.getId());
        dto.setTitle(activity.getTitle());
        dto.setToolId(activity.getToolId());
        dto.setToolName(toolName);
        dto.setType(activity.getType());
        dto.setDate(activity.getDate().toString());
        dto.setCover(activity.getCover());
        dto.setContent(activity.getContent());
        dto.setAuthorId(activity.getAuthorId());
        dto.setAuthorName(author.getName());
        dto.setAuthorAvatar(author.getAvatar());
        dto.setCreateTime(activity.getCreateTime().toString());
        dto.setUpdateTime(activity.getUpdateTime() != null ? activity.getUpdateTime().toString() : null);
        dto.setStatus(activity.getStatus());
        dto.setRegisteredCount(activity.getRegisteredCount());

        // 判断当前用户状态
        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId != null) {
            // 判断是否已报名
            ActivityRegistration registration = activityRegistrationMapper.selectByActivityAndUser(id, currentUserId);
            dto.setIsRegistered(registration != null);

            // 判断是否是发布者
            dto.setIsAuthor(currentUserId.equals(activity.getAuthorId()));

            // 判断权限
            boolean isAdmin = isAdmin(currentUserId);
            dto.setCanEdit(dto.getIsAuthor() || isAdmin);
            dto.setCanDelete(dto.getIsAuthor() || isAdmin);
        } else {
            dto.setIsRegistered(false);
            dto.setIsAuthor(false);
            dto.setCanEdit(false);
            dto.setCanDelete(false);
        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityController.CreateActivityResponse createActivity(ActivityController.CreateActivityRequest request) {
        Long userId = getCurrentUserId();

        // 验证权限（工具Owner或管理员）
        if (request.getToolId() != null && request.getToolId() != -1) {
            // 检查是否是工具Owner
            List<UserRole> ownerRoles = userRoleMapper.selectByUserIdAndRole(userId, "owner");
            boolean isOwner = ownerRoles.stream()
                    .anyMatch(role -> role.getToolId() != null && role.getToolId().equals(request.getToolId()));
            if (!isOwner && !isAdmin(userId)) {
                throw new BusinessException(403, "只有工具Owner或管理员可以发布活动");
            }
        }

        // 创建活动
        Activity activity = new Activity();
        activity.setTitle(request.getTitle());
        activity.setToolId(request.getToolId());
        activity.setType(request.getType());
        activity.setDate(LocalDate.parse(request.getDate()));
        activity.setCover(request.getCover());
        activity.setContent(request.getContent());
        activity.setAuthorId(userId);
        activity.setStatus(calculateStatus(LocalDate.parse(request.getDate())));
        activity.setRegisteredCount(0);
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());

        activityMapper.insert(activity);

        return new ActivityController.CreateActivityResponse(activity.getId(), "活动发布成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityController.UpdateActivityResponse updateActivity(Long id, ActivityController.CreateActivityRequest request) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        Long userId = getCurrentUserId();
        // 验证权限（发布者或管理员）
        if (!activity.getAuthorId().equals(userId) && !isAdmin(userId)) {
            throw new BusinessException(403, "无权限编辑此活动");
        }

        // 更新活动
        activity.setTitle(request.getTitle());
        activity.setToolId(request.getToolId());
        activity.setType(request.getType());
        activity.setDate(LocalDate.parse(request.getDate()));
        activity.setCover(request.getCover());
        activity.setContent(request.getContent());
        activity.setStatus(calculateStatus(LocalDate.parse(request.getDate())));
        activity.setUpdateTime(LocalDateTime.now());
        activityMapper.update(activity);

        return new ActivityController.UpdateActivityResponse(id, "活动更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteActivity(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        Long userId = getCurrentUserId();
        // 验证权限（发布者或管理员）
        if (!activity.getAuthorId().equals(userId) && !isAdmin(userId)) {
            throw new BusinessException(403, "无权限删除此活动");
        }

        // 删除活动（级联删除报名记录）
        activityMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityController.RegisterActivityResponse registerActivity(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        Long userId = getCurrentUserId();

        // 检查是否已报名
        ActivityRegistration existing = activityRegistrationMapper.selectByActivityAndUser(id, userId);
        if (existing != null) {
            throw new BusinessException("您已经报名此活动");
        }

        // 创建报名记录
        ActivityRegistration registration = new ActivityRegistration();
        registration.setActivityId(id);
        registration.setUserId(userId);
        registration.setCreateTime(LocalDateTime.now());
        activityRegistrationMapper.insert(registration);

        // 更新活动报名人数
        activityMapper.updateRegisteredCount(id, 1);

        // 计算积分（参加活动+10，管理员除外）
        if (!isAdmin(userId)) {
            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            PointsRecord record = new PointsRecord();
            record.setUserId(userId);
            record.setPoints(10);
            record.setType("activity");
            record.setTargetId(id);
            record.setMonth(currentMonth);
            record.setCreateTime(LocalDateTime.now());
            pointsRecordMapper.insert(record);
        }

        // 发送消息通知
        if (!activity.getAuthorId().equals(userId)) {
            User user = userMapper.selectById(userId);
            createMessage(activity.getAuthorId(), "activity_registration", "活动报名",
                user.getName() + "报名了你的活动", "/activity/" + id, userId);
        }

        Activity updatedActivity = activityMapper.selectById(id);
        return new ActivityController.RegisterActivityResponse(true, "报名成功", true, updatedActivity.getRegisteredCount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityController.CancelRegistrationResponse cancelRegistration(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        Long userId = getCurrentUserId();

        // 检查是否已报名
        ActivityRegistration registration = activityRegistrationMapper.selectByActivityAndUser(id, userId);
        if (registration == null) {
            throw new BusinessException("您尚未报名此活动");
        }

        // 删除报名记录
        activityRegistrationMapper.delete(id, userId);

        // 更新活动报名人数
        activityMapper.updateRegisteredCount(id, -1);

        Activity updatedActivity = activityMapper.selectById(id);
        return new ActivityController.CancelRegistrationResponse(true, "取消报名成功", false, updatedActivity.getRegisteredCount());
    }

    @Override
    public PageResult<Map<String, Object>> getRegistrations(Long id, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Map<String, Object>> list = activityRegistrationMapper.selectUsersByActivityId(id);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return PageResult.of(list, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    /**
     * 转换为ActivityListDTO
     */
    private ActivityListDTO convertToActivityListDTO(Map<String, Object> map) {
        ActivityListDTO dto = new ActivityListDTO();
        dto.setId((Long) map.get("id"));
        dto.setTitle((String) map.get("title"));
        dto.setToolId((Long) map.get("toolId"));
        dto.setToolName((String) map.get("toolName"));
        dto.setType((String) map.get("type"));
        dto.setDate(map.get("date") != null ? map.get("date").toString() : null);
        dto.setCover((String) map.get("cover"));
        dto.setDescription((String) map.get("description"));
        dto.setStatus((String) map.get("status"));
        dto.setRegisteredCount((Integer) map.get("registeredCount"));
        return dto;
    }

    /**
     * 计算活动状态
     */
    private String calculateStatus(LocalDate activityDate) {
        LocalDate today = LocalDate.now();
        if (activityDate.isBefore(today)) {
            return "ended";
        } else if (activityDate.isEqual(today)) {
            return "ongoing";
        } else {
            return "upcoming";
        }
    }

    /**
     * 创建消息
     */
    private void createMessage(Long userId, String type, String title, String content, String link, Long fromUserId) {
        Message message = new Message();
        message.setUserId(userId);
        message.setType(type);
        message.setTitle(title);
        message.setContent(content);
        message.setLink(link);
        message.setFromUserId(fromUserId);
        message.setRead(false);
        message.setCreateTime(LocalDateTime.now());
        messageMapper.insert(message);
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

    /**
     * 获取当前用户ID（可为null）
     */
    private Long getCurrentUserIdOrNull() {
        try {
            return getCurrentUserId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否是管理员
     */
    private boolean isAdmin(Long userId) {
        List<String> roles = userRoleMapper.selectRolesByUserId(userId);
        return roles != null && roles.contains("admin");
    }
}
