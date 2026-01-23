package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.CoverService;
import com.aicommunity.vo.CoverListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 封面控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/covers")
@Api(tags = "封面接口")
public class CoverController {

    @Autowired
    private CoverService coverService;

    /**
     * 获取推荐封面
     *
     * @param zone  专区类型（可选）
     * @param count 返回数量，默认3
     * @return 封面列表
     */
    @GetMapping("/recommended")
    @ApiOperation(value = "获取推荐封面", notes = "获取系统推荐的帖子封面图片列表")
    public Result<CoverListVO> getRecommendedCovers(
            @ApiParam(value = "专区类型（可选）", example = "practices")
            @RequestParam(required = false) String zone,
            @ApiParam(value = "返回数量，默认3", example = "3")
            @RequestParam(required = false, defaultValue = "3") Integer count) {
        log.info("获取推荐封面, zone={}, count={}", zone, count);
        CoverListVO result = coverService.getRecommendedCovers(zone, count);
        return Result.success(result);
    }
}
