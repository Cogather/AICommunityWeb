package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.ActivityDetailDTO;
import com.aicommunity.dto.ActivityListDTO;
import com.aicommunity.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 活动控制器
 *
 * @author AI Community Team
 */
@Api(tags = "活动相关接口")
@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获取活动列表
     *
     * @param toolId  工具ID
     * @param type    活动类型
     * @param status  活动状态
     * @param page    页码
     * @param pageSize 每页数量
     * @return 活动列表
     */
    @ApiOperation(value = "获取活动列表", notes = "获取活动列表，支持多条件查询")
    @GetMapping
    public Result<PageResult<ActivityListDTO>> getActivities(
            @ApiParam(value = "工具ID") @RequestParam(required = false) Long toolId,
            @ApiParam(value = "活动类型") @RequestParam(required = false) String type,
            @ApiParam(value = "活动状态") @RequestParam(required = false) String status,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<ActivityListDTO> result = activityService.getActivities(toolId, type, status, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取活动详情
     *
     * @param id 活动ID
     * @return 活动详情
     */
    @ApiOperation(value = "获取活动详情", notes = "获取指定活动的详细信息")
    @GetMapping("/{id}")
    public Result<ActivityDetailDTO> getActivityDetail(
            @ApiParam(value = "活动ID", required = true) @PathVariable Long id) {
        ActivityDetailDTO activity = activityService.getActivityDetail(id);
        return Result.success(activity);
    }

    /**
     * 创建活动
     *
     * @param request 创建请求
     * @return 创建结果
     */
    @ApiOperation(value = "创建活动", notes = "发布新活动")
    @PostMapping
    public Result<CreateActivityResponse> createActivity(@Valid @RequestBody CreateActivityRequest request) {
        CreateActivityResponse response = activityService.createActivity(request);
        return Result.success(response);
    }

    /**
     * 更新活动
     *
     * @param id      活动ID
     * @param request 更新请求
     * @return 更新结果
     */
    @ApiOperation(value = "更新活动", notes = "更新活动信息")
    @PutMapping("/{id}")
    public Result<UpdateActivityResponse> updateActivity(
            @ApiParam(value = "活动ID", required = true) @PathVariable Long id,
            @Valid @RequestBody CreateActivityRequest request) {
        UpdateActivityResponse response = activityService.updateActivity(id, request);
        return Result.success(response);
    }

    /**
     * 删除活动
     *
     * @param id 活动ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除活动", notes = "删除活动")
    @DeleteMapping("/{id}")
    public Result<?> deleteActivity(
            @ApiParam(value = "活动ID", required = true) @PathVariable Long id) {
        activityService.deleteActivity(id);
        return Result.success();
    }

    /**
     * 报名活动
     *
     * @param id 活动ID
     * @return 报名结果
     */
    @ApiOperation(value = "报名活动", notes = "报名参加活动")
    @PostMapping("/{id}/register")
    public Result<RegisterActivityResponse> registerActivity(
            @ApiParam(value = "活动ID", required = true) @PathVariable Long id) {
        RegisterActivityResponse response = activityService.registerActivity(id);
        return Result.success(response);
    }

    /**
     * 取消报名
     *
     * @param id 活动ID
     * @return 取消报名结果
     */
    @ApiOperation(value = "取消报名", notes = "取消活动报名")
    @DeleteMapping("/{id}/register")
    public Result<CancelRegistrationResponse> cancelRegistration(
            @ApiParam(value = "活动ID", required = true) @PathVariable Long id) {
        CancelRegistrationResponse response = activityService.cancelRegistration(id);
        return Result.success(response);
    }

    /**
     * 获取活动报名用户列表
     *
     * @param id       活动ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 报名用户列表
     */
    @ApiOperation(value = "获取活动报名用户列表", notes = "获取指定活动的已报名用户列表")
    @GetMapping("/{id}/registrations")
    public Result<PageResult<Map<String, Object>>> getRegistrations(
            @ApiParam(value = "活动ID", required = true) @PathVariable Long id,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "20") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<Map<String, Object>> result = activityService.getRegistrations(id, pageQuery);
        return Result.success(result);
    }

    /**
     * 创建活动请求DTO
     */
    public static class CreateActivityRequest {
        private String title;
        private Long toolId;
        private String type; // "training" | "competition" | "sharing"
        private String date; // YYYY-MM-DD
        private String cover;
        private String content;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Long getToolId() { return toolId; }
        public void setToolId(Long toolId) { this.toolId = toolId; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getCover() { return cover; }
        public void setCover(String cover) { this.cover = cover; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    /**
     * 创建活动响应DTO
     */
    public static class CreateActivityResponse {
        private Long id;
        private String message;

        public CreateActivityResponse(Long id, String message) {
            this.id = id;
            this.message = message;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * 更新活动响应DTO
     */
    public static class UpdateActivityResponse {
        private Long id;
        private String message;

        public UpdateActivityResponse(Long id, String message) {
            this.id = id;
            this.message = message;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * 报名活动响应DTO
     */
    public static class RegisterActivityResponse {
        private Boolean success;
        private String message;
        private Boolean isRegistered;
        private Integer registeredCount;

        public RegisterActivityResponse(Boolean success, String message, Boolean isRegistered, Integer registeredCount) {
            this.success = success;
            this.message = message;
            this.isRegistered = isRegistered;
            this.registeredCount = registeredCount;
        }

        public Boolean getSuccess() { return success; }
        public void setSuccess(Boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Boolean getIsRegistered() { return isRegistered; }
        public void setIsRegistered(Boolean isRegistered) { this.isRegistered = isRegistered; }
        public Integer getRegisteredCount() { return registeredCount; }
        public void setRegisteredCount(Integer registeredCount) { this.registeredCount = registeredCount; }
    }

    /**
     * 取消报名响应DTO
     */
    public static class CancelRegistrationResponse {
        private Boolean success;
        private String message;
        private Boolean isRegistered;
        private Integer registeredCount;

        public CancelRegistrationResponse(Boolean success, String message, Boolean isRegistered, Integer registeredCount) {
            this.success = success;
            this.message = message;
            this.isRegistered = isRegistered;
            this.registeredCount = registeredCount;
        }

        public Boolean getSuccess() { return success; }
        public void setSuccess(Boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Boolean getIsRegistered() { return isRegistered; }
        public void setIsRegistered(Boolean isRegistered) { this.isRegistered = isRegistered; }
        public Integer getRegisteredCount() { return registeredCount; }
        public void setRegisteredCount(Integer registeredCount) { this.registeredCount = registeredCount; }
    }
}
