package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.PostCreateRequest;
import com.aicommunity.dto.PostUpdateRequest;
import com.aicommunity.entity.Comment;
import com.aicommunity.entity.Post;

import java.util.List;

/**
 * 帖子服务接口
 *
 * @author AI Community Team
 */
public interface PostService {
    /**
     * 获取帖子列表
     *
     * @param zone 专区
     * @param toolId 工具ID
     * @param tag 标签
     * @param department 部门
     * @param author 作者
     * @param sort 排序方式
     * @param search 搜索关键词
     * @param pageQuery 分页参数
     * @return 帖子列表
     */
    PageResult<Post> getPosts(String zone, Long toolId, String tag, String department, 
                              String author, String sort, String search, PageQuery pageQuery);

    /**
     * 获取帖子详情
     *
     * @param id 帖子ID
     * @return 帖子详情
     */
    Post getPostDetail(Long id);

    /**
     * 创建帖子
     *
     * @param request 创建请求
     * @return 创建的帖子
     */
    Post createPost(PostCreateRequest request);

    /**
     * 更新帖子
     *
     * @param id 帖子ID
     * @param request 更新请求
     * @return 更新后的帖子
     */
    Post updatePost(Long id, PostUpdateRequest request);

    /**
     * 删除帖子
     *
     * @param id 帖子ID
     */
    void deletePost(Long id);

    /**
     * 点赞/取消点赞帖子
     *
     * @param id 帖子ID
     * @param action 操作：like-点赞，unlike-取消点赞
     * @return 点赞响应
     */
    PostController.LikeResponse likePost(Long id, String action);

    /**
     * 收藏/取消收藏帖子
     *
     * @param id 帖子ID
     * @param action 操作：collect-收藏，uncollect-取消收藏
     * @return 收藏响应
     */
    PostController.CollectResponse collectPost(Long id, String action);

    /**
     * 获取推荐封面列表
     *
     * @return 推荐封面列表
     */
    List<PostController.CoverInfo> getRecommendedCovers();

    /**
     * 保存草稿
     *
     * @param request 草稿请求
     * @return 草稿响应
     */
    PostController.DraftResponse saveDraft(PostController.DraftRequest request);

    /**
     * 获取最热帖子
     *
     * @param zone 专区
     * @param limit 返回数量
     * @return 最热帖子列表
     */
    List<Post> getHotPosts(String zone, Integer limit);

    /**
     * 获取帖子评论列表
     *
     * @param postId 帖子ID
     * @param pageQuery 分页参数
     * @return 评论列表
     */
    PageResult<Comment> getPostComments(Long postId, PageQuery pageQuery);

    /**
     * 发表评论
     *
     * @param postId 帖子ID
     * @param request 评论请求
     * @return 创建的评论
     */
    Comment createComment(Long postId, PostController.CommentCreateRequest request);
}
