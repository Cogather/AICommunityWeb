package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.dto.LoginRequest;
import com.aicommunity.dto.LoginResponse;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.UserMapper;
import com.aicommunity.service.AuthService;
import com.aicommunity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 认证服务实现类
 *
 * @author AI Community Team
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 根据用户名（工号）查询用户
        User user = userMapper.selectByEmployeeId(request.getUsername());
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 验证密码（这里简化处理，实际应该使用BCrypt等加密方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getEmployeeId());

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setDepartment(user.getDepartment());
        // TODO: 查询用户角色
        userInfo.setRole("user");

        response.setUser(userInfo);
        return response;
    }

    @Override
    public void logout() {
        // TODO: 实现登出逻辑（如清除Token缓存等）
    }
}
