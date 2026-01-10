package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.ActivityController;
import com.aicommunity.dto.ActivityDetailDTO;
import com.aicommunity.entity.*;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 活动服务单元测试
 *
 * @author AI Community Team
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PageHelper.class})
public class ActivityServiceImplTest {

    @Mock
    private ActivityMapper activityMapper;

    @Mock
    private ActivityRegistrationMapper activityRegistrationMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ToolMapper toolMapper;

    @Mock
    private PointsRecordMapper pointsRecordMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private Activity testActivity;
    private User testUser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(PageHelper.class);

        // 准备测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("测试用户");
        testUser.setAvatar("http://example.com/avatar.jpg");
        testUser.setDepartment("技术部");

        testActivity = new Activity();
        testActivity.setId(1L);
        testActivity.setTitle("测试活动");
        testActivity.setToolId(1L);
        testActivity.setType("training");
        testActivity.setDate(LocalDate.now().plusDays(7));
        testActivity.setCover("http://example.com/cover.jpg");
        testActivity.setContent("活动内容");
        testActivity.setAuthorId(1L);
        testActivity.setStatus("upcoming");
        testActivity.setRegisteredCount(10);
        testActivity.setCreateTime(LocalDateTime.now());
    }

    /**
     * 测试获取活动列表
     */
    @Test
    public void testGetActivities() {
        // Given
        PowerMockito.mockStatic(PageHelper.class);
        PageQuery pageQuery = new PageQuery(1, 10);

        List<Map<String, Object>> activities = new ArrayList<>();
        Map<String, Object> activity = new HashMap<>();
        activity.put("id", 1L);
        activity.put("title", "测试活动");
        activities.add(activity);

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(activities);
        pageInfo.setTotal(1);

        when(activityMapper.selectByCondition(isNull(), isNull(), isNull())).thenReturn(activities);

        // When
        PageResult<?> result = activityService.getActivities(null, null, null, pageQuery);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal().intValue());
    }

    /**
     * 测试获取活动详情
     */
    @Test
    public void testGetActivityDetail() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(activityMapper.selectById(1L)).thenReturn(testActivity);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(activityRegistrationMapper.selectByActivityAndUser(1L, 2L)).thenReturn(null);
        when(userRoleMapper.selectRolesByUserId(2L)).thenReturn(Arrays.asList("user"));

        // When
        ActivityDetailDTO result = activityService.getActivityDetail(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals("测试活动", result.getTitle());
        assertEquals("测试用户", result.getAuthorName());
        assertFalse(result.getIsRegistered());
    }

    /**
     * 测试创建活动
     */
    @Test
    public void testCreateActivity() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);
        when(userRoleMapper.selectByUserIdAndRole(1L, "owner")).thenReturn(new ArrayList<>());
        when(userRoleMapper.selectRolesByUserId(1L)).thenReturn(Arrays.asList("admin"));
        when(activityMapper.insert(any(Activity.class))).thenAnswer(invocation -> {
            Activity activity = invocation.getArgument(0);
            activity.setId(1L);
            return 1;
        });

        ActivityController.CreateActivityRequest request = new ActivityController.CreateActivityRequest();
        request.setTitle("新活动");
        request.setToolId(1L);
        request.setType("training");
        request.setDate(LocalDate.now().plusDays(7).toString());
        request.setCover("http://example.com/cover.jpg");
        request.setContent("活动内容");

        // When
        ActivityController.CreateActivityResponse response = activityService.createActivity(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getId());
        verify(activityMapper, times(1)).insert(any(Activity.class));
    }

    /**
     * 测试报名活动
     */
    @Test
    public void testRegisterActivity() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(activityMapper.selectById(1L)).thenReturn(testActivity);
        when(activityRegistrationMapper.selectByActivityAndUser(1L, 2L)).thenReturn(null);
        when(activityRegistrationMapper.insert(any(ActivityRegistration.class))).thenReturn(1);
        when(activityMapper.updateRegisteredCount(1L, 1)).thenReturn(1);
        when(userRoleMapper.selectRolesByUserId(2L)).thenReturn(Arrays.asList("user"));

        // When
        ActivityController.RegisterActivityResponse response = activityService.registerActivity(1L);

        // Then
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertTrue(response.getIsRegistered());
        verify(activityRegistrationMapper, times(1)).insert(any(ActivityRegistration.class));
        verify(activityMapper, times(1)).updateRegisteredCount(1L, 1);
    }

    /**
     * 测试报名活动 - 已报名
     */
    @Test(expected = BusinessException.class)
    public void testRegisterActivityAlreadyRegistered() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(activityMapper.selectById(1L)).thenReturn(testActivity);
        when(activityRegistrationMapper.selectByActivityAndUser(1L, 2L))
                .thenReturn(new ActivityRegistration());

        // When
        activityService.registerActivity(1L);

        // Then - 应该抛出BusinessException
    }

    /**
     * 测试取消报名
     */
    @Test
    public void testCancelRegistration() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(activityMapper.selectById(1L)).thenReturn(testActivity);
        when(activityRegistrationMapper.selectByActivityAndUser(1L, 2L))
                .thenReturn(new ActivityRegistration());
        when(activityRegistrationMapper.delete(1L, 2L)).thenReturn(1);
        when(activityMapper.updateRegisteredCount(1L, -1)).thenReturn(1);

        // When
        ActivityController.CancelRegistrationResponse response = activityService.cancelRegistration(1L);

        // Then
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertFalse(response.getIsRegistered());
        verify(activityRegistrationMapper, times(1)).delete(1L, 2L);
        verify(activityMapper, times(1)).updateRegisteredCount(1L, -1);
    }
}
