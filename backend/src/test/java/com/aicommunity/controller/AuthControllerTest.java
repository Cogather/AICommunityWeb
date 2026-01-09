package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.LoginRequest;
import com.aicommunity.dto.LoginResponse;
import com.aicommunity.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 认证控制器单元测试
 *
 * @author AI Community Team
 */
@RunWith(PowerMockRunner.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // 准备测试数据
        loginRequest = new LoginRequest();
        loginRequest.setUsername("E001");
        loginRequest.setPassword("password");

        loginResponse = new LoginResponse();
        loginResponse.setToken("test-token");

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(1L);
        userInfo.setName("测试用户");
        userInfo.setRole("user");
        loginResponse.setUser(userInfo);
    }

    /**
     * 测试登录接口
     */
    @Test
    public void testLogin() {
        // Given
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // When
        Result<LoginResponse> result = authController.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        assertNotNull(result.getData());
        assertEquals("test-token", result.getData().getToken());
        assertEquals(1L, result.getData().getUser().getId().longValue());

        // 验证Service方法被调用
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    /**
     * 测试登出接口
     */
    @Test
    public void testLogout() {
        // Given
        doNothing().when(authService).logout();

        // When
        Result<?> result = authController.logout();

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());

        // 验证Service方法被调用
        verify(authService, times(1)).logout();
    }
}
