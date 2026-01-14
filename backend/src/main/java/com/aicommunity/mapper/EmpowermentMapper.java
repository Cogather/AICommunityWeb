package com.aicommunity.mapper;

import com.aicommunity.entity.Post;
import com.aicommunity.vo.EmpowermentPostItemVO;
import com.aicommunity.vo.FeaturedPostVO;
import com.aicommunity.vo.TagStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 赋能交流Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface EmpowermentMapper {

    /**
     * 查询赋能交流精华帖子列表
     * 查询zone_id=4且essence_post='1'的帖子
     *
     * @return 精华帖子列表
     */
    List<FeaturedPostVO> selectFeaturedPosts();

    /**
     * 查询赋能交流普通帖子列表（支持搜索、标签筛选、排序、分页）
     * 查询zone_id=4且essence_post='0'的帖子
     *
     * @param keyword  搜索关键词（匹配标题、作者、描述）
     * @param tag      标签筛选
     * @param sortBy   排序方式：newest-最新，hot-最热，comments-评论最多，likes-点赞最多
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 帖子列表
     */
    List<EmpowermentPostItemVO> selectEmpowermentPosts(@Param("keyword") String keyword,
                                                        @Param("tag") String tag,
                                                        @Param("sortBy") String sortBy,
                                                        @Param("offset") Integer offset,
                                                        @Param("pageSize") Integer pageSize);

    /**
     * 统计赋能交流普通帖子总数（支持搜索、标签筛选）
     *
     * @param keyword 搜索关键词
     * @param tag     标签筛选
     * @return 总数
     */
    Long countEmpowermentPosts(@Param("keyword") String keyword,
                               @Param("tag") String tag);

    /**
     * 查询标签统计信息
     * 统计每个标签下的帖子数量（包含精华帖子和普通帖子）
     *
     * @return 标签统计列表
     */
    List<TagStatVO> selectTagStats();

    /**
     * 设置帖子为精华帖
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int setFeaturedPost(@Param("postId") String postId);

    /**
     * 取消帖子精华状态
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int cancelFeaturedPost(@Param("postId") String postId);

    /**
     * 根据帖子ID查询帖子信息
     *
     * @param postId 帖子ID
     * @return 帖子信息
     */
    Post selectByPostId(@Param("postId") String postId);
}
