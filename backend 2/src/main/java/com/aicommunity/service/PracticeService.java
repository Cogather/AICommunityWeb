package com.aicommunity.service;

import com.aicommunity.vo.ContributorListVO;
import com.aicommunity.vo.DepartmentListVO;
import com.aicommunity.vo.HotPostListVO;
import com.aicommunity.vo.PostListVO;
import com.aicommunity.vo.TagListVO;

/**
 * AI优秀实践服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface PracticeService {

    /**
     * 获取AI优秀实践帖子列表（支持分页、搜索、筛选、排序）
     *
     * @param page        当前页码，默认1
     * @param pageSize    每页数量，默认15
     * @param keyword     搜索关键词
     * @param tag         按标签筛选
     * @param department  按部门筛选
     * @param contributor 按贡献者筛选
     * @param sortBy      排序方式：newest/hot/comments/likes，默认newest
     * @return 帖子列表
     */
    PostListVO getPostList(Integer page, Integer pageSize, String keyword,
                           String tag, String department, String contributor, String sortBy);

    /**
     * 获取最热帖子Top N
     *
     * @param limit 返回数量，默认3
     * @return 最热帖子列表
     */
    HotPostListVO getHotPosts(Integer limit);

    /**
     * 获取标签列表及统计数量
     *
     * @param department 按部门过滤标签统计（可选）
     * @return 标签列表
     */
    TagListVO getTags(String department);

    /**
     * 获取部门排名列表
     *
     * @param tag 按标签过滤部门统计（可选）
     * @return 部门列表
     */
    DepartmentListVO getDepartments(String tag);

    /**
     * 获取热门贡献者列表
     *
     * @param limit 返回数量，默认5
     * @return 贡献者列表
     */
    ContributorListVO getContributors(Integer limit);
}
