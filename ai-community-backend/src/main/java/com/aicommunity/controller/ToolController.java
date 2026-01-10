package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.ToolOwnerResponse;
import com.aicommunity.entity.Tool;
import com.aicommunity.service.ToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工具控制器
 *
 * @author AI Community Team
 */
@Api(tags = "工具管理")
@RestController
@RequestMapping("/tools")
public class ToolController {

    @Autowired
    private ToolService toolService;

    /**
     * 获取工具列表
     */
    @ApiOperation(value = "获取工具列表", notes = "获取所有工具列表")
    @GetMapping
    public Result<List<Tool>> getTools(
            @ApiParam(value = "是否只获取推荐工具")
            @RequestParam(required = false) Boolean featured) {
        List<Tool> tools = toolService.getTools(featured);
        return Result.success(tools);
    }

    /**
     * 获取工具详情
     */
    @ApiOperation(value = "获取工具详情", notes = "获取指定工具的详细信息")
    @GetMapping("/{id}")
    public Result<Tool> getToolDetail(
            @ApiParam(value = "工具ID", required = true)
            @PathVariable Long id) {
        Tool tool = toolService.getToolDetail(id);
        return Result.success(tool);
    }

    /**
     * 检查用户是否为工具Owner
     */
    @ApiOperation(value = "检查用户是否为工具Owner", notes = "检查当前用户是否为指定工具的Owner")
    @GetMapping("/{id}/owner")
    public Result<ToolOwnerResponse> checkToolOwner(
            @ApiParam(value = "工具ID", required = true)
            @PathVariable Long id) {
        ToolOwnerResponse response = toolService.checkToolOwner(id);
        return Result.success(response);
    }
}
