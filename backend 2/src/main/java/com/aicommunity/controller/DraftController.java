package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.DraftService;
import com.aicommunity.vo.DraftSaveResponseVO;
import com.aicommunity.vo.DraftVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 草稿控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/drafts")
@Api(tags = "草稿接口")
public class DraftController {

    @Autowired
    private DraftService draftService;

    /**
     * 保存草稿
     *
     * @param draft 草稿信息
     * @return 保存响应
     */
    @PostMapping
    @ApiOperation(value = "保存草稿", notes = "将帖子草稿保存到后端（长时间存储）")
    public Result<DraftSaveResponseVO> saveDraft(
            @ApiParam(value = "草稿信息", required = true)
            @Valid @RequestBody DraftVO draft,
            HttpServletRequest request) {
        log.info("保存草稿");
        String userId = getCurrentUserId(request);
        DraftSaveResponseVO result = draftService.saveDraft(draft, userId);
        return Result.success(result);
    }

    /**
     * 获取草稿
     *
     * @param request HTTP请求对象
     * @return 草稿信息
     */
    @GetMapping
    @ApiOperation(value = "获取草稿", notes = "获取当前用户保存在后端的草稿")
    public Result<DraftVO> getDraft(HttpServletRequest request) {
        log.info("获取草稿");
        String userId = getCurrentUserId(request);
        DraftVO result = draftService.getDraft(userId);
        return Result.success(result);
    }

    /**
     * 删除草稿
     *
     * @param request HTTP请求对象
     * @return 响应结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除草稿", notes = "删除当前用户保存在后端的草稿")
    public Result<Void> deleteDraft(HttpServletRequest request) {
        log.info("删除草稿");
        String userId = getCurrentUserId(request);
        draftService.deleteDraft(userId);
        return Result.success();
    }

    /**
     * 从HTTP请求头获取当前登录用户ID
     *
     * @param request HTTP请求对象
     * @return 用户ID，如果未登录则返回null
     */
    private String getCurrentUserId(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }
}
