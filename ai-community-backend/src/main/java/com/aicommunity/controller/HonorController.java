package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
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
@Api(tags = "荣誉相关接口")
@RestController
@RequestMapping("/honors")
public class HonorController {

    @Autowired
    private HonorService honorService;

    /**
     * 获取荣誉列表
     *
     * @param scope      范围：all-全部，mine-我的
     * @param view       视图：grid-荣誉墙，timeline-时光轴
     * @param user       用户名（查看特定用户的时光轴）
     * @param filterType 筛选类型
     * @param filterValue 筛选值
     * @param search     搜索关键词
     * @param page       页码
     * @param pageSize   每页数量
     * @return 荣誉列表
     */
    @ApiOperation(value = "获取荣誉列表", notes = "支持多种筛选、查询和分页功能")
    @GetMapping
    public Result<PageResult<?>> getHonors(
            @ApiParam(value = "范围") @RequestParam(defaultValue = "all") String scope,
            @ApiParam(value = "视图") @RequestParam(defaultValue = "grid") String view,
            @ApiParam(value = "用户名") @RequestParam(required = false) String user,
            @ApiParam(value = "筛选类型") @RequestParam(required = false) String filterType,
            @ApiParam(value = "筛选值") @RequestParam(required = false) String filterValue,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String search,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "16") Integer pageSize) {
        PageQuery pageQuery = new PageQuery(page, pageSize);
        PageResult<?> result = honorService.getHonors(scope, view, user, filterType, filterValue, search, pageQuery);
        return Result.success(result);
    }

    /**
     * 获取荣誉影响力
     *
     * @return 荣誉影响力数据
     */
    @ApiOperation(value = "获取荣誉影响力", notes = "获取荣誉影响力统计数据")
    @GetMapping("/influence")
    public Result<?> getHonorInfluence() {
        return Result.success(honorService.getHonorInfluence());
    }

    /**
     * 给荣誉送花
     *
     * @param id 荣誉ID
     * @return 送花结果
     */
    @ApiOperation(value = "给荣誉送花", notes = "给荣誉送花")
    @PostMapping("/{id}/flower")
    public Result<?> giveFlower(
            @ApiParam(value = "荣誉ID", required = true) @PathVariable Long id) {
        return Result.success(honorService.giveFlower(id));
    }
}
