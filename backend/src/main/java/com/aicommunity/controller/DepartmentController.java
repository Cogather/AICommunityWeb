package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 部门控制器
 *
 * @author AI Community Team
 */
@Api(tags = "部门相关接口")
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取部门统计
     *
     * @param zone 专区
     * @return 部门统计列表
     */
    @ApiOperation(value = "获取部门统计", notes = "获取各部门的帖子统计信息")
    @GetMapping("/stats")
    public Result<?> getStats(
            @ApiParam(value = "专区") @RequestParam(required = false) String zone) {
        return Result.success(departmentService.getStats(zone));
    }
}
