package com.aicommunity.controller;

import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.Result;
import com.aicommunity.service.UserService;
import com.aicommunity.vo.LoginRequestVO;
import com.aicommunity.vo.LoginResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户认证控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Api(tags = "用户认证接口")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求参数
     * @return 登录响应（包含token和用户信息）
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "通过工号和密码进行登录认证，获取访问令牌")
    public Result<LoginResponseVO> login(@Valid @RequestBody LoginRequestVO loginRequest) {
        log.info("用户登录请求，工号：{}", loginRequest.getEmployeeId());
        try {
            LoginResponseVO response = userService.login(loginRequest);
            return Result.success(response);
        } catch (RuntimeException e) {
            log.error("登录失败：{}", e.getMessage());
            if ("工号不存在".equals(e.getMessage())) {
                return Result.error(40001, "工号不存在");
            } else if ("密码错误".equals(e.getMessage())) {
                return Result.error(40002, "密码错误");
            } else if ("账号已被禁用".equals(e.getMessage())) {
                return Result.error(40003, "账号已被禁用");
            } else {
                return Result.error(ErrorCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
            }
        } catch (Exception e) {
            log.error("登录异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "登录失败，请稍后重试");
        }
    }

    /**
     * 用户退出登录
     *
     * @param userId 用户ID（从请求头或token中获取）
     * @return 退出结果
     */
    @PostMapping("/logout")
    @ApiOperation(value = "退出登录", notes = "用户退出登录，使当前token失效")
    public Result<Void> logout(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("用户退出登录，用户ID：{}", userId);
        try {
            if (userId != null) {
                userService.logout(userId);
            }
            return Result.success();
        } catch (Exception e) {
            log.error("退出登录异常", e);
            return Result.error(ErrorCodeEnum.INTERNAL_ERROR.getCode(), "退出登录失败");
        }
    }
}
