package com.aicommunity.mapper;

import com.aicommunity.entity.PostTag;
import com.aicommunity.vo.DepartmentVO;
import com.aicommunity.vo.TagVO;
import com.aicommunity.vo.ToolPostItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工具帖子Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface ToolPostMapper {

    /**
     * 查询工具帖子列表（支持分页、搜索、筛选、排序）
     * zone_id固定为3（AI工具专区）
     *
     * @param toolId      工具ID，0表示"其他工具"
     * @param category    帖子分类：guide-操作指导，excellent-优秀使用
     * @param tag         标签筛选
     * @param department  部门筛选
     * @param keyword     搜索关键词
     * @param sortBy      排序方式：newest-最新，hot-最热，comments-评论最多
     * @param offset      偏移量
     * @param pageSize    每页数量
     * @return 帖子列表
     */
    List<ToolPostItemVO> selectToolPosts(@Param("toolId") Integer toolId,
                                         @Param("category") String category,
                                         @Param("tag") String tag,
                                         @Param("department") String department,
                                         @Param("keyword") String keyword,
                                         @Param("sortBy") String sortBy,
                                         @Param("offset") Integer offset,
                                         @Param("pageSize") Integer pageSize);

    /**
     * 统计工具帖子总数（支持搜索、筛选）
     * zone_id固定为3（AI工具专区）
     *
     * @param toolId     工具ID，0表示"其他工具"
     * @param category   帖子分类
     * @param tag        标签筛选
     * @param department 部门筛选
     * @param keyword    搜索关键词
     * @return 总数
     */
    Long countToolPosts(@Param("toolId") Integer toolId,
                       @Param("category") String category,
                       @Param("tag") String tag,
                       @Param("department") String department,
                       @Param("keyword") String keyword);

    /**
     * 查询标签列表及统计数量（工具专区）
     * zone_id固定为3（AI工具专区）
     *
     * @param toolId     工具ID，0表示"其他工具"
     * @param department 按部门过滤（可选）
     * @return 标签列表
     */
    List<TagVO> selectTagsWithCount(@Param("toolId") Integer toolId,
                                    @Param("department") String department);

    /**
     * 查询部门排名列表（工具专区）
     * zone_id固定为3（AI工具专区）
     *
     * @param toolId 工具ID，0表示"其他工具"
     * @param tag    按标签过滤（可选）
     * @return 部门列表
     */
    List<DepartmentVO> selectDepartmentRankings(@Param("toolId") Integer toolId,
                                               @Param("tag") String tag);

    /**
     * 根据帖子ID查询标签列表
     *
     * @param postId 帖子ID
     * @return 标签列表
     */
    List<PostTag> selectTagsByPostId(@Param("postId") String postId);
}
