package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.PracticeService;
import com.aicommunity.vo.ContributorListVO;
import com.aicommunity.vo.DepartmentListVO;
import com.aicommunity.vo.HotPostListVO;
import com.aicommunity.vo.PostListVO;
import com.aicommunity.vo.TagListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AI优秀实践控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/practices")
@Api(tags = "AI优秀实践接口")
public class PracticeController {

    @Autowired
    private PracticeService practiceService;

    /**
     * 获取AI优秀实践帖子列表
     * 支持分页、搜索、筛选和排序
     *
     * @param page        当前页码，默认1
     * @param pageSize    每页数量，默认15，可选10/15/20/30/50
     * @param keyword     搜索关键词（匹配标题、作者、描述）
     * @param tag         按标签筛选
     * @param department  按部门筛选
     * @param contributor 按贡献者筛选
     * @param sortBy      排序方式：newest（最新）/hot（最热）/comments（评论数）/likes（点赞数），默认newest
     * @return 帖子列表
     */
    @GetMapping("/posts")
    @ApiOperation(value = "获取AI优秀实践帖子列表", notes = "获取AI优秀实践帖子列表，支持分页、搜索、筛选和排序")
    public Result<PostListVO> getPosts(
            @ApiParam(value = "当前页码，默认1", example = "1")
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量，默认15，可选10/15/20/30/50", example = "15")
            @RequestParam(required = false, defaultValue = "15") Integer pageSize,
            @ApiParam(value = "搜索关键词（匹配标题、作者、描述）", example = "AI")
            @RequestParam(required = false) String keyword,
            @ApiParam(value = "按标签筛选", example = "机器学习")
            @RequestParam(required = false) String tag,
            @ApiParam(value = "按部门筛选", example = "研发部")
            @RequestParam(required = false) String department,
            @ApiParam(value = "按贡献者筛选", example = "张三")
            @RequestParam(required = false) String contributor,
            @ApiParam(value = "排序方式：newest（最新）/hot（最热）/comments（评论数）/likes（点赞数），默认newest", example = "newest")
            @RequestParam(required = false, defaultValue = "newest") String sortBy) {
        log.info("获取AI优秀实践帖子列表, page={}, pageSize={}, keyword={}, tag={}, department={}, contributor={}, sortBy={}",
                page, pageSize, keyword, tag, department, contributor, sortBy);
        PostListVO result = practiceService.getPostList(page, pageSize, keyword, tag, department, contributor, sortBy);
        return Result.success(result);
    }

    /**
     * 获取最热帖子Top N
     * 按浏览量排序，用于右侧边栏展示
     *
     * @param limit 返回数量，默认3
     * @return 最热帖子列表
     */
    @GetMapping("/hot-posts")
    @ApiOperation(value = "获取最热帖子Top N", notes = "获取最热帖子Top N（按浏览量排序，用于右侧边栏展示）")
    public Result<HotPostListVO> getHotPosts(
            @ApiParam(value = "返回数量，默认3", example = "3")
            @RequestParam(required = false, defaultValue = "3") Integer limit) {
        log.info("获取最热帖子列表, limit={}", limit);
        HotPostListVO result = practiceService.getHotPosts(limit);
        return Result.success(result);
    }

    /**
     * 获取标签列表及统计数量
     *
     * @param department 按部门过滤标签统计（可选）
     * @return 标签列表
     */
    @GetMapping("/tags")
    @ApiOperation(value = "获取标签列表及统计", notes = "获取所有可用标签及其帖子统计数量")
    public Result<TagListVO> getTags(
            @ApiParam(value = "按部门过滤标签统计", example = "研发部")
            @RequestParam(required = false) String department) {
        log.info("获取标签列表, department={}", department);
        TagListVO result = practiceService.getTags(department);
        return Result.success(result);
    }

    /**
     * 获取部门排名列表
     * 包含发帖数和贡献者统计
     *
     * @param tag 按标签过滤部门统计（可选）
     * @return 部门列表
     */
    @GetMapping("/departments")
    @ApiOperation(value = "获取部门排名列表", notes = "获取部门排名列表，包含发帖数和贡献者统计")
    public Result<DepartmentListVO> getDepartments(
            @ApiParam(value = "按标签过滤部门统计", example = "机器学习")
            @RequestParam(required = false) String tag) {
        log.info("获取部门排名列表, tag={}", tag);
        DepartmentListVO result = practiceService.getDepartments(tag);
        return Result.success(result);
    }

    /**
     * 获取热门贡献者列表
     * 按发帖数/贡献度排序
     *
     * @param limit 返回数量，默认5
     * @return 贡献者列表
     */
    @GetMapping("/contributors")
    @ApiOperation(value = "获取热门贡献者列表", notes = "获取热门贡献者列表（按发帖数/贡献度排序）")
    public Result<ContributorListVO> getContributors(
            @ApiParam(value = "返回数量，默认5", example = "5")
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        log.info("获取热门贡献者列表, limit={}", limit);
        ContributorListVO result = practiceService.getContributors(limit);
        return Result.success(result);
    }
}
