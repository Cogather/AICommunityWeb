package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.HonorInfluenceResponse;
import com.aicommunity.dto.HonorFlowerResponse;
import com.aicommunity.entity.Honor;
import com.aicommunity.service.HonorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 荣誉控制器
 *
 * @author AI Community Team
 */
@Api(tags = "荣誉管理")
@RestController
@RequestMapping("/honors")
public class HonorController {

    @Autowired
    private HonorService honorService;

    /**
     * 获取荣誉列表
     */
    @ApiOperation(value = "获取荣誉列表", notes = "支持多种筛选、查询和分页功能")
    @GetMapping
    public Result<PageResult<Honor>> getHonors(
            @ApiParam(value = "范围：all-全部，mine-我的荣誉")
            @RequestParam(required = false, defaultValue = "all") String scope,
            @ApiParam(value = "视图：grid-荣誉墙，timeline-时光轴")
            @RequestParam(required = false, defaultValue = "grid") String view,
            @ApiParam(value = "用户名（查看特定用户的时光轴）")
            @RequestParam(required = false) String user,
            @ApiParam(value = "筛选类型：category-类别，award-奖项，department-部门")
            @RequestParam(required = false) String filterType,
            @ApiParam(value = "筛选值")
            @RequestParam(required = false) String filterValue,
            @ApiParam(value = "搜索用户名")
            @RequestParam(required = false) String search,
            PageQuery pageQuery) {
        PageResult<Honor> result = honorService.getHonors(scope, view, user, filterType, filterValue, search, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取荣誉影响力
     */
    @ApiOperation(value = "获取荣誉影响力", notes = "获取荣誉影响力统计数据")
    @GetMapping("/influence")
    public Result<HonorInfluenceResponse> getHonorInfluence() {
        HonorInfluenceResponse response = honorService.getHonorInfluence();
        return Result.success(response);
    }

    /**
     * 获取AI使用达人Top用户
     */
    @ApiOperation(value = "获取AI使用达人Top用户", notes = "用于首页展示的AI使用达人")
    @GetMapping("/top-users")
    public Result<PageResult<Honor.TopUser>> getTopUsers(
            @ApiParam(value = "返回数量", example = "6")
            @RequestParam(required = false, defaultValue = "6") Integer limit) {
        PageResult<Honor.TopUser> result = honorService.getTopUsers(limit);
        return Result.success(result);
    }

    /**
     * 给荣誉送花
     */
    @ApiOperation(value = "给荣誉送花", notes = "给荣誉送花")
    @PostMapping("/{id}/flower")
    public Result<HonorFlowerResponse> giveFlower(
            @ApiParam(value = "荣誉ID", required = true)
            @PathVariable Long id) {
        HonorFlowerResponse response = honorService.giveFlower(id);
        return Result.success(response);
    }
}
