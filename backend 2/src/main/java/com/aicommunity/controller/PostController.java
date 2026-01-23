package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.PostService;
import com.aicommunity.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 帖子控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/posts")
@Api(tags = "帖子接口")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 获取帖子详情
     *
     * @param id     帖子ID
     * @param request HTTP请求对象
     * @return 帖子详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取帖子详情", notes = "获取指定帖子的详细信息，包括内容、作者信息、互动数据等")
    public Result<PostDetailVO> getPostDetail(
            @ApiParam(value = "帖子ID", required = true, example = "1")
            @PathVariable String id,
            HttpServletRequest request) {
        log.info("获取帖子详情, postId={}", id);
        String userId = getCurrentUserId(request);
        PostDetailVO result = postService.getPostDetail(id, userId);
        return Result.success(result);
    }

    /**
     * 创建帖子
     *
     * @param requestVO 创建帖子请求
     * @param request   HTTP请求对象
     * @return 帖子详情
     */
    @PostMapping
    @ApiOperation(value = "创建帖子", notes = "创建新帖子")
    public Result<PostDetailVO> createPost(
            @ApiParam(value = "创建帖子请求", required = true)
            @Valid @RequestBody PostCreateRequestVO requestVO,
            HttpServletRequest request) {
        log.info("创建帖子, title={}, zone={}", requestVO.getTitle(), requestVO.getZone());
        String userId = getCurrentUserId(request);
        PostDetailVO result = postService.createPost(requestVO, userId);
        return Result.success(result);
    }

    /**
     * 更新帖子
     *
     * @param id        帖子ID
     * @param requestVO 更新帖子请求
     * @param request   HTTP请求对象
     * @return 帖子详情
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新帖子", notes = "更新指定帖子的内容")
    public Result<PostDetailVO> updatePost(
            @ApiParam(value = "帖子ID", required = true, example = "1")
            @PathVariable String id,
            @ApiParam(value = "更新帖子请求", required = true)
            @Valid @RequestBody PostUpdateRequestVO requestVO,
            HttpServletRequest request) {
        log.info("更新帖子, postId={}", id);
        String userId = getCurrentUserId(request);
        PostDetailVO result = postService.updatePost(id, requestVO, userId);
        return Result.success(result);
    }

    /**
     * 删除帖子
     *
     * @param id      帖子ID
     * @param request HTTP请求对象
     * @return 响应结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除帖子", notes = "删除指定帖子")
    public Result<Void> deletePost(
            @ApiParam(value = "帖子ID", required = true, example = "1")
            @PathVariable String id,
            HttpServletRequest request) {
        log.info("删除帖子, postId={}", id);
        String userId = getCurrentUserId(request);
        postService.deletePost(id, userId);
        return Result.success();
    }

    /**
     * 点赞/取消点赞帖子
     *
     * @param id        帖子ID
     * @param requestVO 点赞操作请求
     * @param request   HTTP请求对象
     * @return 点赞响应
     */
    @PostMapping("/{id}/like")
    @ApiOperation(value = "点赞/取消点赞帖子", notes = "对帖子进行点赞或取消点赞操作")
    public Result<LikeResponseVO> likePost(
            @ApiParam(value = "帖子ID", required = true, example = "1")
            @PathVariable String id,
            @ApiParam(value = "点赞操作请求", required = true)
            @Valid @RequestBody LikeActionRequestVO requestVO,
            HttpServletRequest request) {
        log.info("点赞帖子, postId={}, action={}", id, requestVO.getAction());
        String userId = getCurrentUserId(request);
        LikeResponseVO result = postService.likePost(id, requestVO.getAction(), userId);
        return Result.success(result);
    }

    /**
     * 收藏/取消收藏帖子
     *
     * @param id        帖子ID
     * @param requestVO 收藏操作请求
     * @param request   HTTP请求对象
     * @return 收藏响应
     */
    @PostMapping("/{id}/collect")
    @ApiOperation(value = "收藏/取消收藏帖子", notes = "对帖子进行收藏或取消收藏操作")
    public Result<CollectResponseVO> collectPost(
            @ApiParam(value = "帖子ID", required = true, example = "1")
            @PathVariable String id,
            @ApiParam(value = "收藏操作请求", required = true)
            @Valid @RequestBody CollectActionRequestVO requestVO,
            HttpServletRequest request) {
        log.info("收藏帖子, postId={}, action={}", id, requestVO.getAction());
        String userId = getCurrentUserId(request);
        CollectResponseVO result = postService.collectPost(id, requestVO.getAction(), userId);
        return Result.success(result);
    }

    /**
     * 设置帖子精华/置顶状态（管理员）
     *
     * @param id        帖子ID
     * @param requestVO 设置请求
     * @param request   HTTP请求对象
     * @return 设置响应
     */
    @PutMapping("/{id}/featured")
    @ApiOperation(value = "设置帖子精华/置顶状态", notes = "管理员设置帖子精华/置顶状态。不同区域有不同的功能名称：AI优秀实践-精华，赋能交流-精华，扶摇Agent应用-置顶，AI工具专区-其他工具-精华")
    public Result<PostFeaturedSetResponseVO> setFeaturedPost(
            @ApiParam(value = "帖子ID", required = true, example = "1")
            @PathVariable String id,
            @ApiParam(value = "设置请求", required = true)
            @Valid @RequestBody PostFeaturedSetRequestVO requestVO,
            HttpServletRequest request) {
        log.info("设置帖子精华/置顶, postId={}, featured={}, zone={}, toolId={}", id, requestVO.getFeatured(), requestVO.getZone(), requestVO.getToolId());
        String userId = getCurrentUserId(request);
        PostFeaturedSetResponseVO result = postService.setFeaturedPost(id, requestVO, userId);
        return Result.success(result);
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
