package com.aicommunity.service;

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
     * @param username 用户名
     * @param password 密码
     * @return 登录响应
     */
    LoginResponse login(String username, String password);
}
