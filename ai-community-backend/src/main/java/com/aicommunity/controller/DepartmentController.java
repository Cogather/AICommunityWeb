package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.DepartmentStatsResponse;
import com.aicommunity.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门控制器
 *
 * @author AI Community Team
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取部门统计
     */
    @ApiOperation(value = "获取部门统计", notes = "获取各部门的帖子统计信息")
    @GetMapping("/stats")
    public Result<List<DepartmentStatsResponse>> getDepartmentStats(
            @ApiParam(value = "专区")
            @RequestParam(required = false) String zone) {
        List<DepartmentStatsResponse> stats = departmentService.getDepartmentStats(zone);
        return Result.success(stats);
    }
}
