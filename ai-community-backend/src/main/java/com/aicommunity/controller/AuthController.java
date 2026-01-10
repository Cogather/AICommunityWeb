package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.LoginRequest;
import com.aicommunity.dto.LoginResponse;
import com.aicommunity.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户认证控制器
 *
 * @author AI Community Team
 */
@Api(tags = "用户认证")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(response);
    }

    /**
     * 用户登出
     *
     * @return 登出结果
     */
    @ApiOperation(value = "用户登出", notes = "用户登出接口")
    @PostMapping("/logout")
    public Result<?> logout() {
        authService.logout();
        return Result.success();
    }
}
