package com.aicommunity.service;

import com.aicommunity.dto.LoginRequest;
import com.aicommunity.dto.LoginResponse;

/**
 * 认证服务接口
 *
 * @author AI Community Team
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户登出
     */
    void logout();
}
