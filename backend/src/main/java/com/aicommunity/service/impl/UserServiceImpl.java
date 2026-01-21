package com.aicommunity.service.impl;

import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.UserService;
import com.aicommunity.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private UserPointsMapper userPointsMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostCollectMapper postCollectMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ToolActivityMapper toolActivityMapper;

    @Autowired
    private PracticeMapper practiceMapper;

    @Override
    public LoginResponseVO login(LoginRequestVO loginRequest) {
        log.info("用户登录，工号：{}", loginRequest.getEmployeeId());

        // 根据工号查询用户
        UserInfo userInfo = userInfoMapper.selectByUserName(loginRequest.getEmployeeId());
        if (userInfo == null) {
            throw new RuntimeException("工号不存在");
        }

        // TODO: 实际项目中应该验证密码，这里暂时跳过密码验证
        // 验证密码（实际应该从用户中心验证或使用加密密码验证）
        // if (!passwordEncoder.matches(loginRequest.getPassword(), userInfo.getPassword())) {
        //     throw new RuntimeException("密码错误");
        // }

        // 检查账号状态
        if (!"正常".equals(userInfo.getStatus()) && !"0".equals(userInfo.getStatus())) {
            throw new RuntimeException("账号已被禁用");
        }

        // 生成token（实际应该使用JWT生成）
        String token = generateToken(userInfo.getUserId());

        // 构建用户信息VO
        UserProfileVO userProfileVO = buildUserProfileVO(userInfo, true);

        // 构建登录响应
        LoginResponseVO responseVO = new LoginResponseVO();
        responseVO.setToken(token);
        responseVO.setExpiresIn(86400L); // 24小时
        responseVO.setUser(userProfileVO);

        return responseVO;
    }

    @Override
    public void logout(String userId) {
        log.info("用户退出登录，用户ID：{}", userId);
        // TODO: 实际项目中应该将token加入黑名单或从缓存中删除
    }

    @Override
    public UserProfileVO getCurrentUser(String userId) {
        log.info("获取当前用户信息，用户ID：{}", userId);
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        return buildUserProfileVO(userInfo, true);
    }

    @Override
    public UserProfileVO getUserById(String userId) {
        log.info("根据ID获取用户信息，用户ID：{}", userId);
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        // 查看他人信息时不返回敏感字段
        return buildUserProfileVO(userInfo, false);
    }

    @Override
    public UserProfileVO getUserByEmployeeId(String employeeId) {
        log.info("根据工号获取用户信息，工号：{}", employeeId);
        UserInfo userInfo = userInfoMapper.selectByUserName(employeeId);
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        // 查看他人信息时不返回敏感字段
        return buildUserProfileVO(userInfo, false);
    }

    @Override
    public void updateCurrentUser(String userId, UserUpdateRequestVO updateRequestVO) {
        log.info("更新用户信息，用户ID：{}", userId);
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息（只能更新头像和简介）
        int rows = userInfoMapper.updateUserProfile(
                userId,
                updateRequestVO.getAvatar(),
                updateRequestVO.getBio()
        );

        if (rows <= 0) {
            throw new RuntimeException("更新用户信息失败");
        }
    }

    /**
     * 构建用户信息VO
     *
     * @param userInfo     用户信息实体
     * @param includeRoles 是否包含角色和工具信息（当前用户返回true，查看他人返回false）
     * @return 用户信息VO
     */
    private UserProfileVO buildUserProfileVO(UserInfo userInfo, boolean includeRoles) {
        UserProfileVO vo = new UserProfileVO();
        vo.setId(userInfo.getUserId());
        vo.setEmployeeId(userInfo.getUserName());
        vo.setName(userInfo.getChnName());
        vo.setAvatar(userInfo.getAuthorAvatar());
        vo.setBio(userInfo.getBio());

        // 构建部门信息
        UserDepartmentsVO departmentsVO = new UserDepartmentsVO();
        if (StringUtils.hasText(userInfo.getDepartmentL1Id())) {
            departmentsVO.setLevel1(buildDepartmentInfo(userInfo.getDepartmentL1Id(), userInfo.getDepartmentL1(), 1));
        }
        if (StringUtils.hasText(userInfo.getDepartmentL2Id())) {
            departmentsVO.setLevel2(buildDepartmentInfo(userInfo.getDepartmentL2Id(), userInfo.getDepartmentL2(), 2));
        }
        if (StringUtils.hasText(userInfo.getDepartmentL3Id())) {
            departmentsVO.setLevel3(buildDepartmentInfo(userInfo.getDepartmentL3Id(), userInfo.getDepartmentL3(), 3));
        }
        if (StringUtils.hasText(userInfo.getDepartmentL4Id())) {
            departmentsVO.setLevel4(buildDepartmentInfo(userInfo.getDepartmentL4Id(), userInfo.getDepartmentL4(), 4));
        }
        if (StringUtils.hasText(userInfo.getDepartmentL5Id())) {
            departmentsVO.setLevel5(buildDepartmentInfo(userInfo.getDepartmentL5Id(), userInfo.getDepartmentL5(), 5));
        }
        if (StringUtils.hasText(userInfo.getDepartmentL6Id())) {
            departmentsVO.setLevel6(buildDepartmentInfo(userInfo.getDepartmentL6Id(), userInfo.getDepartmentL6(), 6));
        }
        vo.setDepartments(departmentsVO);

        // 构建完整部门路径
        StringBuilder deptPath = new StringBuilder();
        if (departmentsVO.getLevel1() != null) {
            deptPath.append(departmentsVO.getLevel1().getName());
        }
        if (departmentsVO.getLevel2() != null) {
            if (deptPath.length() > 0) deptPath.append("/");
            deptPath.append(departmentsVO.getLevel2().getName());
        }
        if (departmentsVO.getLevel3() != null) {
            if (deptPath.length() > 0) deptPath.append("/");
            deptPath.append(departmentsVO.getLevel3().getName());
        }
        if (departmentsVO.getLevel4() != null) {
            if (deptPath.length() > 0) deptPath.append("/");
            deptPath.append(departmentsVO.getLevel4().getName());
        }
        if (departmentsVO.getLevel5() != null) {
            if (deptPath.length() > 0) deptPath.append("/");
            deptPath.append(departmentsVO.getLevel5().getName());
        }
        if (departmentsVO.getLevel6() != null) {
            if (deptPath.length() > 0) deptPath.append("/");
            deptPath.append(departmentsVO.getLevel6().getName());
        }
        vo.setDepartment(deptPath.toString());

        // 统计信息
        vo.setPostsCount(userInfoMapper.countUserPosts(userInfo.getUserId()));
        vo.setFavoritesCount(userInfoMapper.countUserFavorites(userInfo.getUserId()));
        vo.setCommentsCount(userInfoMapper.countUserComments(userInfo.getUserId()));
        vo.setActivitiesCount(userInfoMapper.countUserActivities(userInfo.getUserId()));
        vo.setFlowersCount(userInfoMapper.countUserFlowers(userInfo.getUserId()));

        // 查询积分
        Integer points = userPointsMapper.selectUserPoints(userInfo.getUserId());
        vo.setPoints(points != null ? points : 0);

        // 只有当前用户才返回角色和工具信息
        if (includeRoles) {
            // 解析角色（role字段可能是逗号分隔的字符串）
            List<String> roles = new ArrayList<>();
            if (StringUtils.hasText(userInfo.getRole())) {
                roles = Arrays.stream(userInfo.getRole().split(","))
                        .map(String::trim)
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toList());
            }
            // 如果没有角色，默认添加user角色
            if (roles.isEmpty()) {
                roles.add("user");
            }
            vo.setRoles(roles);

            // 查询用户拥有的工具（owner字段等于用户ID）
            List<Tool> ownedTools = toolMapper.selectTools(null);
            List<OwnedToolVO> ownedToolVOs = ownedTools.stream()
                    .filter(tool -> userInfo.getUserId().equals(tool.getOwner()))
                    .map(tool -> {
                        OwnedToolVO toolVO = new OwnedToolVO();
                        toolVO.setToolId(tool.getId());
                        toolVO.setToolName(tool.getTitle());
                        return toolVO;
                    })
                    .collect(Collectors.toList());
            vo.setOwnedTools(ownedToolVOs);
        }

        return vo;
    }

    /**
     * 构建部门信息
     *
     * @param id    部门ID
     * @param name  部门名称
     * @param level 部门层级
     * @return 部门信息VO
     */
    private DepartmentInfoVO buildDepartmentInfo(String id, String name, Integer level) {
        DepartmentInfoVO deptVO = new DepartmentInfoVO();
        deptVO.setId(id);
        deptVO.setName(name);
        deptVO.setLevel(level);
        return deptVO;
    }

    /**
     * 生成token（临时实现，实际应该使用JWT）
     *
     * @param userId 用户ID
     * @return token字符串
     */
    private String generateToken(String userId) {
        // TODO: 实际应该使用JWT生成token
        // 这里暂时返回一个简单的token
        return "mock_token_" + userId + "_" + System.currentTimeMillis();
    }

    @Override
    public PostListVO getUserPosts(String userId, Integer page, Integer pageSize) {
        log.info("获取用户发布的帖子列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 查询帖子列表
        List<Post> posts = postMapper.selectUserPosts(userId, offset, pageSize);
        Long total = postMapper.countUserPosts(userId);

        // 转换为VO
        List<PostItemVO> postItemVOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(posts)) {
            for (Post post : posts) {
                PostItemVO vo = convertPostToPostItemVO(post);
                postItemVOs.add(vo);
            }
        }

        // 构建返回对象
        PostListVO result = new PostListVO();
        result.setList(postItemVOs);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setTotalPages((int) Math.ceil((double) total / pageSize));

        return result;
    }

    @Override
    public PostListVO getUserFavorites(String userId, Integer page, Integer pageSize) {
        log.info("获取用户收藏的帖子列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 查询收藏记录
        List<PostCollectVO> collectVOs = postCollectMapper.selectUserFavorites(userId, offset, pageSize);
        Long total = postCollectMapper.countUserFavorites(userId);

        // 根据收藏记录查询帖子详情
        List<PostItemVO> postItemVOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(collectVOs)) {
            for (PostCollectVO collectVO : collectVOs) {
                Post post = postMapper.selectByPostId(collectVO.getPostId());
                if (post != null && "0".equals(post.getStatus())) {
                    PostItemVO vo = convertPostToPostItemVO(post);
                    // 设置收藏时间
                    if (collectVO.getCreateTime() != null) {
                        vo.setFavoriteTime(formatDate(collectVO.getCreateTime()));
                    }
                    postItemVOs.add(vo);
                }
            }
        }

        // 构建返回对象
        PostListVO result = new PostListVO();
        result.setList(postItemVOs);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setTotalPages((int) Math.ceil((double) total / pageSize));

        return result;
    }

    @Override
    public CommentListVO getUserComments(String userId, Integer page, Integer pageSize) {
        log.info("获取用户评论列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 查询评论列表
        List<Comment> comments = commentMapper.selectUserComments(userId, offset, pageSize);
        Long total = commentMapper.countUserComments(userId);

        // 转换为VO
        List<UserCommentVO> commentVOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comments)) {
            for (Comment comment : comments) {
                UserCommentVO vo = convertCommentToUserCommentVO(comment);
                commentVOs.add(vo);
            }
        }

        // 构建返回对象
        CommentListVO result = new CommentListVO();
        result.setList(commentVOs.stream().map(this::convertUserCommentVOToCommentVO).collect(Collectors.toList()));
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public ActivityListVO getUserActivities(String userId, Integer page, Integer pageSize) {
        log.info("获取用户参与的活动列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 查询活动列表
        List<ToolActivity> activities = toolActivityMapper.selectUserActivities(userId, offset, pageSize);
        Long total = toolActivityMapper.countUserActivities(userId);

        // 转换为VO
        List<ActivityItemVO> activityVOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(activities)) {
            for (ToolActivity activity : activities) {
                ActivityItemVO vo = convertToolActivityToActivityItemVO(activity);
                // 设置报名人数信息（使用registeredCount字段）
                vo.setRegisteredCount(vo.getCurrentParticipants());
                vo.setMaxRegistrations(vo.getMaxParticipants());
                activityVOs.add(vo);
            }
        }

        // 构建返回对象
        ActivityListVO result = new ActivityListVO();
        result.setList(activityVOs);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public ActivityListVO getUserCreatedActivities(String userId, Integer page, Integer pageSize) {
        log.info("获取用户发布的活动列表，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 查询活动列表
        List<ToolActivity> activities = toolActivityMapper.selectUserCreatedActivities(userId, offset, pageSize);
        Long total = toolActivityMapper.countUserCreatedActivities(userId);

        // 获取用户信息（用于设置创建者信息）
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        String creatorName = userInfo != null ? userInfo.getChnName() : "未知用户";

        // 转换为VO
        List<ActivityItemVO> activityVOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(activities)) {
            for (ToolActivity activity : activities) {
                ActivityItemVO vo = convertToolActivityToActivityItemVO(activity);
                // 设置创建者信息
                vo.setCreatorId(Integer.parseInt(userId));
                vo.setCreatorName(creatorName);
                // 设置报名人数信息（使用registeredCount和maxRegistrations字段）
                vo.setRegisteredCount(vo.getCurrentParticipants());
                vo.setMaxRegistrations(vo.getMaxParticipants());
                activityVOs.add(vo);
            }
        }

        // 构建返回对象
        ActivityListVO result = new ActivityListVO();
        result.setList(activityVOs);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public UserPointsVO getUserPoints(String userId, Integer page, Integer pageSize) {
        log.info("获取用户积分信息，用户ID：{}，页码：{}，每页数量：{}", userId, page, pageSize);

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }

        // 查询总积分
        Integer totalPoints = userPointsMapper.selectUserPoints(userId);
        if (totalPoints == null) {
            totalPoints = 0;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 查询积分历史记录
        List<PointsHistoryVO> pointsHistory = userPointsMapper.selectPointsHistory(userId, offset, pageSize);

        // 查询积分规则
        List<PointsRuleVO> pointsRules = userPointsMapper.selectPointsRules();

        // TODO: 计算本月积分和排名（需要根据实际业务逻辑实现）
        Integer monthlyPoints = 0;
        Integer ranking = 0;

        // 构建返回对象
        UserPointsVO result = new UserPointsVO();
        result.setTotalPoints(totalPoints);
        result.setMonthlyPoints(monthlyPoints);
        result.setRanking(ranking);
        result.setPointsHistory(pointsHistory);
        result.setPointsRules(pointsRules);

        return result;
    }

    /**
     * 将Post实体转换为PostItemVO
     */
    private PostItemVO convertPostToPostItemVO(Post post) {
        PostItemVO vo = new PostItemVO();
        vo.setId(Integer.parseInt(post.getPostId()));
        vo.setTitle(post.getTitle());
        vo.setImage(post.getFrontCover());
        vo.setViews(post.getViewsNums() != null ? post.getViewsNums() : 0);
        vo.setFeatured("1".equals(post.getEssencePost()));

        // 查询作者信息
        if (StringUtils.hasText(post.getAuthorId())) {
            UserInfo author = userInfoMapper.selectByUserId(post.getAuthorId());
            if (author != null) {
                vo.setAuthor(author.getChnName());
                vo.setAuthorId(Integer.parseInt(author.getUserId()));
                vo.setAuthorAvatar(author.getAuthorAvatar());
                // 构建部门路径
                StringBuilder deptPath = new StringBuilder();
                if (StringUtils.hasText(author.getDepartmentL1())) {
                    deptPath.append(author.getDepartmentL1());
                }
                if (StringUtils.hasText(author.getDepartmentL2())) {
                    if (deptPath.length() > 0) deptPath.append("/");
                    deptPath.append(author.getDepartmentL2());
                }
                if (StringUtils.hasText(author.getDepartmentL3())) {
                    if (deptPath.length() > 0) deptPath.append("/");
                    deptPath.append(author.getDepartmentL3());
                }
                vo.setDepartment(deptPath.toString());
            }
        }

        // 查询标签
        List<PostTag> tags = practiceMapper.selectTagsByPostId(post.getPostId());
        if (!CollectionUtils.isEmpty(tags)) {
            List<String> tagNames = tags.stream()
                    .map(PostTag::getTag)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toList());
            vo.setTags(tagNames);
        } else {
            vo.setTags(new ArrayList<>());
        }

        // 查询统计数据
        Integer likes = postMapper.countLikes(post.getPostId());
        Integer comments = postMapper.countComments(post.getPostId());
        vo.setLikes(likes != null ? likes : 0);
        vo.setComments(comments != null ? comments : 0);

        // 格式化时间
        if (post.getCreatedAt() != null) {
            vo.setCreateTime(formatDate(post.getCreatedAt()));
            vo.setCreateTimeDisplay(calculateRelativeTime(formatDate(post.getCreatedAt())));
        }

        // 设置描述（从内容中提取前100个字符）
        if (StringUtils.hasText(post.getContent())) {
            String description = post.getContent().length() > 100
                    ? post.getContent().substring(0, 100) + "..."
                    : post.getContent();
            vo.setDescription(description);
        }

        return vo;
    }

    /**
     * 将Comment实体转换为UserCommentVO
     */
    private UserCommentVO convertCommentToUserCommentVO(Comment comment) {
        UserCommentVO vo = new UserCommentVO();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setPostId(Integer.parseInt(comment.getPostId()));
        vo.setUserId(Integer.parseInt(comment.getUserId()));
        vo.setLikes(comment.getLikes() != null ? comment.getLikes() : 0);

        // 查询帖子标题
        Post post = postMapper.selectByPostId(comment.getPostId());
        if (post != null) {
            vo.setPostTitle(post.getTitle());
        }

        // 查询用户信息
        if (StringUtils.hasText(comment.getUserId())) {
            UserInfo user = userInfoMapper.selectByUserId(comment.getUserId());
            if (user != null) {
                vo.setUserName(user.getChnName());
                vo.setUserAvatar(user.getAuthorAvatar());
            }
        }

        // 格式化时间
        if (comment.getCreateTime() != null) {
            vo.setCreateTime(formatDate(comment.getCreateTime()));
        }

        return vo;
    }

    /**
     * 将UserCommentVO转换为CommentVO
     */
    private CommentVO convertUserCommentVOToCommentVO(UserCommentVO userCommentVO) {
        CommentVO vo = new CommentVO();
        vo.setId(userCommentVO.getId());
        vo.setContent(userCommentVO.getContent());
        vo.setPostId(userCommentVO.getPostId());
        vo.setUserId(userCommentVO.getUserId());
        vo.setUserName(userCommentVO.getUserName());
        vo.setUserAvatar(userCommentVO.getUserAvatar());
        vo.setLikes(userCommentVO.getLikes());
        vo.setCreateTime(userCommentVO.getCreateTime());
        vo.setReplies(new ArrayList<>());
        return vo;
    }

    /**
     * 将ToolActivity实体转换为ActivityItemVO
     */
    private ActivityItemVO convertToolActivityToActivityItemVO(ToolActivity activity) {
        ActivityItemVO vo = new ActivityItemVO();
        vo.setId(activity.getId());
        vo.setTitle(activity.getTitle());
        vo.setCover(activity.getCover());
        vo.setType(activity.getType());
        vo.setStatus(activity.getStatus());
        vo.setLocation(activity.getLocation());

        // 格式化日期和时间
        if (activity.getDate() != null && activity.getStartTime() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String dateStr = dateFormat.format(activity.getDate());
            String timeStr = timeFormat.format(activity.getStartTime());
            vo.setDate(dateStr + " " + timeStr);
        }

        // 查询报名人数
        Integer registeredCount = toolActivityMapper.countParticipants(activity.getId());
        vo.setCurrentParticipants(registeredCount != null ? registeredCount : 0);
        vo.setMaxParticipants(activity.getMaxParticipants());

        return vo;
    }

    /**
     * 格式化日期为ISO 8601格式
     */
    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 计算相对时间显示
     */
    private String calculateRelativeTime(String createTime) {
        // TODO: 实现相对时间计算逻辑
        return createTime;
    }
}
