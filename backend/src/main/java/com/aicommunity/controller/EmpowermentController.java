package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.EmpowermentService;
import com.aicommunity.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 赋能交流控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/empowerment")
@Api(tags = "赋能交流接口")
public class EmpowermentController {

    @Autowired
    private EmpowermentService empowermentService;

    /**
     * 获取精华帖子列表
     *
     * @return 精华帖子列表响应
     */
    @GetMapping("/featured-posts")
    @ApiOperation(value = "获取精华帖子列表", notes = "获取赋能交流页面的精华/置顶帖子。精华帖子始终在帖子列表顶部显示，不参与分页和筛选。")
    public Result<FeaturedPostListVO> getFeaturedPosts() {
        log.info("获取精华帖子列表");
        FeaturedPostListVO result = empowermentService.getFeaturedPosts();
        return Result.success(result);
    }

    /**
     * 获取帖子列表
     *
     * @param tag      标签筛选
     * @param keyword  搜索关键词（匹配标题、作者、描述）
     * @param sortBy   排序方式：newest-最新，hot-最热（浏览量），comments-评论最多，likes-点赞最多
     * @param page     页码，默认1
     * @param pageSize 每页条数，默认15
     * @return 帖子列表响应（带分页）
     */
    @GetMapping("/posts")
    @ApiOperation(value = "获取帖子列表", notes = "获取赋能交流页面的普通帖子列表，支持搜索、标签筛选、排序和分页。此接口只返回普通帖子，不包含精华帖子。")
    public Result<EmpowermentPostListVO> getPosts(
            @ApiParam(value = "标签筛选", example = "讨论")
            @RequestParam(required = false) String tag,
            @ApiParam(value = "搜索关键词（匹配标题、作者、描述）", example = "Agent")
            @RequestParam(required = false) String keyword,
            @ApiParam(value = "排序方式：newest-最新，hot-最热，comments-评论最多，likes-点赞最多", example = "newest")
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            @ApiParam(value = "页码，默认1", example = "1")
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页条数，默认15", example = "15")
            @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        log.info("获取帖子列表, tag={}, keyword={}, sortBy={}, page={}, pageSize={}", 
                tag, keyword, sortBy, page, pageSize);
        EmpowermentPostListVO result = empowermentService.getPosts(keyword, tag, sortBy, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取标签统计
     *
     * @return 标签统计列表响应
     */
    @GetMapping("/tags")
    @ApiOperation(value = "获取标签统计", notes = "获取赋能交流页面的标签统计信息，用于标签筛选。标签统计包含所有帖子（精华+普通）。")
    public Result<TagStatListVO> getTags() {
        log.info("获取标签统计");
        TagStatListVO result = empowermentService.getTags();
        return Result.success(result);
    }

    /**
     * 获取精选合集
     *
     * @param limit 返回数量，默认5
     * @return 精选合集列表响应
     */
    @GetMapping("/collections")
    @ApiOperation(value = "获取精选合集", notes = "获取侧边栏展示的精选合集信息。")
    public Result<CollectionListVO> getCollections(
            @ApiParam(value = "返回数量，默认5", example = "5")
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        log.info("获取精选合集, limit={}", limit);
        CollectionListVO result = empowermentService.getCollections(limit);
        return Result.success(result);
    }

    /**
     * 设置/取消精华帖子
     *
     * @param request 设置精华帖子请求
     * @return 设置响应
     */
    @PutMapping("/featured-posts")
    @ApiOperation(value = "设置精华帖子", notes = "管理员设置/取消精华帖子。")
    public Result<FeaturedPostSetResponseVO> setFeaturedPost(
            @ApiParam(value = "设置精华帖子请求", required = true)
            @Valid @RequestBody FeaturedPostSetRequestVO request) {
        log.info("设置精华帖子, postId={}, featured={}", request.getPostId(), request.getFeatured());
        FeaturedPostSetResponseVO result = empowermentService.setFeaturedPost(
                request.getPostId(), request.getFeatured());
        return Result.success(result);
    }
}
