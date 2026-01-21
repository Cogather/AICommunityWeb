package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.ZoneTagService;
import com.aicommunity.vo.ZoneTagListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 专区控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/zones")
@Api(tags = "专区接口")
public class ZoneController {

    @Autowired
    private ZoneTagService zoneTagService;

    /**
     * 获取专区已有标签
     *
     * @param zone   专区类型：practices/tools/agent/empowerment
     * @param toolId 工具ID（可选）
     * @return 标签列表
     */
    @GetMapping("/{zone}/tags")
    @ApiOperation(value = "获取专区已有标签", notes = "获取指定专区的已有标签列表，用于发布帖子时选择标签")
    public Result<ZoneTagListVO> getZoneTags(
            @ApiParam(value = "专区类型：practices/tools/agent/empowerment", required = true, example = "practices")
            @PathVariable String zone,
            @ApiParam(value = "工具ID（zone为tools时可传）", example = "1")
            @RequestParam(required = false) Integer toolId) {
        log.info("获取专区标签, zone={}, toolId={}", zone, toolId);
        ZoneTagListVO result = zoneTagService.getZoneTags(zone, toolId);
        return Result.success(result);
    }
}
