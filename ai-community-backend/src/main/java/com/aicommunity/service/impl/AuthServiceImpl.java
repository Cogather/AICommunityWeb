package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.dto.LoginResponse;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.UserMapper;
import com.aicommunity.mapper.UserRoleMapper;
import com.aicommunity.service.AuthService;
import com.aicommunity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

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
    private UserRoleMapper userRoleMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(String username, String password) {
        // 查询用户
        User user = userMapper.selectByEmployeeId(username);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.LOGIN_ERROR);
        }

        // 验证密码（实际应该使用BCrypt等加密方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.LOGIN_ERROR);
        }

        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getName());

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setDepartment(user.getDepartment());

        // 获取用户角色
        List<String> roles = userRoleMapper.selectRolesByUserId(user.getId());
        if (roles.contains("admin")) {
            userInfo.setRole("admin");
        } else {
            userInfo.setRole("user");
        }

        response.setUser(userInfo);
        return response;
    }
}
