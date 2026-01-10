package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.TagInfo;
import com.aicommunity.service.TagService;
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
 * 标签控制器
 *
 * @author AI Community Team
 */
@Api(tags = "标签管理")
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 获取标签列表
     */
    @ApiOperation(value = "获取标签列表", notes = "获取指定专区的标签列表")
    @GetMapping
    public Result<List<TagInfo>> getTags(
            @ApiParam(value = "专区", required = true, example = "practices")
            @RequestParam String zone,
            @ApiParam(value = "工具ID")
            @RequestParam(required = false) Long toolId) {
        List<TagInfo> tags = tagService.getTags(zone, toolId);
        return Result.success(tags);
    }
}
