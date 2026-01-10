package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.TopContributorResponse;
import com.aicommunity.service.UserContributorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户贡献者控制器
 *
 * @author AI Community Team
 */
@Api(tags = "用户贡献者")
@RestController
@RequestMapping("/users")
public class UserContributorController {

    @Autowired
    private UserContributorService userContributorService;

    /**
     * 获取热门贡献者
     */
    @ApiOperation(value = "获取热门贡献者", notes = "获取指定专区的热门贡献者列表")
    @GetMapping("/top-contributors")
    public Result<List<TopContributorResponse>> getTopContributors(
            @ApiParam(value = "专区")
            @RequestParam(required = false) String zone,
            @ApiParam(value = "返回数量", example = "10")
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<TopContributorResponse> contributors = userContributorService.getTopContributors(zone, limit);
        return Result.success(contributors);
    }
}
