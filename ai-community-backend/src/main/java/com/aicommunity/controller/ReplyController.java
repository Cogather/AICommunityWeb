package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回复控制器
 *
 * @author AI Community Team
 */
@Api(tags = "回复管理")
@RestController
@RequestMapping("/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    /**
     * 删除回复
     */
    @ApiOperation(value = "删除回复", notes = "删除回复")
    @DeleteMapping("/{id}")
    public Result<?> deleteReply(
            @ApiParam(value = "回复ID", required = true)
            @PathVariable Long id) {
        replyService.deleteReply(id);
        return Result.success();
    }
}
