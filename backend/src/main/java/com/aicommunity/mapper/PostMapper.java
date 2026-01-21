package com.aicommunity.mapper;

import com.aicommunity.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface PostMapper {

    /**
     * 查询赋能交流列表（zone_id=4）
     *
     * @param limit 限制数量
     * @return 帖子列表
     */
    List<Post> selectEmpowermentPosts(@Param("limit") Integer limit);

    /**
     * 查询AI优秀实践列表（zone_id=1）
     * 根据label_id分类：培训赋能、AI训战、用户交流
     *
     * @param limit 每个分类限制数量
     * @return 帖子列表
     */
    List<Post> selectPracticePosts(@Param("limit") Integer limit);

    /**
     * 根据ID查询帖子详情
     *
     * @param postId 帖子ID
     * @return 帖子信息
     */
    Post selectByPostId(@Param("postId") String postId);

    /**
     * 插入帖子
     *
     * @param post 帖子信息
     * @return 影响行数
     */
    int insertPost(Post post);

    /**
     * 更新帖子
     *
     * @param post 帖子信息
     * @return 影响行数
     */
    int updatePost(Post post);

    /**
     * 删除帖子（逻辑删除）
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int deletePost(@Param("postId") String postId);

    /**
     * 增加浏览量
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int incrementViews(@Param("postId") String postId);

    /**
     * 查询帖子点赞数
     *
     * @param postId 帖子ID
     * @return 点赞数
     */
    Integer countLikes(@Param("postId") String postId);

    /**
     * 查询帖子收藏数
     *
     * @param postId 帖子ID
     * @return 收藏数
     */
    Integer countCollects(@Param("postId") String postId);

    /**
     * 查询帖子评论数
     *
     * @param postId 帖子ID
     * @return 评论数
     */
    Integer countComments(@Param("postId") String postId);

    /**
     * 查询扶摇Agent应用的精华帖子
     * 查询zone_id=5且tool_id=-1且essence_post='1'的帖子
     *
     * @param zoneId 专区ID（扶摇Agent应用为5）
     * @param toolId 工具ID（扶摇Agent应用为-1）
     * @return 精华帖子，如果没有则返回null
     */
    Post selectAgentFeaturedPost(@Param("zoneId") Integer zoneId, @Param("toolId") Integer toolId);

    /**
     * 设置帖子为精华帖
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int setAgentFeaturedPost(@Param("postId") String postId);

    /**
     * 取消扶摇Agent应用的精华帖
     * 将zone_id=5且tool_id=-1的所有帖子的essence_post设置为'0'
     *
     * @param zoneId 专区ID（扶摇Agent应用为5）
     * @param toolId 工具ID（扶摇Agent应用为-1）
     * @return 影响行数
     */
    int cancelAgentFeaturedPost(@Param("zoneId") Integer zoneId, @Param("toolId") Integer toolId);

    /**
     * 查询精华/置顶帖子列表（按区域）
     *
     * @param zoneId 专区ID（1-AI优秀实践，3-AI工具专区，4-赋能交流，5-扶摇Agent应用）
     * @param toolId 工具ID（可选，0表示其他工具，-1表示扶摇Agent应用）
     * @return 精华帖子列表
     */
    List<Post> selectFeaturedPostsByZone(@Param("zoneId") Integer zoneId, @Param("toolId") Integer toolId);

    /**
     * 更新帖子精华状态
     *
     * @param postId 帖子ID
     * @param essencePost 精华状态（'0'或'1'）
     * @return 影响行数
     */
    int updatePostEssence(@Param("postId") String postId, @Param("essencePost") String essencePost);

    /**
     * 查询工具专区精华帖子（其他工具专有）
     * 查询zone_id=3且tool_id=0且essence_post='1'的帖子
     *
     * @param zoneId 专区ID（AI工具专区为3）
     * @param toolId 工具ID（其他工具为0）
     * @return 精华帖子，如果没有则返回null
     */
    Post selectToolFeaturedPost(@Param("zoneId") Integer zoneId, @Param("toolId") Integer toolId);

    /**
     * 取消工具专区精华帖
     * 将zone_id=3且tool_id=0的所有帖子的essence_post设置为'0'
     *
     * @param zoneId 专区ID（AI工具专区为3）
     * @param toolId 工具ID（其他工具为0）
     * @return 影响行数
     */
    int cancelToolFeaturedPost(@Param("zoneId") Integer zoneId, @Param("toolId") Integer toolId);

    /**
     * 查询用户发布的帖子列表（分页）
     *
     * @param userId   用户ID
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 帖子列表
     */
    List<Post> selectUserPosts(@Param("userId") String userId,
                               @Param("offset") Integer offset,
                               @Param("pageSize") Integer pageSize);

    /**
     * 统计用户发布的帖子总数
     *
     * @param userId 用户ID
     * @return 帖子总数
     */
    Long countUserPosts(@Param("userId") String userId);
}
