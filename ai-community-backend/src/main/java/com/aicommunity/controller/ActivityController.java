package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.ActivityCreateRequest;
import com.aicommunity.dto.ActivityUpdateRequest;
import com.aicommunity.dto.ActivityRegistrationResponse;
import com.aicommunity.entity.Activity;
import com.aicommunity.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 活动控制器
 *
 * @author AI Community Team
 */
@Api(tags = "活动管理")
@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获取活动列表
     */
    @ApiOperation(value = "获取活动列表", notes = "获取活动列表，支持筛选")
    @GetMapping
    public Result<PageResult<Activity>> getActivities(
            @ApiParam(value = "工具ID")
            @RequestParam(required = false) Long toolId,
            @ApiParam(value = "活动类型")
            @RequestParam(required = false) String type,
            @ApiParam(value = "活动状态")
            @RequestParam(required = false) String status,
            PageQuery pageQuery) {
        PageResult<Activity> result = activityService.getActivities(toolId, type, status, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取活动详情
     */
    @ApiOperation(value = "获取活动详情", notes = "获取指定活动的详细信息")
    @GetMapping("/{id}")
    public Result<Activity> getActivityDetail(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long id) {
        Activity activity = activityService.getActivityDetail(id);
        return Result.success(activity);
    }

    /**
     * 创建活动
     */
    @ApiOperation(value = "创建活动", notes = "发布新活动")
    @PostMapping
    public Result<Activity> createActivity(
            @ApiParam(value = "创建请求", required = true)
            @Valid @RequestBody ActivityCreateRequest request) {
        Activity activity = activityService.createActivity(request);
        return Result.success(activity);
    }

    /**
     * 更新活动
     */
    @ApiOperation(value = "更新活动", notes = "更新活动信息")
    @PutMapping("/{id}")
    public Result<Activity> updateActivity(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "更新请求", required = true)
            @Valid @RequestBody ActivityUpdateRequest request) {
        Activity activity = activityService.updateActivity(id, request);
        return Result.success(activity);
    }

    /**
     * 删除活动
     */
    @ApiOperation(value = "删除活动", notes = "删除活动")
    @DeleteMapping("/{id}")
    public Result<?> deleteActivity(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long id) {
        activityService.deleteActivity(id);
        return Result.success();
    }

    /**
     * 报名活动
     */
    @ApiOperation(value = "报名活动", notes = "报名参加活动")
    @PostMapping("/{id}/register")
    public Result<ActivityRegistrationResponse> registerActivity(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long id) {
        ActivityRegistrationResponse response = activityService.registerActivity(id);
        return Result.success(response);
    }

    /**
     * 取消报名
     */
    @ApiOperation(value = "取消报名", notes = "取消活动报名")
    @DeleteMapping("/{id}/register")
    public Result<ActivityRegistrationResponse> cancelRegistration(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long id) {
        ActivityRegistrationResponse response = activityService.cancelRegistration(id);
        return Result.success(response);
    }

    /**
     * 获取报名用户列表
     */
    @ApiOperation(value = "获取报名用户列表", notes = "获取指定活动的已报名用户列表")
    @GetMapping("/{id}/registrations")
    public Result<PageResult<ActivityRegistrationResponse.RegistrationUser>> getRegistrations(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long id,
            PageQuery pageQuery) {
        PageResult<ActivityRegistrationResponse.RegistrationUser> result = activityService.getRegistrations(id, pageQuery);
        return Result.success(result);
    }
}
