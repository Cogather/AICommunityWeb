package com.aicommunity.mapper;

import com.aicommunity.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 帖子Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface PostMapper {

    /**
     * 根据条件查询帖子列表
     *
     * @param zone       专区
     * @param toolId     工具ID
     * @param tag        标签
     * @param department 部门
     * @param author     作者
     * @param search     搜索关键词
     * @param sort       排序方式
     * @return 帖子列表
     */
    List<Map<String, Object>> selectByCondition(
            @Param("zone") String zone,
            @Param("toolId") Long toolId,
            @Param("tag") String tag,
            @Param("department") String department,
            @Param("author") String author,
            @Param("search") String search,
            @Param("sort") String sort,
            @Param("isFeatured") Boolean isFeatured);

    /**
     * 根据ID查询帖子
     *
     * @param id 帖子ID
     * @return 帖子
     */
    Post selectById(@Param("id") Long id);

    /**
     * 插入帖子
     *
     * @param post 帖子
     * @return 影响行数
     */
    int insert(Post post);

    /**
     * 更新帖子
     *
     * @param post 帖子
     * @return 影响行数
     */
    int update(Post post);

    /**
     * 删除帖子
     *
     * @param id 帖子ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新点赞数
     *
     * @param id    帖子ID
     * @param delta 变化量
     * @return 影响行数
     */
    int updateLikes(@Param("id") Long id, @Param("delta") Integer delta);

    /**
     * 更新评论数
     *
     * @param id    帖子ID
     * @param delta 变化量
     * @return 影响行数
     */
    int updateComments(@Param("id") Long id, @Param("delta") Integer delta);

    /**
     * 更新浏览量
     *
     * @param id 帖子ID
     * @return 影响行数
     */
    int incrementViews(@Param("id") Long id);

    /**
     * 根据作者ID查询帖子
     *
     * @param authorId 作者ID
     * @return 帖子列表
     */
    List<Map<String, Object>> selectByAuthorId(@Param("authorId") Long authorId);

    /**
     * 统计作者发布的帖子数
     *
     * @param authorId 作者ID
     * @return 帖子数
     */
    int countByAuthorId(@Param("authorId") Long authorId);

    /**
     * 查询热门贡献者
     *
     * @param zone  专区
     * @param limit 返回数量
     * @return 贡献者列表
     */
    List<Map<String, Object>> selectTopContributors(@Param("zone") String zone, @Param("limit") Integer limit);

    /**
     * 查询最热帖子
     *
     * @param zone  专区
     * @param limit 返回数量
     * @return 帖子列表
     */
    List<Map<String, Object>> selectHotPosts(@Param("zone") String zone, @Param("limit") Integer limit);

    /**
     * 查询部门统计
     *
     * @param zone 专区
     * @return 部门统计列表
     */
    List<Map<String, Object>> selectDepartmentStats(@Param("zone") String zone);
}
