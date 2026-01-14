package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.ToolService;
import com.aicommunity.vo.ActivityRegistrationListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 活动控制器
 * 提供活动相关的通用接口（用于个人中心等页面）
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Slf4j
@RestController
@RequestMapping("/api/activities")
@Api(tags = "活动接口")
public class ActivityController {

    @Autowired
    private ToolService toolService;

    /**
     * 获取活动报名用户列表
     * 此接口用于个人中心等页面查看活动报名详情
     *
     * @param activityId 活动ID
     * @param page       页码，默认1
     * @param pageSize   每页数量，默认100
     * @param request    HTTP请求对象
     * @return 报名列表
     */
    @GetMapping("/{activityId}/registrations")
    @ApiOperation(value = "获取活动报名用户列表", notes = "获取指定活动的所有报名用户信息，用于个人中心等页面")
    public Result<ActivityRegistrationListVO> getActivityRegistrations(
            @ApiParam(value = "活动ID", required = true, example = "1")
            @PathVariable Integer activityId,
            @ApiParam(value = "页码，默认1", example = "1")
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量，默认100", example = "100")
            @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            HttpServletRequest request) {
        log.info("获取活动报名用户列表, activityId={}, page={}, pageSize={}", activityId, page, pageSize);
        String userId = getCurrentUserId(request);
        ActivityRegistrationListVO result = toolService.getActivityRegistrations(activityId, userId, page, pageSize);
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
