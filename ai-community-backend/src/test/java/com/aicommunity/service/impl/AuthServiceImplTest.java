package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.dto.LoginRequest;
import com.aicommunity.dto.LoginResponse;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.UserMapper;
import com.aicommunity.util.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.DigestUtils;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 认证服务单元测试
 *
 * @author AI Community Team
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DigestUtils.class})
public class AuthServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private LoginRequest loginRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(DigestUtils.class);

        // 准备测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmployeeId("E001");
        testUser.setName("测试用户");
        testUser.setPassword("5f4dcc3b5aa765d61d8327deb882cf99"); // "password"的MD5
        testUser.setAvatar("http://example.com/avatar.jpg");
        testUser.setDepartment("技术部");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("E001");
        loginRequest.setPassword("password");
    }

    /**
     * 测试登录成功
     */
    @Test
    public void testLoginSuccess() {
        // Given
        when(userMapper.selectByEmployeeId("E001")).thenReturn(testUser);
        PowerMockito.when(DigestUtils.md5DigestAsHex(any(byte[].class)))
                .thenReturn("5f4dcc3b5aa765d61d8327deb882cf99");
        when(jwtUtil.generateToken(1L, "E001")).thenReturn("test-token");

        // When
        LoginResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("test-token", response.getToken());
        assertNotNull(response.getUser());
        assertEquals(1L, response.getUser().getId().longValue());
        assertEquals("测试用户", response.getUser().getName());
        assertEquals("技术部", response.getUser().getDepartment());

        // 验证方法调用
        verify(userMapper, times(1)).selectByEmployeeId("E001");
        verify(jwtUtil, times(1)).generateToken(1L, "E001");
    }

    /**
     * 测试登录失败 - 用户不存在
     */
    @Test(expected = BusinessException.class)
    public void testLoginUserNotFound() {
        // Given
        when(userMapper.selectByEmployeeId("E001")).thenReturn(null);

        // When
        authService.login(loginRequest);

        // Then - 应该抛出BusinessException
    }

    /**
     * 测试登录失败 - 密码错误
     */
    @Test(expected = BusinessException.class)
    public void testLoginWrongPassword() {
        // Given
        when(userMapper.selectByEmployeeId("E001")).thenReturn(testUser);
        PowerMockito.when(DigestUtils.md5DigestAsHex(any(byte[].class)))
                .thenReturn("wrong-password-hash");

        // When
        authService.login(loginRequest);

        // Then - 应该抛出BusinessException
    }

    /**
     * 测试登出
     */
    @Test
    public void testLogout() {
        // When
        authService.logout();

        // Then - 不应该抛出异常
        // TODO: 如果实现了登出逻辑，需要验证相关方法调用
    }
}
