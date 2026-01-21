package com.aicommunity.service;

import com.aicommunity.vo.*;

/**
 * 赋能交流服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface EmpowermentService {

    /**
     * 获取精华帖子列表
     *
     * @return 精华帖子列表响应
     */
    FeaturedPostListVO getFeaturedPosts();

    /**
     * 获取帖子列表（支持搜索、标签筛选、排序和分页）
     *
     * @param keyword  搜索关键词
     * @param tag      标签筛选
     * @param sortBy   排序方式：newest-最新，hot-最热，comments-评论最多，likes-点赞最多
     * @param page     页码，默认1
     * @param pageSize 每页条数，默认15
     * @return 帖子列表响应（带分页）
     */
    EmpowermentPostListVO getPosts(String keyword, String tag, String sortBy, Integer page, Integer pageSize);

    /**
     * 获取标签统计
     *
     * @return 标签统计列表响应
     */
    TagStatListVO getTags();

    /**
     * 获取精选合集
     *
     * @param limit 返回数量，默认5
     * @return 精选合集列表响应
     */
    CollectionListVO getCollections(Integer limit);

    /**
     * 设置/取消精华帖子
     *
     * @param postId   帖子ID
     * @param featured true-设为精华，false-取消精华
     * @return 设置响应
     */
    FeaturedPostSetResponseVO setFeaturedPost(Integer postId, Boolean featured);
}
