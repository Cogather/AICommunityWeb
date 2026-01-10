package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.AwardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 奖项控制器
 *
 * @author AI Community Team
 */
@Api(tags = "奖项相关接口")
@RestController
@RequestMapping("/awards")
public class AwardController {

    @Autowired
    private AwardService awardService;

    /**
     * 获取奖项列表
     *
     * @param category 奖项分类
     * @return 奖项列表
     */
    @ApiOperation(value = "获取奖项列表", notes = "获取奖项列表，支持按分类筛选")
    @GetMapping
    public Result<?> getAwards(
            @ApiParam(value = "奖项分类") @RequestParam(required = false) String category) {
        return Result.success(awardService.getAwards(category));
    }

    /**
     * 获取奖项规则
     *
     * @param id 奖项ID
     * @return 奖项规则
     */
    @ApiOperation(value = "获取奖项规则", notes = "获取指定奖项的详细规则信息")
    @GetMapping("/{id}/rules")
    public Result<?> getAwardRules(
            @ApiParam(value = "奖项ID", required = true) @PathVariable Long id) {
        return Result.success(awardService.getAwardRules(id));
    }
}
