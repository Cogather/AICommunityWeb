package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.AgentService;
import com.aicommunity.vo.AgentFeaturedPostRequestVO;
import com.aicommunity.vo.AgentFeaturedPostResponseVO;
import com.aicommunity.vo.AgentFeaturedPostSetResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 扶摇Agent应用控制器
 * 提供扶摇Agent应用页面的专有接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/agent")
@Api(tags = "扶摇Agent应用接口")
public class AgentController {

    @Autowired
    private AgentService agentService;

    /**
     * 获取置顶/精选帖子
     * 获取扶摇Agent应用页面的置顶/精选帖子
     * 精华帖子在帖子列表外单独展示（大图卡片样式），不参与分页、搜索、标签筛选和排序
     *
     * @return 置顶帖子信息
     */
    @GetMapping("/featured-post")
    @ApiOperation(value = "获取置顶/精选帖子", notes = "获取扶摇Agent应用页面的置顶/精选帖子，精华帖子在帖子列表外单独展示")
    public Result<AgentFeaturedPostResponseVO> getFeaturedPost() {
        log.info("获取扶摇Agent应用置顶/精选帖子");
        AgentFeaturedPostResponseVO result = agentService.getFeaturedPost();
        return Result.success(result);
    }

    /**
     * 设置置顶帖子
     * Owner或管理员设置/取消置顶帖子
     *
     * @param request 设置置顶请求
     * @return 设置结果
     */
    @PutMapping("/featured-post")
    @ApiOperation(value = "设置置顶帖子", notes = "Owner或管理员设置/取消置顶帖子")
    public Result<AgentFeaturedPostSetResponseVO> setFeaturedPost(
            @ApiParam(value = "设置置顶请求", required = true)
            @Valid @RequestBody AgentFeaturedPostRequestVO requestVO,
            HttpServletRequest request) {
        log.info("设置扶摇Agent应用置顶帖子, postId={}", requestVO.getPostId());
        String userId = getCurrentUserId(request);
        AgentFeaturedPostSetResponseVO result = agentService.setFeaturedPost(requestVO.getPostId(), userId);
        return Result.success(result);
    }

    /**
     * 从HTTP请求头获取当前登录用户ID
     *
     * @param request HTTP请求对象
     * @return 用户ID，如果未登录则返回null
     */
    private String getCurrentUserId(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }
}
