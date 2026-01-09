package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标签控制器
 *
 * @author AI Community Team
 */
@Api(tags = "标签相关接口")
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 获取标签列表
     *
     * @param zone   专区
     * @param toolId 工具ID
     * @return 标签列表
     */
    @ApiOperation(value = "获取标签列表", notes = "获取指定专区的标签列表")
    @GetMapping
    public Result<?> getTags(
            @ApiParam(value = "专区", required = true) @RequestParam String zone,
            @ApiParam(value = "工具ID") @RequestParam(required = false) Long toolId) {
        return Result.success(tagService.getTags(zone, toolId));
    }
}
