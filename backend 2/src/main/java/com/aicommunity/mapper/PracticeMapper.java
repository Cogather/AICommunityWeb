package com.aicommunity.mapper;

import com.aicommunity.entity.PostTag;
import com.aicommunity.entity.UserInfo;
import com.aicommunity.vo.ContributorVO;
import com.aicommunity.vo.DepartmentVO;
import com.aicommunity.vo.HotPostVO;
import com.aicommunity.vo.PostItemVO;
import com.aicommunity.vo.TagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI优秀实践Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface PracticeMapper {

    /**
     * 查询AI优秀实践帖子列表（支持分页、搜索、筛选、排序）
     *
     * @param keyword      搜索关键词
     * @param tag          标签筛选
     * @param department   部门筛选
     * @param contributor  贡献者筛选
     * @param sortBy       排序方式
     * @param offset       偏移量
     * @param pageSize     每页数量
     * @return 帖子列表
     */
    List<PostItemVO> selectPracticePosts(@Param("keyword") String keyword,
                                         @Param("tag") String tag,
                                         @Param("department") String department,
                                         @Param("contributor") String contributor,
                                         @Param("sortBy") String sortBy,
                                         @Param("offset") Integer offset,
                                         @Param("pageSize") Integer pageSize);

    /**
     * 统计AI优秀实践帖子总数（支持搜索、筛选）
     *
     * @param keyword     搜索关键词
     * @param tag         标签筛选
     * @param department  部门筛选
     * @param contributor 贡献者筛选
     * @return 总数
     */
    Long countPracticePosts(@Param("keyword") String keyword,
                           @Param("tag") String tag,
                           @Param("department") String department,
                           @Param("contributor") String contributor);

    /**
     * 查询最热帖子Top N（按浏览量排序）
     *
     * @param limit 返回数量
     * @return 最热帖子列表
     */
    List<HotPostVO> selectHotPosts(@Param("limit") Integer limit);

    /**
     * 查询标签列表及统计数量
     *
     * @param department 按部门过滤（可选）
     * @return 标签列表
     */
    List<TagVO> selectTagsWithCount(@Param("department") String department);

    /**
     * 查询部门排名列表
     *
     * @param tag 按标签过滤（可选）
     * @return 部门列表
     */
    List<DepartmentVO> selectDepartmentRankings(@Param("tag") String tag);

    /**
     * 查询热门贡献者列表
     *
     * @param limit 返回数量
     * @return 贡献者列表
     */
    List<ContributorVO> selectTopContributors(@Param("limit") Integer limit);

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfo selectUserInfoById(@Param("userId") String userId);

    /**
     * 根据帖子ID查询标签列表
     *
     * @param postId 帖子ID
     * @return 标签列表
     */
    List<PostTag> selectTagsByPostId(@Param("postId") String postId);

    /**
     * 根据标签名称查询标签信息
     *
     * @param tagName 标签名称
     * @return 标签信息
     */
    PostTag selectTagByName(@Param("tagName") String tagName);
}
