package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.controller.PostController;
import com.aicommunity.dto.PostDetailDTO;
import com.aicommunity.dto.PostListDTO;

/**
 * 帖子服务接口
 *
 * @author AI Community Team
 */
public interface PostService {

    /**
     * 获取帖子列表
     *
     * @param zone       专区
     * @param toolId     工具ID
     * @param tag        标签
     * @param department 部门
     * @param author     作者
     * @param sort       排序方式
     * @param search     搜索关键词
     * @param isFeatured 是否精选
     * @param pageQuery  分页参数
     * @return 帖子列表
     */
    PageResult<PostListDTO> getPosts(String zone, Long toolId, String tag, String department,
                                      String author, String sort, String search, Boolean isFeatured,
                                      PageQuery pageQuery);

    /**
     * 获取帖子详情
     *
     * @param id 帖子ID
     * @return 帖子详情
     */
    PostDetailDTO getPostDetail(Long id);

    /**
     * 创建帖子
     *
     * @param request 创建请求
     * @return 创建结果
     */
    PostController.CreatePostResponse createPost(PostController.CreatePostRequest request);

    /**
     * 更新帖子
     *
     * @param id      帖子ID
     * @param request 更新请求
     * @return 更新结果
     */
    PostController.UpdatePostResponse updatePost(Long id, PostController.CreatePostRequest request);

    /**
     * 删除帖子
     *
     * @param id 帖子ID
     */
    void deletePost(Long id);

    /**
     * 点赞/取消点赞帖子
     *
     * @param id      帖子ID
     * @param request 点赞请求
     * @return 点赞结果
     */
    PostController.LikePostResponse likePost(Long id, PostController.LikePostRequest request);

    /**
     * 收藏/取消收藏帖子
     *
     * @param id      帖子ID
     * @param request 收藏请求
     * @return 收藏结果
     */
    PostController.CollectPostResponse collectPost(Long id, PostController.CollectPostRequest request);

    /**
     * 获取推荐封面列表
     *
     * @return 推荐封面列表
     */
    Object getRecommendedCovers();

    /**
     * 保存草稿
     *
     * @param request 草稿请求
     * @return 保存结果
     */
    PostController.SaveDraftResponse saveDraft(PostController.SaveDraftRequest request);

    /**
     * 获取最热帖子
     *
     * @param zone  专区
     * @param limit 返回数量
     * @return 帖子列表
     */
    Object getHotPosts(String zone, Integer limit);
}
