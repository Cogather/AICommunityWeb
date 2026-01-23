package com.aicommunity.service;

import com.aicommunity.vo.*;

/**
 * 帖子服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface PostService {

    /**
     * 获取帖子详情
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID（用于判断是否点赞、收藏等）
     * @return 帖子详情
     */
    PostDetailVO getPostDetail(String postId, String userId);

    /**
     * 创建帖子
     *
     * @param request 创建帖子请求
     * @param userId  用户ID
     * @return 帖子详情
     */
    PostDetailVO createPost(PostCreateRequestVO request, String userId);

    /**
     * 更新帖子
     *
     * @param postId  帖子ID
     * @param request 更新帖子请求
     * @param userId  用户ID
     * @return 帖子详情
     */
    PostDetailVO updatePost(String postId, PostUpdateRequestVO request, String userId);

    /**
     * 删除帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    void deletePost(String postId, String userId);

    /**
     * 点赞/取消点赞帖子
     *
     * @param postId 帖子ID
     * @param action 操作类型：like-点赞，unlike-取消点赞
     * @param userId 用户ID
     * @return 点赞响应
     */
    LikeResponseVO likePost(String postId, String action, String userId);

    /**
     * 收藏/取消收藏帖子
     *
     * @param postId 帖子ID
     * @param action 操作类型：collect-收藏，uncollect-取消收藏
     * @param userId 用户ID
     * @return 收藏响应
     */
    CollectResponseVO collectPost(String postId, String action, String userId);

    /**
     * 设置帖子精华/置顶状态
     *
     * @param postId  帖子ID
     * @param request 设置请求
     * @param userId  用户ID
     * @return 设置响应
     */
    PostFeaturedSetResponseVO setFeaturedPost(String postId, PostFeaturedSetRequestVO request, String userId);
}
