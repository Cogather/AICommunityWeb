package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.AwardRulesResponse;
import com.aicommunity.entity.Award;
import com.aicommunity.service.AwardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 奖项控制器
 *
 * @author AI Community Team
 */
@Api(tags = "奖项管理")
@RestController
@RequestMapping("/awards")
public class AwardController {

    @Autowired
    private AwardService awardService;

    /**
     * 获取奖项列表
     */
    @ApiOperation(value = "获取奖项列表", notes = "获取奖项列表，支持按分类筛选")
    @GetMapping
    public Result<List<Award>> getAwards(
            @ApiParam(value = "奖项分类")
            @RequestParam(required = false) String category) {
        List<Award> awards = awardService.getAwards(category);
        return Result.success(awards);
    }

    /**
     * 获取奖项规则详情
     */
    @ApiOperation(value = "获取奖项规则详情", notes = "获取指定奖项的详细规则信息")
    @GetMapping("/{id}/rules")
    public Result<AwardRulesResponse> getAwardRules(
            @ApiParam(value = "奖项ID", required = true)
            @PathVariable Long id) {
        AwardRulesResponse response = awardService.getAwardRules(id);
        return Result.success(response);
    }
}
