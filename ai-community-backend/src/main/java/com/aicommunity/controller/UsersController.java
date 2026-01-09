package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户列表控制器（复数形式，用于获取多个用户）
 *
 * @author AI Community Team
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    /**
     * 获取热门贡献者
     *
     * @param zone  专区
     * @param limit 返回数量
     * @return 贡献者列表
     */
    @ApiOperation(value = "获取热门贡献者", notes = "获取指定专区的热门贡献者列表")
    @GetMapping("/top-contributors")
    public Result<?> getTopContributors(
            @ApiParam(value = "专区", required = false) @RequestParam(required = false) String zone,
            @ApiParam(value = "返回数量", required = false) @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(userService.getTopContributors(zone, limit));
    }
}
