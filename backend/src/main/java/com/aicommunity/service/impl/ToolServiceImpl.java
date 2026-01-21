package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.entity.*;
import com.aicommunity.mapper.PostMapper;
import com.aicommunity.mapper.ToolActivityMapper;
import com.aicommunity.mapper.ToolMapper;
import com.aicommunity.mapper.ToolPostMapper;
import com.aicommunity.mapper.UserInfoMapper;
import com.aicommunity.service.ToolService;
import com.aicommunity.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工具服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class ToolServiceImpl implements ToolService {

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private ToolPostMapper toolPostMapper;

    @Autowired
    private ToolActivityMapper toolActivityMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PostMapper postMapper;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final int FRONTEND_AGENT_TOOL_ID = -1;
    private static final int AGENT_TOOL_ID = 3;

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public List<ToolItemVO> getToolList(Boolean featured) {
        List<Tool> tools = toolMapper.selectTools(featured);
        if (CollectionUtils.isEmpty(tools)) {
            return new ArrayList<>();
        }
        return tools.stream().map(tool -> {
            ToolItemVO vo = new ToolItemVO();
            vo.setId(tool.getId().intValue());
            // 字段映射：数据库title字段映射到前端name字段
            vo.setName(tool.getTitle());
            vo.setDesc(tool.getDesc());
            // 字段映射：数据库image字段映射到前端logo字段
            vo.setLogo(tool.getImage());
            // 使用数据库color字段，如果为null则使用默认值
            vo.setColor(tool.getColor() != null && !tool.getColor().isEmpty() 
                ? tool.getColor() : "#409eff");
            vo.setLink("/tools?toolId=" + tool.getToolId());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public ToolPostListVO getToolPostList(Integer toolId, String category, String tag,
                                         String department, String keyword, String sortBy,
                                         Integer page, Integer pageSize) {

        if (toolId != null && toolId == FRONTEND_AGENT_TOOL_ID) {
            toolId = AGENT_TOOL_ID;
        }

        // 参数校验和默认值设置
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "newest";
        }

        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询帖子列表
        List<ToolPostItemVO> posts = toolPostMapper.selectToolPosts(toolId, category, tag,
                department, keyword, sortBy, offset, pageSize);

        // 查询总数
        Long total = toolPostMapper.countToolPosts(toolId, category, tag, department, keyword);

        // 处理帖子数据：添加标签
        List<ToolPostItemVO> processedPosts = processToolPosts(posts);

        // 构建返回结果
        ToolPostListVO result = new ToolPostListVO();
        result.setList(processedPosts);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public ActivityListVO getActivityList(Integer toolId, String status, Integer page, Integer pageSize) {

        if (toolId != null && toolId == FRONTEND_AGENT_TOOL_ID) {
            toolId = AGENT_TOOL_ID;
        }
        // 参数校验和默认值设置
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询活动列表
        List<ToolActivity> activities = toolActivityMapper.selectActivities(toolId, status, offset, pageSize);

        // 查询总数
        Long total = toolActivityMapper.countActivities(toolId, status);

        // 转换为VO
        List<ActivityItemVO> activityVOs = activities.stream().map(this::convertToActivityItemVO).collect(Collectors.toList());

        // 构建返回结果
        ActivityListVO result = new ActivityListVO();
        result.setList(activityVOs);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public OwnerPermissionVO checkOwnerPermission(Integer toolId, String userId) {
        OwnerPermissionVO vo = new OwnerPermissionVO();
        vo.setToolId(toolId);
        
        if (userId == null || userId.isEmpty()) {
            vo.setIsOwner(false);
            vo.setPermissions(new ArrayList<>());
            return vo;
        }

        // 特殊处理：扶摇Agent应用（toolId=-1）
        // 扶摇Agent应用不在t_new_tool表中，需要检查用户是否为管理员或配置的Owner
        if (toolId != null && toolId.equals(-1)) {
            // 查询用户信息，检查是否为管理员
            UserInfo userInfo = userInfoMapper.selectByUserId(userId);
            if (userInfo != null && "admin".equals(userInfo.getRole())) {
                vo.setIsOwner(true);
                vo.setPermissions(Arrays.asList("publish_activity", "manage_posts", "set_featured"));
                return vo;
            }
            // TODO: 如果后续有扶摇Agent的Owner配置表，可以在这里查询
            // 目前暂时返回false，只有管理员可以操作
            vo.setIsOwner(false);
            vo.setPermissions(new ArrayList<>());
            return vo;
        }

        // 查询工具信息
        List<Tool> tools = toolMapper.selectTools(null);
        Tool tool = tools.stream()
                .filter(t -> t.getToolId() != null && t.getToolId().equals(Long.valueOf(toolId)))
                .findFirst()
                .orElse(null);

        if (tool == null) {
            vo.setIsOwner(false);
            vo.setPermissions(new ArrayList<>());
            return vo;
        }

        // 检查是否为Owner
        boolean isOwner = userId.equals(tool.getOwner());

        vo.setIsOwner(isOwner);
        if (isOwner) {
            List<String> permissions = Arrays.asList("publish_activity", "manage_posts", "set_featured");
            vo.setPermissions(permissions);
        } else {
            vo.setPermissions(new ArrayList<>());
        }

        return vo;
    }

    @Override
    public TagListVO getTags(Integer toolId, String department) {

        if (toolId != null && toolId == FRONTEND_AGENT_TOOL_ID) {
            toolId = AGENT_TOOL_ID;
        }
        List<TagVO> tags = toolPostMapper.selectTagsWithCount(toolId, department);

        // 如果没有"全部"标签，添加一个
        boolean hasAllTag = tags.stream().anyMatch(tag -> "全部".equals(tag.getName()));
        if (!hasAllTag && !CollectionUtils.isEmpty(tags)) {
            TagVO allTag = new TagVO();
            allTag.setName("全部");
            // 计算总数
            int totalCount = tags.stream().mapToInt(TagVO::getCount).sum();
            allTag.setCount(totalCount);
            tags.add(0, allTag);
        }

        TagListVO result = new TagListVO();
        result.setList(tags);
        return result;
    }

    @Override
    public DepartmentListVO getDepartments(Integer toolId, String tag) {
        List<DepartmentVO> departments = toolPostMapper.selectDepartmentRankings(toolId, tag);

        // 设置ID（如果数据库没有自动生成）
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId() == null) {
                departments.get(i).setId(i + 1);
            }
        }

        DepartmentListVO result = new DepartmentListVO();
        result.setList(departments);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityCreateResponseVO createActivity(ActivityCreateRequestVO request, String userId) {
        // 权限校验：检查是否为工具Owner
        OwnerPermissionVO permission = checkOwnerPermission(request.getToolId(), userId);
        if (!permission.getIsOwner()) {
            throw new BusinessException(403, "无权限，只有工具Owner可以发布活动");
        }

        // 查询工具信息
        List<Tool> tools = toolMapper.selectTools(null);
        Tool tool = tools.stream()
                .filter(t -> t.getToolId() != null && t.getToolId().equals(Long.valueOf(request.getToolId())))
                .findFirst()
                .orElseThrow(() -> new BusinessException(404, "工具不存在"));

        // 解析日期和时间
        Date date;
        Date startTime;
        Date endTime;
        try {
            date = DATE_FORMAT.parse(request.getDate());
            startTime = parseTimeString(request.getStartTime());
            endTime = parseTimeString(request.getEndTime());
        } catch (Exception e) {
            log.error("日期时间解析失败", e);
            throw new BusinessException(400, "日期或时间格式错误");
        }

        // 计算活动状态
        Date now = new Date();
        String status = calculateActivityStatus(date, startTime, endTime, now);

        // 创建活动实体
        ToolActivity activity = new ToolActivity();
        activity.setToolId(request.getToolId());
        activity.setToolName(tool.getTitle());
        activity.setType(request.getType());
        activity.setTitle(request.getTitle());
        activity.setContent(request.getContent());
        activity.setCover(request.getCover());
        activity.setDate(date);
        activity.setStartTime(startTime);
        activity.setEndTime(endTime);
        activity.setLocation(request.getLocation());
        activity.setMeetingLink(request.getMeetingLink());
        activity.setSpeaker(request.getSpeaker());
        activity.setSpeakerTitle(request.getSpeakerTitle());
        activity.setMaxParticipants(request.getMaxParticipants());
        activity.setCurrentParticipants(0);
        activity.setStatus(status);
        activity.setCreateTime(now);

        // 插入数据库
        int rows = toolActivityMapper.insertActivity(activity);
        if (rows <= 0) {
            throw new BusinessException(500, "活动创建失败");
        }

        // 构建响应
        ActivityCreateResponseVO response = new ActivityCreateResponseVO();
        response.setId(activity.getId());
        response.setToolId(activity.getToolId());
        response.setTitle(activity.getTitle());
        response.setStatus(activity.getStatus());
        response.setCreateTime(ISO_DATE_FORMAT.format(activity.getCreateTime()));

        return response;
    }

    @Override
    public ActivityDetailVO getActivityDetail(Integer activityId, String userId) {
        // 查询活动详情
        ToolActivity activity = toolActivityMapper.selectActivityById(activityId);
        if (activity == null) {
            throw new BusinessException(404, "活动不存在");
        }

        // 转换为详情VO
        ActivityDetailVO detailVO = convertToActivityDetailVO(activity);

        // 查询参与者列表
        List<ActivityParticipantVO> participants = toolActivityMapper.selectParticipants(activityId);
        detailVO.setParticipants(participants);

        // 检查当前用户是否已报名
        if (userId != null && !userId.isEmpty()) {
            ToolActivityJoin join = toolActivityMapper.selectJoinByUser(activityId, userId);
            detailVO.setIsJoined(join != null);
        } else {
            detailVO.setIsJoined(false);
        }

        return detailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityJoinResponseVO joinActivity(Integer activityId, String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new BusinessException(401, "请先登录");
        }

        // 查询活动详情
        ToolActivity activity = toolActivityMapper.selectActivityById(activityId);
        if (activity == null) {
            throw new BusinessException(404, "活动不存在");
        }

        // 检查活动状态
        Date now = new Date();
        String status = calculateActivityStatus(activity.getDate(), activity.getStartTime(),
                activity.getEndTime(), now);
        if ("ended".equals(status)) {
            throw new BusinessException(400, "活动已结束，无法报名");
        }

        // 检查是否已报名
        ToolActivityJoin existingJoin = toolActivityMapper.selectJoinByUser(activityId, userId);
        if (existingJoin != null) {
            throw new BusinessException(400, "您已报名该活动");
        }

        // 检查报名人数限制
        Integer currentCount = toolActivityMapper.countParticipants(activityId);
        if (activity.getMaxParticipants() != null && activity.getMaxParticipants() > 0) {
            if (currentCount >= activity.getMaxParticipants()) {
                throw new BusinessException(400, "活动报名已满");
            }
        }

        // 创建报名记录
        ToolActivityJoin join = new ToolActivityJoin();
        join.setToolId(activity.getToolId());
        join.setToolActivityId(activityId);
        join.setJoinUserId(userId);
        join.setJoinTime(now);
        join.setCanceled(0);

        int rows = toolActivityMapper.insertJoin(join);
        if (rows <= 0) {
            throw new BusinessException(500, "报名失败");
        }

        // 更新活动当前报名人数
        activity.setCurrentParticipants(currentCount + 1);
        toolActivityMapper.updateActivity(activity);

        // 构建响应
        ActivityJoinResponseVO response = new ActivityJoinResponseVO();
        response.setActivityId(activityId);
        response.setUserId(Integer.parseInt(userId));
        response.setJoinTime(ISO_DATE_FORMAT.format(now));

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelJoin(Integer activityId, String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new BusinessException(401, "请先登录");
        }

        // 查询报名记录
        ToolActivityJoin join = toolActivityMapper.selectJoinByUser(activityId, userId);
        if (join == null) {
            throw new BusinessException(404, "未找到报名记录");
        }

        // 取消报名
        int rows = toolActivityMapper.cancelJoin(activityId, userId);
        if (rows <= 0) {
            throw new BusinessException(500, "取消失败");
        }

        // 更新活动当前报名人数
        ToolActivity activity = toolActivityMapper.selectActivityById(activityId);
        if (activity != null) {
            Integer currentCount = toolActivityMapper.countParticipants(activityId);
            activity.setCurrentParticipants(currentCount);
            toolActivityMapper.updateActivity(activity);
        }
    }

    /**
     * 处理工具帖子数据：添加标签
     *
     * @param posts 原始帖子列表
     * @return 处理后的帖子列表
     */
    private List<ToolPostItemVO> processToolPosts(List<ToolPostItemVO> posts) {
        if (CollectionUtils.isEmpty(posts)) {
            return new ArrayList<>();
        }

        for (ToolPostItemVO post : posts) {
            // 查询并设置标签列表
            List<PostTag> tags = toolPostMapper.selectTagsByPostId(String.valueOf(post.getId()));
            if (!CollectionUtils.isEmpty(tags)) {
                List<String> tagNames = tags.stream()
                        .map(PostTag::getTag)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                post.setTags(tagNames);
            } else {
                post.setTags(new ArrayList<>());
            }

            // 设置默认值
            if (post.getComments() == null) {
                post.setComments(0);
            }
            if (post.getLikes() == null) {
                post.setLikes(0);
            }
            if (post.getViews() == null) {
                post.setViews(0);
            }
        }

        return posts;
    }

    /**
     * 转换为活动项VO
     *
     * @param activity 活动实体
     * @return 活动项VO
     */
    private ActivityItemVO convertToActivityItemVO(ToolActivity activity) {
        ActivityItemVO vo = new ActivityItemVO();
        vo.setId(activity.getId());
        vo.setToolId(activity.getToolId());
        vo.setToolName(activity.getToolName());
        vo.setType(activity.getType());
        vo.setTitle(activity.getTitle());
        vo.setContent(activity.getContent());
        vo.setCover(activity.getCover());
        vo.setDate(DATE_FORMAT.format(activity.getDate()));
        vo.setStartTime(TIME_FORMAT.format(activity.getStartTime()));
        vo.setEndTime(TIME_FORMAT.format(activity.getEndTime()));
        vo.setLocation(activity.getLocation());
        vo.setMeetingLink(activity.getMeetingLink());
        vo.setSpeaker(activity.getSpeaker());
        vo.setSpeakerTitle(activity.getSpeakerTitle());
        vo.setMaxParticipants(activity.getMaxParticipants());
        vo.setCurrentParticipants(activity.getCurrentParticipants());
        vo.setStatus(activity.getStatus());
        vo.setCreateTime(ISO_DATE_FORMAT.format(activity.getCreateTime()));
        return vo;
    }

    /**
     * 转换为活动详情VO
     *
     * @param activity 活动实体
     * @return 活动详情VO
     */
    private ActivityDetailVO convertToActivityDetailVO(ToolActivity activity) {
        ActivityDetailVO vo = new ActivityDetailVO();
        vo.setId(activity.getId());
        vo.setToolId(activity.getToolId());
        vo.setToolName(activity.getToolName());
        vo.setType(activity.getType());
        vo.setTitle(activity.getTitle());
        vo.setContent(activity.getContent());
        vo.setCover(activity.getCover());
        vo.setDate(DATE_FORMAT.format(activity.getDate()));
        vo.setStartTime(TIME_FORMAT.format(activity.getStartTime()));
        vo.setEndTime(TIME_FORMAT.format(activity.getEndTime()));
        vo.setLocation(activity.getLocation());
        vo.setMeetingLink(activity.getMeetingLink());
        vo.setSpeaker(activity.getSpeaker());
        vo.setSpeakerTitle(activity.getSpeakerTitle());
        vo.setSpeakerAvatar("");
        vo.setMaxParticipants(activity.getMaxParticipants());
        vo.setCurrentParticipants(activity.getCurrentParticipants());
        vo.setStatus(activity.getStatus());
        vo.setCreateTime(ISO_DATE_FORMAT.format(activity.getCreateTime()));
        vo.setCreatorId(null);
        vo.setCreatorName("系统");
        return vo;
    }

    /**
     * 解析时间字符串（HH:mm格式）为Date对象
     *
     * @param timeStr 时间字符串（HH:mm格式）
     * @return Date对象（使用1970-01-01作为基准日期）
     */
    private Date parseTimeString(String timeStr) {
        try {
            String[] parts = timeStr.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("时间格式错误，应为HH:mm");
            }
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            
            Calendar cal = Calendar.getInstance();
            cal.set(1970, 0, 1, hour, minute, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        } catch (Exception e) {
            log.error("解析时间字符串失败: {}", timeStr, e);
            throw new BusinessException(400, "时间格式错误，应为HH:mm");
        }
    }

    /**
     * 计算活动状态
     *
     * @param date       活动日期
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param now        当前时间
     * @return 活动状态
     */
    private String calculateActivityStatus(Date date, Date startTime, Date endTime, Date now) {
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(date);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startTime);
        calStart.set(Calendar.HOUR_OF_DAY, startCal.get(Calendar.HOUR_OF_DAY));
        calStart.set(Calendar.MINUTE, startCal.get(Calendar.MINUTE));
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);
        
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(date);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endTime);
        calEnd.set(Calendar.HOUR_OF_DAY, endCal.get(Calendar.HOUR_OF_DAY));
        calEnd.set(Calendar.MINUTE, endCal.get(Calendar.MINUTE));
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);

        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(now);

        if (nowCal.before(calStart)) {
            return "upcoming";
        } else if (nowCal.after(calEnd)) {
            return "ended";
        } else {
            return "ongoing";
        }
    }

    @Override
    public ActivityCreateResponseVO updateActivity(Integer activityId, ActivityUpdateRequestVO request, String userId) {
        // 查询活动是否存在
        ToolActivity existingActivity = toolActivityMapper.selectActivityById(activityId);
        if (existingActivity == null) {
            throw new BusinessException("活动不存在");
        }

        // TODO: 权限检查 - 只有活动创建者、工具Owner或管理员可以编辑
        // 这里暂时跳过权限检查

        // 更新活动信息
        ToolActivity activity = new ToolActivity();
        activity.setId(activityId);
        
        if (request.getType() != null) {
            activity.setType(request.getType());
        }
        if (request.getTitle() != null) {
            activity.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            activity.setContent(request.getContent());
        }
        if (request.getCover() != null) {
            activity.setCover(request.getCover());
        }
        if (request.getDate() != null) {
            try {
                activity.setDate(DATE_FORMAT.parse(request.getDate()));
            } catch (Exception e) {
                log.error("日期解析失败: {}", request.getDate(), e);
            }
        }
        if (request.getStartTime() != null) {
            try {
                activity.setStartTime(parseTimeString(request.getStartTime()));
            } catch (Exception e) {
                log.error("开始时间解析失败: {}", request.getStartTime(), e);
            }
        }
        if (request.getEndTime() != null) {
            try {
                activity.setEndTime(parseTimeString(request.getEndTime()));
            } catch (Exception e) {
                log.error("结束时间解析失败: {}", request.getEndTime(), e);
            }
        }
        if (request.getLocation() != null) {
            activity.setLocation(request.getLocation());
        }
        if (request.getMeetingLink() != null) {
            activity.setMeetingLink(request.getMeetingLink());
        }
        if (request.getSpeaker() != null) {
            activity.setSpeaker(request.getSpeaker());
        }
        if (request.getSpeakerTitle() != null) {
            activity.setSpeakerTitle(request.getSpeakerTitle());
        }
        if (request.getMaxParticipants() != null) {
            activity.setMaxParticipants(request.getMaxParticipants());
        }

        // 重新计算活动状态
        Date now = new Date();
        String status = calculateActivityStatus(
            activity.getDate() != null ? activity.getDate() : existingActivity.getDate(),
            activity.getStartTime() != null ? activity.getStartTime() : existingActivity.getStartTime(),
            activity.getEndTime() != null ? activity.getEndTime() : existingActivity.getEndTime(),
            now
        );
        activity.setStatus(status);

        toolActivityMapper.updateActivity(activity);

        // 返回更新后的活动信息
        ToolActivity updatedActivity = toolActivityMapper.selectActivityById(activityId);
        ActivityCreateResponseVO response = new ActivityCreateResponseVO();
        response.setId(updatedActivity.getId());
        response.setToolId(updatedActivity.getToolId());
        response.setTitle(updatedActivity.getTitle());
        response.setStatus(updatedActivity.getStatus());
        response.setCreateTime(ISO_DATE_FORMAT.format(updatedActivity.getCreateTime()));
        return response;
    }

    @Override
    public void deleteActivity(Integer activityId, String userId) {
        // 查询活动是否存在
        ToolActivity activity = toolActivityMapper.selectActivityById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        // TODO: 权限检查 - 只有活动创建者、工具Owner或管理员可以删除
        // 这里暂时跳过权限检查

        // 删除活动报名记录
        toolActivityMapper.deleteJoinsByActivity(activityId);
        
        // 删除活动
        toolActivityMapper.deleteActivity(activityId);
    }

    @Override
    public ActivityRegistrationListVO getActivityRegistrations(Integer activityId, String userId, Integer page, Integer pageSize) {
        // 参数校验和默认值设置
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }

        // 查询活动是否存在
        ToolActivity activity = toolActivityMapper.selectActivityById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        // TODO: 权限检查 - 活动创建者、工具Owner或管理员可查看完整报名列表，普通用户只能看到前10个
        // 这里暂时返回完整列表

        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询报名列表
        List<ActivityParticipantVO> participants = toolActivityMapper.selectRegistrations(activityId, offset, pageSize);

        // 查询总数
        Long total = toolActivityMapper.countRegistrations(activityId);

        // 构建响应
        ActivityRegistrationListVO vo = new ActivityRegistrationListVO();
        vo.setList(participants);
        vo.setTotal(total);
        vo.setPage(page);
        vo.setPageSize(pageSize);
        return vo;
    }

    @Override
    public ToolFeaturedPostResponseVO getFeaturedPost(Integer toolId) {
        // 此接口仅支持toolId=0（其他工具）
        if (toolId == null || toolId != 0) {
            ToolFeaturedPostResponseVO vo = new ToolFeaturedPostResponseVO();
            vo.setPost(null);
            return vo;
        }

        // 查询精华帖子：zone_id=3（AI工具专区），tool_id=0（其他工具），essence_post='1'
        Post featuredPost = postMapper.selectToolFeaturedPost(3, 0);

        ToolFeaturedPostResponseVO vo = new ToolFeaturedPostResponseVO();
        if (featuredPost == null) {
            vo.setPost(null);
            return vo;
        }

        // 转换为FeaturedPostVO
        FeaturedPostVO postVO = convertToFeaturedPostVO(featuredPost);
        vo.setPost(postVO);
        return vo;
    }

    @Override
    public ToolFeaturedPostSetResponseVO setFeaturedPost(ToolFeaturedPostRequestVO request, String userId) {
        // 此接口仅支持toolId=0（其他工具）
        if (request.getToolId() == null || request.getToolId() != 0) {
            throw new BusinessException("此接口仅支持toolId=0（其他工具）");
        }

        // TODO: 权限检查 - 只有工具Owner或管理员可以设置精华帖子
        // 这里暂时跳过权限检查

        // 如果postId为null或0，表示取消精华
        if (request.getPostId() == null || request.getPostId() == 0) {
            // 取消所有精华帖子
            postMapper.cancelToolFeaturedPost(3, 0);
            
            ToolFeaturedPostSetResponseVO response = new ToolFeaturedPostSetResponseVO();
            response.setPostId(0);
            response.setSetTime(ISO_DATE_FORMAT.format(new Date()));
            return response;
        }

        // 先取消所有精华帖子
        postMapper.cancelToolFeaturedPost(3, 0);

        // 设置新的精华帖子
        postMapper.updatePostEssence(String.valueOf(request.getPostId()), "1");

        ToolFeaturedPostSetResponseVO response = new ToolFeaturedPostSetResponseVO();
        response.setPostId(request.getPostId());
        response.setSetTime(ISO_DATE_FORMAT.format(new Date()));
        return response;
    }

    /**
     * 将Post转换为FeaturedPostVO
     *
     * @param post 帖子对象
     * @return FeaturedPostVO
     */
    private FeaturedPostVO convertToFeaturedPostVO(Post post) {
        FeaturedPostVO vo = new FeaturedPostVO();
        vo.setId(Integer.parseInt(post.getPostId()));
        vo.setTitle(post.getTitle());
        
        // 提取摘要（从content中提取前200个字符）
        String content = post.getContent();
        if (content != null && content.length() > 200) {
            vo.setDescription(content.substring(0, 200) + "...");
        } else {
            vo.setDescription(content != null ? content : "");
        }
        
        vo.setCover(post.getFrontCover());
        vo.setImage(post.getFrontCover());
        
        // 查询作者信息
        if (post.getAuthorId() != null) {
            UserInfo author = userInfoMapper.selectByUserId(post.getAuthorId());
            if (author != null) {
                vo.setAuthor(author.getChnName() != null ? author.getChnName() : author.getUserName());
                vo.setAuthorId(Integer.parseInt(author.getUserId()));
                vo.setAuthorAvatar(author.getAuthorAvatar());
            }
        }
        
        // 查询标签（从t_new_posts_tag表）
        // TODO: 需要关联查询标签信息
        
        vo.setViews(post.getViewsNums() != null ? post.getViewsNums() : 0);
        
        // 查询评论数和点赞数
        // TODO: 需要关联查询评论数和点赞数
        
        vo.setFeatured(true);
        vo.setCreateTime(ISO_DATE_FORMAT.format(post.getCreatedAt()));
        
        return vo;
    }
}
