package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.UserController;
import com.aicommunity.dto.UserProfileDTO;
import com.aicommunity.dto.UserPointsDTO;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.*;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户服务单元测试
 *
 * @author AI Community Team
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PageHelper.class})
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private PostMapper postMapper;

    @Mock
    private FavoriteMapper favoriteMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private ActivityMapper activityMapper;

    @Mock
    private PointsRecordMapper pointsRecordMapper;

    @Mock
    private HonorMapper honorMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(PageHelper.class);

        // 准备测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmployeeId("E001");
        testUser.setName("测试用户");
        testUser.setAvatar("http://example.com/avatar.jpg");
        testUser.setDepartment("技术部");
        testUser.setBio("这是个人简介");
    }

    /**
     * 测试获取当前用户信息
     */
    @Test
    public void testGetCurrentUser() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userRoleMapper.selectRolesByUserId(1L)).thenReturn(Arrays.asList("user"));
        when(postMapper.countByAuthorId(1L)).thenReturn(10);
        when(favoriteMapper.countByUserId(1L)).thenReturn(5);
        when(commentMapper.countByUserId(1L)).thenReturn(20);
        when(activityMapper.countByUserId(1L)).thenReturn(3);
        when(honorMapper.sumFlowersByUserId(1L)).thenReturn(100);

        // When
        UserProfileDTO result = userService.getCurrentUser();

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals("测试用户", result.getName());
        assertEquals("技术部", result.getDepartment());
        assertEquals(10, result.getPostsCount().intValue());
        assertEquals(5, result.getFavoritesCount().intValue());
        assertEquals(20, result.getCommentsCount().intValue());
        assertEquals(3, result.getActivitiesCount().intValue());
        assertEquals(100, result.getFlowersCount().intValue());
    }

    /**
     * 测试获取当前用户信息 - 未授权
     */
    @Test(expected = BusinessException.class)
    public void testGetCurrentUserUnauthorized() {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        userService.getCurrentUser();

        // Then - 应该抛出BusinessException
    }

    /**
     * 测试计算用户积分
     */
    @Test
    public void testCalculatePoints() {
        // Given
        String token = "Bearer test-token";
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);

        // Mock积分记录
        List<Map<String, Object>> pointsRecords = new ArrayList<>();
        Map<String, Object> record1 = new HashMap<>();
        record1.put("type", "post");
        record1.put("points", 15);
        pointsRecords.add(record1);

        Map<String, Object> record2 = new HashMap<>();
        record2.put("type", "comment");
        record2.put("points", 1);
        pointsRecords.add(record2);

        Map<String, Object> record3 = new HashMap<>();
        record3.put("type", "like_received");
        record3.put("points", 3);
        pointsRecords.add(record3);

        when(pointsRecordMapper.selectByUserIdAndMonth(1L, currentMonth)).thenReturn(pointsRecords);

        // When
        UserPointsDTO result = userService.calculatePoints();

        // Then
        assertNotNull(result);
        assertTrue(result.getPoints() > 0);
        assertNotNull(result.getBreakdown());
    }

    /**
     * 测试获取用户个人资料
     */
    @Test
    public void testGetUserProfile() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.selectByName("测试用户")).thenReturn(testUser);
        when(userRoleMapper.selectRolesByUserId(1L)).thenReturn(Arrays.asList("user"));
        when(postMapper.countByAuthorId(1L)).thenReturn(10);
        when(favoriteMapper.countByUserId(1L)).thenReturn(5);
        when(commentMapper.countByUserId(1L)).thenReturn(20);
        when(activityMapper.countByUserId(1L)).thenReturn(3);
        when(honorMapper.sumFlowersByUserId(1L)).thenReturn(100);

        // When - 通过用户ID查询
        UserProfileDTO result1 = userService.getUserProfile(1L, null);

        // Then
        assertNotNull(result1);
        assertEquals(1L, result1.getId().longValue());
        assertEquals("测试用户", result1.getName());

        // When - 通过用户名查询
        UserProfileDTO result2 = userService.getUserProfile(null, "测试用户");

        // Then
        assertNotNull(result2);
        assertEquals(1L, result2.getId().longValue());
    }

    /**
     * 测试获取用户个人资料 - 用户不存在
     */
    @Test(expected = BusinessException.class)
    public void testGetUserProfileNotFound() {
        // Given
        when(userMapper.selectById(999L)).thenReturn(null);
        when(userMapper.selectByName("不存在的用户")).thenReturn(null);

        // When
        userService.getUserProfile(999L, null);

        // Then - 应该抛出BusinessException
    }

    /**
     * 测试更新用户资料
     */
    @Test
    public void testUpdateProfile() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.update(any(User.class))).thenReturn(1);

        UserController.UpdateProfileRequest request = new UserController.UpdateProfileRequest();
        request.setBio("新的个人简介");
        request.setAvatar("http://example.com/new-avatar.jpg");

        // When
        userService.updateProfile(request);

        // Then
        verify(userMapper, times(1)).update(any(User.class));
    }

    /**
     * 测试获取用户发布的帖子
     */
    @Test
    public void testGetUserPosts() {
        // Given
        PowerMockito.mockStatic(PageHelper.class);
        PageQuery pageQuery = new PageQuery(1, 10);

        List<Map<String, Object>> posts = new ArrayList<>();
        Map<String, Object> post = new HashMap<>();
        post.put("id", 1L);
        post.put("title", "测试帖子");
        posts.add(post);

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(posts);
        pageInfo.setTotal(1);

        when(postMapper.selectByAuthorId(1L)).thenReturn(posts);

        // When
        PageResult<?> result = userService.getUserPosts(1L, pageQuery);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal().intValue());
    }

    /**
     * 测试获取热门贡献者
     */
    @Test
    public void testGetTopContributors() {
        // Given
        List<Map<String, Object>> contributors = new ArrayList<>();
        Map<String, Object> contributor = new HashMap<>();
        contributor.put("id", 1L);
        contributor.put("name", "测试用户");
        contributor.put("postCount", 10);
        contributors.add(contributor);

        when(postMapper.selectTopContributors("practices", 10)).thenReturn(contributors);

        // When
        Object result = userService.getTopContributors("practices", 10);

        // Then
        assertNotNull(result);
    }
}
