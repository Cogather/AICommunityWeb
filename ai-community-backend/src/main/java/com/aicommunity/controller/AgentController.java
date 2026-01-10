package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.AgentFeaturedPostResponse;
import com.aicommunity.service.AgentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 扶摇Agent应用控制器
 *
 * @author AI Community Team
 */
@Api(tags = "扶摇Agent应用")
@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    /**
     * 获取扶摇Agent应用置顶帖子
     */
    @ApiOperation(value = "获取扶摇Agent应用置顶帖子", notes = "获取扶摇Agent应用页面的置顶帖子")
    @GetMapping("/featured-post")
    public Result<AgentFeaturedPostResponse> getFeaturedPost() {
        AgentFeaturedPostResponse response = agentService.getFeaturedPost();
        return Result.success(response);
    }
}
