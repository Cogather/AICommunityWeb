package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.ToolService;
import com.aicommunity.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 工具控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/tools")
@Api(tags = "AI工具专区接口")
public class ToolController {

    @Autowired
    private ToolService toolService;

    /**
     * 获取工具列表
     *
     * @param featured 是否只返回推荐工具
     * @return 工具列表
     */
    @GetMapping
    @ApiOperation(value = "获取工具列表", notes = "获取AI工具专区展示的工具列表（与工具专区页面共用同一接口）")
    public Result<ToolListVO> getTools(
            @ApiParam(value = "是否只返回推荐工具", example = "false")
            @RequestParam(required = false) Boolean featured) {
        log.info("获取工具列表, featured={}", featured);
        List<ToolItemVO> list = toolService.getToolList(featured);
        ToolListVO vo = new ToolListVO();
        vo.setList(list);
        return Result.success(vo);
    }

    /**
     * 获取工具帖子列表
     *
     * @param toolId     工具ID，0表示"其他工具"
     * @param category   帖子分类：guide-操作指导，excellent-优秀使用
     * @param tag        标签筛选
     * @param department 部门筛选
     * @param keyword    搜索关键词
     * @param sortBy     排序方式：newest-最新，hot-最热，comments-评论最多
     * @param page       页码，默认1
     * @param pageSize   每页条数，默认15
     * @return 帖子列表
     */
    @GetMapping("/posts")
    @ApiOperation(value = "获取工具帖子列表", notes = "根据工具ID获取相关帖子列表，支持分类筛选、标签筛选、搜索和排序")
    public Result<ToolPostListVO> getToolPosts(
            @ApiParam(value = "工具ID，0表示\"其他工具\"", required = true, example = "1")
            @RequestParam(required = true) Integer toolId,
            @ApiParam(value = "帖子分类：guide-操作指导，excellent-优秀使用", example = "guide")
            @RequestParam(required = false) String category,
            @ApiParam(value = "标签筛选", example = "新手")
            @RequestParam(required = false) String tag,
            @ApiParam(value = "部门筛选", example = "研发部")
            @RequestParam(required = false) String department,
            @ApiParam(value = "搜索关键词", example = "TestMate")
            @RequestParam(required = false) String keyword,
            @ApiParam(value = "排序方式：newest-最新，hot-最热，comments-评论最多", example = "newest")
            @RequestParam(required = false) String sortBy,
            @ApiParam(value = "页码，默认1", example = "1")
            @RequestParam(required = false) Integer page,
            @ApiParam(value = "每页条数，默认15", example = "15")
            @RequestParam(required = false) Integer pageSize) {
        log.info("获取工具帖子列表, toolId={}, category={}, tag={}, department={}, keyword={}, sortBy={}, page={}, pageSize={}",
                toolId, category, tag, department, keyword, sortBy, page, pageSize);
        ToolPostListVO result = toolService.getToolPostList(toolId, category, tag, department, keyword, sortBy, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取工具活动列表
     *
     * @param toolId   工具ID
     * @param status   活动状态：upcoming-即将开始，ongoing-进行中，ended-已结束
     * @param page     页码，默认1
     * @param pageSize 每页条数，默认10
     * @return 活动列表
     */
    @GetMapping("/activities")
    @ApiOperation(value = "获取工具活动列表", notes = "获取指定工具的近期活动与培训信息")
    public Result<ActivityListVO> getActivities(
            @ApiParam(value = "工具ID", required = true, example = "1")
            @RequestParam(required = true) Integer toolId,
            @ApiParam(value = "活动状态：upcoming-即将开始，ongoing-进行中，ended-已结束", example = "upcoming")
            @RequestParam(required = false) String status,
            @ApiParam(value = "页码，默认1", example = "1")
            @RequestParam(required = false) Integer page,
            @ApiParam(value = "每页条数，默认10", example = "10")
            @RequestParam(required = false) Integer pageSize) {
        log.info("获取工具活动列表, toolId={}, status={}, page={}, pageSize={}", toolId, status, page, pageSize);
        ActivityListVO result = toolService.getActivityList(toolId, status, page, pageSize);
        return Result.success(result);
    }

    /**
     * 检查工具Owner权限
     *
     * @param toolId 工具ID
     * @return 权限信息
     */
    @GetMapping("/{toolId}/check-owner")
    @ApiOperation(value = "检查工具Owner权限", notes = "检查当前用户是否为指定工具的Owner")
    public Result<OwnerPermissionVO> checkOwnerPermission(
            @ApiParam(value = "工具ID", required = true, example = "1")
            @PathVariable Integer toolId,
            HttpServletRequest request) {
        log.info("检查工具Owner权限, toolId={}", toolId);
        String userId = getCurrentUserId(request);
        OwnerPermissionVO result = toolService.checkOwnerPermission(toolId, userId);
        return Result.success(result);
    }

    /**
     * 获取标签统计
     *
     * @param toolId     工具ID，0表示"其他工具"
     * @param department 部门筛选，用于联动统计
     * @return 标签列表
     */
    @GetMapping("/{toolId}/tags")
    @ApiOperation(value = "获取标签统计", notes = "获取指定工具下帖子的标签统计信息")
    public Result<TagListVO> getTags(
            @ApiParam(value = "工具ID，0表示\"其他工具\"", required = true, example = "1")
            @PathVariable Integer toolId,
            @ApiParam(value = "部门筛选，用于联动统计", example = "研发部")
            @RequestParam(required = false) String department) {
        log.info("获取标签统计, toolId={}, department={}", toolId, department);
        TagListVO result = toolService.getTags(toolId, department);
        return Result.success(result);
    }

    /**
     * 获取部门统计
     *
     * @param toolId 工具ID，0表示"其他工具"
     * @param tag    标签筛选，用于联动统计
     * @return 部门列表
     */
    @GetMapping("/{toolId}/departments")
    @ApiOperation(value = "获取部门统计", notes = "获取指定工具下帖子的部门归类统计")
    public Result<DepartmentListVO> getDepartments(
            @ApiParam(value = "工具ID，0表示\"其他工具\"", required = true, example = "1")
            @PathVariable Integer toolId,
            @ApiParam(value = "标签筛选，用于联动统计", example = "新手")
            @RequestParam(required = false) String tag) {
        log.info("获取部门统计, toolId={}, tag={}", toolId, tag);
        DepartmentListVO result = toolService.getDepartments(toolId, tag);
        return Result.success(result);
    }

    /**
     * 发布活动
     *
     * @param request 活动创建请求
     * @return 活动创建响应
     */
    @PostMapping("/activities")
    @ApiOperation(value = "发布活动", notes = "工具Owner或管理员发布新活动/培训")
    public Result<ActivityCreateResponseVO> createActivity(
            @ApiParam(value = "活动创建请求", required = true)
            @Valid @RequestBody ActivityCreateRequestVO requestVO,
            HttpServletRequest request) {
        log.info("发布活动, request={}", requestVO);
        String userId = getCurrentUserId(request);
        ActivityCreateResponseVO result = toolService.createActivity(requestVO, userId);
        return Result.success(result);
    }

    /**
     * 获取活动详情
     *
     * @param activityId 活动ID
     * @return 活动详情
     */
    @GetMapping("/activities/{activityId}")
    @ApiOperation(value = "获取活动详情", notes = "获取单个活动的详细信息")
    public Result<ActivityDetailVO> getActivityDetail(
            @ApiParam(value = "活动ID", required = true, example = "1")
            @PathVariable Integer activityId,
            HttpServletRequest request) {
        log.info("获取活动详情, activityId={}", activityId);
        String userId = getCurrentUserId(request);
        ActivityDetailVO result = toolService.getActivityDetail(activityId, userId);
        return Result.success(result);
    }

    /**
     * 报名活动
     *
     * @param activityId 活动ID
     * @return 报名响应
     */
    @PostMapping("/activities/{activityId}/join")
    @ApiOperation(value = "报名活动", notes = "用户报名参加活动")
    public Result<ActivityJoinResponseVO> joinActivity(
            @ApiParam(value = "活动ID", required = true, example = "1")
            @PathVariable Integer activityId,
            HttpServletRequest request) {
        log.info("报名活动, activityId={}", activityId);
        String userId = getCurrentUserId(request);
        ActivityJoinResponseVO result = toolService.joinActivity(activityId, userId);
        return Result.success(result);
    }

    /**
     * 取消报名
     *
     * @param activityId 活动ID
     * @return 响应结果
     */
    @DeleteMapping("/activities/{activityId}/join")
    @ApiOperation(value = "取消报名", notes = "用户取消活动报名")
    public Result<Void> cancelJoin(
            @ApiParam(value = "活动ID", required = true, example = "1")
            @PathVariable Integer activityId,
            HttpServletRequest request) {
        log.info("取消报名, activityId={}", activityId);
        String userId = getCurrentUserId(request);
        toolService.cancelJoin(activityId, userId);
        return Result.success();
    }

    /**
     * 编辑活动
     *
     * @param activityId 活动ID
     * @param requestVO  活动更新请求
     * @param request    HTTP请求对象
     * @return 活动更新响应
     */
    @PutMapping("/activities/{activityId}")
    @ApiOperation(value = "编辑活动", notes = "工具Owner或管理员编辑已有活动信息")
    public Result<ActivityCreateResponseVO> updateActivity(
            @ApiParam(value = "活动ID", required = true, example = "1")
            @PathVariable Integer activityId,
            @ApiParam(value = "活动更新请求", required = true)
            @Valid @RequestBody ActivityUpdateRequestVO requestVO,
            HttpServletRequest request) {
        log.info("编辑活动, activityId={}, request={}", activityId, requestVO);
        String userId = getCurrentUserId(request);
        ActivityCreateResponseVO result = toolService.updateActivity(activityId, requestVO, userId);
        return Result.success(result);
    }

    /**
     * 删除活动
     *
     * @param activityId 活动ID
     * @param request    HTTP请求对象
     * @return 响应结果
     */
    @DeleteMapping("/activities/{activityId}")
    @ApiOperation(value = "删除活动", notes = "工具Owner或管理员删除活动")
    public Result<Void> deleteActivity(
            @ApiParam(value = "活动ID", required = true, example = "1")
            @PathVariable Integer activityId,
            HttpServletRequest request) {
        log.info("删除活动, activityId={}", activityId);
        String userId = getCurrentUserId(request);
        toolService.deleteActivity(activityId, userId);
        return Result.success();
    }

    /**
     * 获取活动报名列表
     *
     * @param activityId 活动ID
     * @param page       页码，默认1
     * @param pageSize   每页数量，默认15
     * @param request    HTTP请求对象
     * @return 报名列表
     */
    @GetMapping("/activities/{activityId}/registrations")
    @ApiOperation(value = "获取活动报名列表", notes = "获取指定活动的已报名用户列表（分页）")
    public Result<ActivityRegistrationListVO> getActivityRegistrations(
            @ApiParam(value = "活动ID", required = true, example = "1")
            @PathVariable Integer activityId,
            @ApiParam(value = "页码，默认1", example = "1")
            @RequestParam(required = false) Integer page,
            @ApiParam(value = "每页数量，默认15", example = "15")
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        log.info("获取活动报名列表, activityId={}, page={}, pageSize={}", activityId, page, pageSize);
        String userId = getCurrentUserId(request);
        ActivityRegistrationListVO result = toolService.getActivityRegistrations(activityId, userId, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取精华帖子（其他工具专有）
     *
     * @param toolId 工具ID，此接口仅支持toolId=0
     * @return 精华帖子响应
     */
    @GetMapping("/featured-post")
    @ApiOperation(value = "获取精华帖子", notes = "获取\"其他工具\"区域的精华/置顶帖子。此接口仅适用于toolId=0的\"其他工具\"")
    public Result<ToolFeaturedPostResponseVO> getFeaturedPost(
            @ApiParam(value = "工具ID，此接口仅支持toolId=0", required = true, example = "0")
            @RequestParam(required = true) Integer toolId) {
        log.info("获取精华帖子, toolId={}", toolId);
        ToolFeaturedPostResponseVO result = toolService.getFeaturedPost(toolId);
        return Result.success(result);
    }

    /**
     * 设置精华帖子（其他工具专有）
     *
     * @param request 设置精华帖子请求
     * @return 设置响应
     */
    @PutMapping("/featured-post")
    @ApiOperation(value = "设置精华帖子", notes = "管理员或Owner设置/取消\"其他工具\"的精华帖子")
    public Result<ToolFeaturedPostSetResponseVO> setFeaturedPost(
            @ApiParam(value = "设置精华帖子请求", required = true)
            @Valid @RequestBody ToolFeaturedPostRequestVO requestVO,
            HttpServletRequest request) {
        log.info("设置精华帖子, toolId={}, postId={}", requestVO.getToolId(), requestVO.getPostId());
        String userId = getCurrentUserId(request);
        ToolFeaturedPostSetResponseVO result = toolService.setFeaturedPost(requestVO, userId);
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
