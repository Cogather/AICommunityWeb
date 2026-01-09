package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.ToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 工具控制器
 *
 * @author AI Community Team
 */
@Api(tags = "工具相关接口")
@RestController
@RequestMapping("/tools")
public class ToolController {

    @Autowired
    private ToolService toolService;

    /**
     * 获取工具列表
     *
     * @param featured 是否只获取推荐工具
     * @return 工具列表
     */
    @ApiOperation(value = "获取工具列表", notes = "获取所有工具列表，包括工具Banner")
    @GetMapping
    public Result<?> getTools(
            @ApiParam(value = "是否只获取推荐工具") @RequestParam(required = false) Boolean featured) {
        return Result.success(toolService.getTools(featured));
    }

    /**
     * 获取工具详情
     *
     * @param id 工具ID
     * @return 工具详情
     */
    @ApiOperation(value = "获取工具详情", notes = "获取指定工具的详细信息")
    @GetMapping("/{id}")
    public Result<?> getToolDetail(
            @ApiParam(value = "工具ID", required = true) @PathVariable Long id) {
        return Result.success(toolService.getToolDetail(id));
    }

    /**
     * 检查用户是否为工具Owner
     *
     * @param id 工具ID
     * @return 是否为Owner
     */
    @ApiOperation(value = "检查用户是否为工具Owner", notes = "检查当前用户是否为指定工具的Owner")
    @GetMapping("/{id}/owner")
    public Result<?> checkOwner(
            @ApiParam(value = "工具ID", required = true) @PathVariable Long id) {
        return Result.success(toolService.checkOwner(id));
    }
}
