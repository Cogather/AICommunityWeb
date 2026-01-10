package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.PostCreateRequest;
import com.aicommunity.dto.PostUpdateRequest;
import com.aicommunity.entity.Comment;
import com.aicommunity.entity.Post;
import com.aicommunity.mapper.*;
import com.aicommunity.service.PostService;
import com.aicommunity.service.PointsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子服务实现类
 *
 * @author AI Community Team
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PostTagMapper postTagMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private RecommendedCoverMapper recommendedCoverMapper;

    @Autowired
    private DraftMapper draftMapper;

    @Autowired
    private PointsService pointsService;

    @Autowired
    private com.aicommunity.mapper.UserRoleMapper userRoleMapper;

    @Override
    public PageResult<Post> getPosts(String zone, Long toolId, String tag, String department,
                                     String author, String sort, String search, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Post> posts = postMapper.selectPosts(zone, toolId, tag, department, author, sort, search);
        
        // 填充作者信息、标签等信息
        for (Post post : posts) {
            fillPostInfo(post);
        }
        
        PageInfo<Post> pageInfo = new PageInfo<>(posts);
        return PageResult.of(posts, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public Post getPostDetail(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.POST_NOT_FOUND);
        }

        fillPostInfo(post);
        
        // 增加浏览量
        postMapper.incrementViews(id);
        post.setViews(post.getViews() + 1);

        // 检查当前用户是否已点赞、收藏
        Long currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            post.setIsLiked(likeMapper.existsByUserAndTarget(currentUserId, "post", id));
            post.setIsCollected(favoriteMapper.existsByUserAndPost(currentUserId, id));
            post.setIsAuthor(post.getAuthorId().equals(currentUserId));
        }

        return post;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Post createPost(PostCreateRequest request) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setSummary(request.getSummary());
        post.setContent(request.getContent());
        post.setCover(request.getCover());
        post.setAuthorId(currentUserId);
        post.setZone(request.getZone());
        post.setToolId(request.getToolId());
        post.setIsFeatured(false);
        post.setViews(0);
        post.setLikes(0);
        post.setComments(0);
        post.setCreateTime(new Date());
        post.setUpdateTime(new Date());

        postMapper.insert(post);

        // 保存标签关联
        if (request.getTags() != null && request.getTags().length > 0) {
            savePostTags(post.getId(), request.getTags());
        }

        // 计算积分（发布帖子+15，管理员除外）
        if (!isAdmin(currentUserId)) {
            pointsService.addPoints(currentUserId, 15, "post", post.getId(), "post");
        }

        return getPostDetail(post.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Post updatePost(Long id, PostUpdateRequest request) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.POST_NOT_FOUND);
        }

        Long currentUserId = getCurrentUserId();
        if (!post.getAuthorId().equals(currentUserId) && !isAdmin(currentUserId)) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN);
        }

        post.setTitle(request.getTitle());
        post.setSummary(request.getSummary());
        post.setContent(request.getContent());
        post.setCover(request.getCover());
        post.setZone(request.getZone());
        post.setToolId(request.getToolId());
        post.setUpdateTime(new Date());

        postMapper.updateById(post);

        // 更新标签关联
        postTagMapper.deleteByPostId(id);
        if (request.getTags() != null && request.getTags().length > 0) {
            savePostTags(id, request.getTags());
        }

        return getPostDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.POST_NOT_FOUND);
        }

        Long currentUserId = getCurrentUserId();
        if (!post.getAuthorId().equals(currentUserId) && !isAdmin(currentUserId)) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN);
        }

        // 删除关联数据
        postTagMapper.deleteByPostId(id);
        likeMapper.deleteByTarget("post", id);
        favoriteMapper.deleteByPostId(id);
        commentMapper.deleteByPostId(id);

        postMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostController.LikeResponse likePost(Long id, String action) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.POST_NOT_FOUND);
        }

        boolean exists = likeMapper.existsByUserAndTarget(currentUserId, "post", id);
        PostController.LikeResponse response = new PostController.LikeResponse();

        if ("like".equals(action)) {
            if (!exists) {
                likeMapper.insert(currentUserId, "post", id);
                postMapper.incrementLikes(id);
                post.setLikes(post.getLikes() + 1);

                // 计算积分（帖子被点赞+3，管理员除外）
                if (!isAdmin(post.getAuthorId())) {
                    pointsService.addPoints(post.getAuthorId(), 3, "like_received", id, "post");
                }
            }
            response.setLiked(true);
        } else if ("unlike".equals(action)) {
            if (exists) {
                likeMapper.deleteByUserAndTarget(currentUserId, "post", id);
                postMapper.decrementLikes(id);
                post.setLikes(Math.max(0, post.getLikes() - 1));
            }
            response.setLiked(false);
        }

        response.setLikes(post.getLikes());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostController.CollectResponse collectPost(Long id, String action) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.POST_NOT_FOUND);
        }

        boolean exists = favoriteMapper.existsByUserAndPost(currentUserId, id);
        PostController.CollectResponse response = new PostController.CollectResponse();

        if ("collect".equals(action)) {
            if (!exists) {
                favoriteMapper.insert(currentUserId, id);
                
                // 计算积分（帖子被收藏+5，管理员除外）
                if (!isAdmin(post.getAuthorId())) {
                    pointsService.addPoints(post.getAuthorId(), 5, "favorite_received", id, "post");
                }
            }
            response.setCollected(true);
        } else if ("uncollect".equals(action)) {
            if (exists) {
                favoriteMapper.deleteByUserAndPost(currentUserId, id);
            }
            response.setCollected(false);
        }

        return response;
    }

    @Override
    public List<PostController.CoverInfo> getRecommendedCovers() {
        return recommendedCoverMapper.selectAll().stream()
                .map(cover -> {
                    PostController.CoverInfo info = new PostController.CoverInfo();
                    info.setId(cover.getId());
                    info.setUrl(cover.getUrl());
                    info.setThumbnail(cover.getThumbnail());
                    return info;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostController.DraftResponse saveDraft(PostController.DraftRequest request) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        // TODO: 实现草稿保存逻辑
        PostController.DraftResponse response = new PostController.DraftResponse();
        response.setDraftId("draft_" + System.currentTimeMillis());
        response.setSavedAt(new Date().toString());
        response.setMessage("草稿保存成功");
        return response;
    }

    @Override
    public List<Post> getHotPosts(String zone, Integer limit) {
        List<Post> posts = postMapper.selectHotPosts(zone, limit);
        for (Post post : posts) {
            fillPostInfo(post);
        }
        return posts;
    }

    @Override
    public PageResult<Comment> getPostComments(Long postId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Comment> comments = commentMapper.selectByPostId(postId);
        
        // 填充回复列表和用户信息
        for (Comment comment : comments) {
            fillCommentInfo(comment);
        }
        
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        return PageResult.of(comments, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment createComment(Long postId, PostController.CommentCreateRequest request) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.POST_NOT_FOUND);
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(currentUserId);
        comment.setContent(request.getContent());
        comment.setLikes(0);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());

        commentMapper.insert(comment);

        // 更新帖子评论数
        postMapper.incrementComments(postId);

        // 计算积分（发表评论+1，管理员除外）
        if (!isAdmin(currentUserId)) {
            pointsService.addPoints(currentUserId, 1, "comment", comment.getId(), "comment");
        }

        // 发送消息通知（TODO: 实现消息通知）

        fillCommentInfo(comment);
        return comment;
    }

    /**
     * 填充帖子信息
     */
    private void fillPostInfo(Post post) {
        // 填充作者信息
        if (post.getAuthorId() != null) {
            com.aicommunity.entity.User user = userMapper.selectById(post.getAuthorId());
            if (user != null) {
                post.setAuthorName(user.getName());
                post.setAuthorAvatar(user.getAvatar());
            }
        }

        // 填充标签列表
        List<String> tags = tagMapper.selectNamesByPostId(post.getId());
        post.setTags(tags);

        // 填充工具名称
        if (post.getToolId() != null) {
            com.aicommunity.entity.Tool tool = toolMapper.selectById(post.getToolId());
            if (tool != null) {
                post.setToolName(tool.getName());
            }
        }
    }

    /**
     * 填充评论信息
     */
    private void fillCommentInfo(Comment comment) {
        // 填充用户信息
        if (comment.getUserId() != null) {
            com.aicommunity.entity.User user = userMapper.selectById(comment.getUserId());
            if (user != null) {
                comment.setUserName(user.getName());
                comment.setUserAvatar(user.getAvatar());
            }
        }

        // 填充回复列表
        List<com.aicommunity.entity.Reply> replies = replyMapper.selectByCommentId(comment.getId());
        for (com.aicommunity.entity.Reply reply : replies) {
            fillReplyInfo(reply);
        }
        comment.setReplies(replies);

        // 检查是否已点赞
        Long currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            comment.setIsLiked(likeMapper.existsByUserAndTarget(currentUserId, "comment", comment.getId()));
        }
    }

    /**
     * 填充回复信息
     */
    private void fillReplyInfo(com.aicommunity.entity.Reply reply) {
        if (reply.getUserId() != null) {
            com.aicommunity.entity.User user = userMapper.selectById(reply.getUserId());
            if (user != null) {
                reply.setUserName(user.getName());
                reply.setUserAvatar(user.getAvatar());
            }
        }
        if (reply.getReplyToUserId() != null) {
            com.aicommunity.entity.User user = userMapper.selectById(reply.getReplyToUserId());
            if (user != null) {
                reply.setReplyTo(user.getName());
            }
        }
    }

    /**
     * 保存帖子标签关联
     */
    private void savePostTags(Long postId, String[] tagNames) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return;
        }
        
        for (String tagName : tagNames) {
            com.aicommunity.entity.Tag tag = tagMapper.selectByNameAndZone(tagName, post.getZone());
            if (tag == null) {
                // 创建新标签
                tag = new com.aicommunity.entity.Tag();
                tag.setName(tagName);
                tag.setZone(post.getZone());
                tag.setPreset(false);
                tagMapper.insert(tag);
            }
            postTagMapper.insert(postId, tag.getId());
        }
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        return com.aicommunity.util.UserContext.getUserId();
    }

    /**
     * 判断是否是管理员
     */
    private boolean isAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        List<String> roles = userRoleMapper.selectRolesByUserId(userId);
        return roles != null && roles.contains("admin");
    }
}
