package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.Result;
import com.aicommunity.service.HonorService;
import com.aicommunity.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 荣誉控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/honor")
@Api(tags = "AI使用达人接口")
public class HonorController {

    @Autowired
    private HonorService honorService;

    /**
     * 获取个人荣誉列表
     *
     * @param pageQuery   分页参数
     * @param scope       筛选范围：all（全员）/mine（我的），默认all
     * @param filterType  筛选类型：award（按奖项）/department（按部门）
     * @param filterValue 筛选值（奖项名称或部门名称）
     * @param keyword     搜索关键词（匹配用户姓名）
     * @param view        视图模式：grid（荣誉墙）/timeline（时光轴）
     * @param userName    时光轴模式下，指定用户姓名查看其荣誉历程
     * @return 荣誉列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取个人荣誉列表", notes = "获取个人荣誉列表，支持分页、筛选和视图切换")
    public Result<HonorListVO> getHonorList(
            PageQuery pageQuery,
            @ApiParam(value = "筛选范围：all（全员）/mine（我的），默认all", example = "all")
            @RequestParam(required = false) String scope,
            @ApiParam(value = "筛选类型：award（按奖项）/department（按部门）", example = "award")
            @RequestParam(required = false) String filterType,
            @ApiParam(value = "筛选值（奖项名称或部门名称）", example = "2026年度 AI 技术突破奖")
            @RequestParam(required = false) String filterValue,
            @ApiParam(value = "搜索关键词（匹配用户姓名）", example = "林星辰")
            @RequestParam(required = false) String keyword,
            @ApiParam(value = "视图模式：grid（荣誉墙）/timeline（时光轴）", example = "grid")
            @RequestParam(required = false) String view,
            @ApiParam(value = "时光轴模式下，指定用户姓名查看其荣誉历程", example = "林星辰")
            @RequestParam(required = false) String userName,
            HttpServletRequest request) {
        log.info("获取个人荣誉列表, page={}, pageSize={}, scope={}, filterType={}, filterValue={}, keyword={}, view={}, userName={}",
                pageQuery.getPage(), pageQuery.getPageSize(), scope, filterType, filterValue, keyword, view, userName);

        String currentUserId = getCurrentUserId(request);

        HonorListVO result = honorService.getHonorList(pageQuery, scope, filterType, filterValue, 
                keyword, view, userName, currentUserId);
        return Result.success(result);
    }

    /**
     * 获取团队奖项列表
     *
     * @param year 按年份筛选
     * @return 团队奖项列表
     */
    @GetMapping("/team-awards")
    @ApiOperation(value = "获取团队奖项列表", notes = "获取团队荣誉奖项列表")
    public Result<TeamAwardListVO> getTeamAwards(
            @ApiParam(value = "按年份筛选", example = "2026")
            @RequestParam(required = false) String year,
            HttpServletRequest request) {
        log.info("获取团队奖项列表, year={}", year);

        String currentUserId = getCurrentUserId(request);

        TeamAwardListVO result = honorService.getTeamAwards(year, currentUserId);
        return Result.success(result);
    }

    /**
     * 获取荣誉影响力排行榜
     *
     * @param limit       返回数量，默认10
     * @param scope       筛选范围：all（全员）/mine（我的），默认all
     * @param filterType  筛选类型：award（按奖项）/department（按部门）
     * @param filterValue 筛选值
     * @return 排行榜列表
     */
    @GetMapping("/leaderboard")
    @ApiOperation(value = "获取荣誉影响力排行榜", notes = "获取荣誉影响力排行榜（按获奖数量和花朵数排序）")
    public Result<LeaderboardVO> getLeaderboard(
            @ApiParam(value = "返回数量，默认10", example = "10")
            @RequestParam(required = false) Integer limit,
            @ApiParam(value = "筛选范围：all（全员）/mine（我的），默认all", example = "all")
            @RequestParam(required = false) String scope,
            @ApiParam(value = "筛选类型：award（按奖项）/department（按部门）", example = "award")
            @RequestParam(required = false) String filterType,
            @ApiParam(value = "筛选值", example = "2026年度 AI 技术突破奖")
            @RequestParam(required = false) String filterValue,
            HttpServletRequest request) {
        log.info("获取荣誉影响力排行榜, limit={}, scope={}, filterType={}, filterValue={}",
                limit, scope, filterType, filterValue);

        String currentUserId = getCurrentUserId(request);

        LeaderboardVO result = honorService.getLeaderboard(limit, scope, filterType, filterValue, currentUserId);
        return Result.success(result);
    }

    /**
     * 送花
     *
     * @param request 送花请求
     * @return 送花响应
     */
    @PostMapping("/flower")
    @ApiOperation(value = "送花", notes = "给荣誉记录送花")
    public Result<FlowerResponseVO> giveFlower(
            @Valid @RequestBody FlowerRequestVO requestVO,
            HttpServletRequest request) {
        log.info("送花, honorId={}, type={}", requestVO.getHonorId(), requestVO.getType());

        String currentUserId = getCurrentUserId(request);
        if (currentUserId == null || currentUserId.isEmpty()) {
            return Result.error(401, "请先登录");
        }

        String type = requestVO.getType() != null ? requestVO.getType() : "individual";
        FlowerResponseVO result = honorService.giveFlower(requestVO.getHonorId(), type, currentUserId);
        return Result.success(result);
    }

    /**
     * 获取奖项名称列表
     *
     * @return 奖项名称列表
     */
    @GetMapping("/awards")
    @ApiOperation(value = "获取奖项名称列表", notes = "获取所有奖项名称列表（用于筛选）")
    public Result<AwardListVO> getAwards() {
        log.info("获取奖项名称列表");
        AwardListVO result = honorService.getAwardNames();
        return Result.success(result);
    }

    /**
     * 获取部门列表
     *
     * @return 部门列表
     */
    @GetMapping("/departments")
    @ApiOperation(value = "获取部门列表", notes = "获取所有部门列表（用于筛选）")
    public Result<AwardListVO> getDepartments() {
        log.info("获取部门列表");
        AwardListVO result = honorService.getDepartments();
        return Result.success(result);
    }

    /**
     * 获取用户荣誉时光轴
     *
     * @param userName 用户姓名，不传则返回所有用户的时光轴
     * @return 时光轴数据
     */
    @GetMapping("/timeline")
    @ApiOperation(value = "获取用户荣誉时光轴", notes = "获取指定用户的荣誉时光轴数据")
    public Result<TimelineVO> getTimeline(
            @ApiParam(value = "用户姓名，不传则返回所有用户的时光轴", example = "林星辰")
            @RequestParam(required = false) String userName,
            HttpServletRequest request) {
        log.info("获取用户荣誉时光轴, userName={}", userName);

        String currentUserId = getCurrentUserId(request);

        TimelineVO result = honorService.getTimeline(userName, currentUserId);
        return Result.success(result);
    }

    /**
     * 保存单个奖项（管理员操作）
     *
     * @param request 保存奖项请求
     * @return 保存结果
     */
    @PostMapping("/awards")
    @ApiOperation(value = "保存单个奖项", notes = "添加或更新单个奖项（管理员操作）")
    public Result<AwardSaveRequestVO> saveAward(@Valid @RequestBody AwardSaveRequestVO request) {
        log.info("保存单个奖项, id={}, name={}", request.getId(), request.getName());

        // TODO: 检查管理员权限
        // if (!isAdmin()) {
        //     return Result.error(403, "无权限操作");
        // }

        AwardSaveRequestVO result = honorService.saveAward(request);
        return Result.success(result);
    }

    /**
     * 删除奖项（管理员操作）
     *
     * @param id 奖项ID（路径参数）
     * @return 删除结果
     */
    @DeleteMapping("/awards/{id}")
    @ApiOperation(value = "删除奖项", notes = "删除指定奖项（管理员操作）")
    public Result<Void> deleteAward(
            @ApiParam(value = "奖项ID", required = true, example = "1")
            @PathVariable Integer id) {
        log.info("删除奖项, id={}", id);

        // TODO: 检查管理员权限
        // if (!isAdmin()) {
        //     return Result.error(403, "无权限操作");
        // }

        honorService.deleteAward(id);
        return Result.success();
    }

    /**
     * 获取最新获奖者列表（首页展示用）
     *
     * @param limit 返回数量，默认9
     * @return 最新获奖者列表
     */
    @GetMapping("/latest-winners")
    @ApiOperation(value = "获取最新获奖者列表", notes = "获取最新的个人获奖者列表，用于首页AI使用达人区域展示")
    public Result<LatestWinnerListVO> getLatestWinners(
            @ApiParam(value = "返回数量，默认9", example = "9")
            @RequestParam(required = false, defaultValue = "9") Integer limit) {
        log.info("获取最新获奖者列表, limit={}", limit);
        LatestWinnerListVO result = honorService.getLatestWinners(limit);
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
