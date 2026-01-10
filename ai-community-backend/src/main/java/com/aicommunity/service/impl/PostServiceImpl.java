package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.PostController;
import com.aicommunity.dto.PostDetailDTO;
import com.aicommunity.dto.PostListDTO;
import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.PostService;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private PostTagMapper postTagMapper;

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private RecommendedCoverMapper recommendedCoverMapper;

    @Autowired
    private PostDraftMapper postDraftMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Override
    public PageResult<PostListDTO> getPosts(String zone, Long toolId, String tag, String department,
                                            String author, String sort, String search, Boolean isFeatured,
                                            PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Map<String, Object>> list = postMapper.selectByCondition(zone, toolId, tag, department, author, search, sort, isFeatured);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

        // 转换为DTO
        List<PostListDTO> dtoList = list.stream().map(this::convertToPostListDTO).collect(Collectors.toList());

        return PageResult.of(dtoList, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public PostDetailDTO getPostDetail(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        // 增加浏览量
        postMapper.incrementViews(id);

        // 查询作者信息
        User author = userMapper.selectById(post.getAuthorId());
        if (author == null) {
            throw new BusinessException("作者不存在");
        }

        // 查询标签
        List<String> tags = postTagMapper.selectTagsByPostId(id);

        // 查询工具信息
        String toolName = null;
        if (post.getToolId() != null) {
            Tool tool = toolMapper.selectById(post.getToolId());
            if (tool != null) {
                toolName = tool.getName();
            }
        }

        // 构建DTO
        PostDetailDTO dto = new PostDetailDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSummary(post.getSummary());
        dto.setContent(post.getContent());
        dto.setAuthorName(author.getName());
        dto.setAuthorAvatar(author.getAvatar());
        dto.setAuthorId(author.getId());
        dto.setCreateTime(post.getCreateTime().toString());
        dto.setUpdateTime(post.getUpdateTime() != null ? post.getUpdateTime().toString() : null);
        dto.setViews(post.getViews());
        dto.setLikes(post.getLikes());
        dto.setComments(post.getComments());
        dto.setTags(tags);
        dto.setCover(post.getCover());
        dto.setZone(post.getZone());
        dto.setToolId(post.getToolId());
        dto.setToolName(toolName);

        // 判断当前用户状态
        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId != null) {
            // 判断是否点赞
            LikeRecord likeRecord = likeRecordMapper.selectByUserAndTarget(currentUserId, "post", id);
            dto.setIsLiked(likeRecord != null);

            // 判断是否收藏
            Favorite favorite = favoriteMapper.selectByUserAndPost(currentUserId, id);
            dto.setIsCollected(favorite != null);

            // 判断是否是作者
            dto.setIsAuthor(currentUserId.equals(post.getAuthorId()));

            // 判断权限
            boolean isAdmin = isAdmin(currentUserId);
            dto.setCanEdit(dto.getIsAuthor() || isAdmin);
            dto.setCanDelete(dto.getIsAuthor() || isAdmin);
        } else {
            dto.setIsLiked(false);
            dto.setIsCollected(false);
            dto.setIsAuthor(false);
            dto.setCanEdit(false);
            dto.setCanDelete(false);
        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostController.CreatePostResponse createPost(PostController.CreatePostRequest request) {
        Long userId = getCurrentUserId();

        // 创建帖子
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setSummary(request.getSummary());
        post.setContent(request.getContent());
        post.setAuthorId(userId);
        post.setZone(request.getZone());
        post.setToolId(request.getToolId());
        post.setCover(request.getCover());
        post.setIsFeatured(false);
        post.setViews(0);
        post.setLikes(0);
        post.setComments(0);
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());

        postMapper.insert(post);

        // 批量插入标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            List<PostTag> postTags = request.getTags().stream()
                    .map(tagName -> {
                        PostTag postTag = new PostTag();
                        postTag.setPostId(post.getId());
                        postTag.setTagName(tagName);
                        postTag.setCreateTime(LocalDateTime.now());
                        return postTag;
                    })
                    .collect(Collectors.toList());
            postTagMapper.insertBatch(postTags);
        }

        // 计算积分（发布帖子+15，管理员除外）
        if (!isAdmin(userId)) {
            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            PointsRecord record = new PointsRecord();
            record.setUserId(userId);
            record.setPoints(15);
            record.setType("post");
            record.setTargetId(post.getId());
            record.setMonth(currentMonth);
            record.setCreateTime(LocalDateTime.now());
            pointsRecordMapper.insert(record);
        }

        return new PostController.CreatePostResponse(post.getId(), "帖子发布成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostController.UpdatePostResponse updatePost(Long id, PostController.CreatePostRequest request) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        Long userId = getCurrentUserId();
        // 验证权限（作者或管理员）
        if (!post.getAuthorId().equals(userId) && !isAdmin(userId)) {
            throw new BusinessException(403, "无权限编辑此帖子");
        }

        // 更新帖子
        post.setTitle(request.getTitle());
        post.setSummary(request.getSummary());
        post.setContent(request.getContent());
        post.setCover(request.getCover());
        post.setZone(request.getZone());
        post.setToolId(request.getToolId());
        post.setUpdateTime(LocalDateTime.now());
        postMapper.update(post);

        // 更新标签
        postTagMapper.deleteByPostId(id);
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            List<PostTag> postTags = request.getTags().stream()
                    .map(tagName -> {
                        PostTag postTag = new PostTag();
                        postTag.setPostId(id);
                        postTag.setTagName(tagName);
                        postTag.setCreateTime(LocalDateTime.now());
                        return postTag;
                    })
                    .collect(Collectors.toList());
            postTagMapper.insertBatch(postTags);
        }

        return new PostController.UpdatePostResponse(id, "帖子更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        Long userId = getCurrentUserId();
        // 验证权限（作者或管理员）
        if (!post.getAuthorId().equals(userId) && !isAdmin(userId)) {
            throw new BusinessException(403, "无权限删除此帖子");
        }

        // 删除帖子（级联删除评论、点赞、收藏记录由数据库外键约束处理，或手动删除）
        postTagMapper.deleteByPostId(id);
        postMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostController.LikePostResponse likePost(Long id, PostController.LikePostRequest request) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        Long userId = getCurrentUserId();
        LikeRecord likeRecord = likeRecordMapper.selectByUserAndTarget(userId, "post", id);

        boolean isLike = "like".equals(request.getAction());
        boolean currentlyLiked = likeRecord != null;

        if (isLike && !currentlyLiked) {
            // 点赞
            LikeRecord newRecord = new LikeRecord();
            newRecord.setUserId(userId);
            newRecord.setTargetType("post");
            newRecord.setTargetId(id);
            newRecord.setCreateTime(LocalDateTime.now());
            likeRecordMapper.insert(newRecord);

            // 更新帖子点赞数
            postMapper.updateLikes(id, 1);

            // 给作者加积分（+3，管理员除外）
            if (!isAdmin(post.getAuthorId())) {
                String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                PointsRecord record = new PointsRecord();
                record.setUserId(post.getAuthorId());
                record.setPoints(3);
                record.setType("like_received");
                record.setTargetId(id);
                record.setMonth(currentMonth);
                record.setCreateTime(LocalDateTime.now());
                pointsRecordMapper.insert(record);
            }
        } else if (!isLike && currentlyLiked) {
            // 取消点赞
            likeRecordMapper.delete(userId, "post", id);
            postMapper.updateLikes(id, -1);
        }

        Post updatedPost = postMapper.selectById(id);
        return new PostController.LikePostResponse(isLike && !currentlyLiked || currentlyLiked && isLike, updatedPost.getLikes());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostController.CollectPostResponse collectPost(Long id, PostController.CollectPostRequest request) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        Long userId = getCurrentUserId();
        Favorite favorite = favoriteMapper.selectByUserAndPost(userId, id);

        boolean isCollect = "collect".equals(request.getAction());
        boolean currentlyCollected = favorite != null;

        if (isCollect && !currentlyCollected) {
            // 收藏
            Favorite newFavorite = new Favorite();
            newFavorite.setUserId(userId);
            newFavorite.setPostId(id);
            newFavorite.setCreateTime(LocalDateTime.now());
            favoriteMapper.insert(newFavorite);

            // 给作者加积分（+5，管理员除外）
            if (!isAdmin(post.getAuthorId())) {
                String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                PointsRecord record = new PointsRecord();
                record.setUserId(post.getAuthorId());
                record.setPoints(5);
                record.setType("favorite_received");
                record.setTargetId(id);
                record.setMonth(currentMonth);
                record.setCreateTime(LocalDateTime.now());
                pointsRecordMapper.insert(record);
            }
        } else if (!isCollect && currentlyCollected) {
            // 取消收藏
            favoriteMapper.delete(userId, id);
        }

        return new PostController.CollectPostResponse(isCollect && !currentlyCollected || currentlyCollected && isCollect);
    }

    @Override
    public Object getRecommendedCovers() {
        List<RecommendedCover> covers = recommendedCoverMapper.selectAll();
        List<Map<String, Object>> list = covers.stream()
                .map(cover -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", cover.getId());
                    map.put("url", cover.getUrl());
                    map.put("thumbnail", cover.getThumbnail());
                    return map;
                })
                .collect(Collectors.toList());
        return Map.of("list", list);
    }

    @Override
    public PostController.SaveDraftResponse saveDraft(PostController.SaveDraftRequest request) {
        Long userId = getCurrentUserId();

        // 查询用户是否有草稿
        PostDraft draft = postDraftMapper.selectByUserId(userId);
        if (draft == null) {
            // 创建新草稿
            draft = new PostDraft();
            draft.setUserId(userId);
        }

        // 更新草稿内容
        draft.setTitle(request.getTitle());
        draft.setSummary(request.getSummary());
        draft.setContent(request.getContent());
        draft.setCover(request.getCover());
        draft.setZone(request.getZone());
        draft.setToolId(request.getToolId());
        if (request.getTags() != null) {
            draft.setTags(String.join(",", request.getTags()));
        }
        draft.setUpdateTime(LocalDateTime.now());

        if (draft.getId() == null) {
            postDraftMapper.insert(draft);
        } else {
            postDraftMapper.update(draft);
        }

        String savedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new PostController.SaveDraftResponse(draft.getId().toString(), savedAt, "草稿保存成功");
    }

    @Override
    public Object getHotPosts(String zone, Integer limit) {
        List<Map<String, Object>> posts = postMapper.selectHotPosts(zone, limit);
        List<PostListDTO> dtoList = posts.stream().map(this::convertToPostListDTO).collect(Collectors.toList());
        return Map.of("list", dtoList);
    }

    /**
     * 转换为PostListDTO
     */
    private PostListDTO convertToPostListDTO(Map<String, Object> map) {
        PostListDTO dto = new PostListDTO();
        dto.setId((Long) map.get("id"));
        dto.setTitle((String) map.get("title"));
        dto.setDescription((String) map.get("summary"));
        dto.setAuthor((String) map.get("authorName"));
        dto.setAuthorAvatar((String) map.get("authorAvatar"));
        dto.setCreateTime(map.get("createTime") != null ? map.get("createTime").toString() : null);
        dto.setViews((Integer) map.get("views"));
        dto.setComments((Integer) map.get("comments"));
        dto.setLikes((Integer) map.get("likes"));
        dto.setCover((String) map.get("cover"));
        dto.setImage((String) map.get("cover")); // 兼容字段
        dto.setIsFeatured((Boolean) map.get("isFeatured"));
        dto.setZone((String) map.get("zone"));
        dto.setToolId((Long) map.get("toolId"));
        dto.setToolName((String) map.get("toolName"));

        // 查询标签
        if (dto.getId() != null) {
            List<String> tags = postTagMapper.selectTagsByPostId(dto.getId());
            dto.setTags(tags);
        }

        return dto;
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new BusinessException(401, "未授权，请先登录");
    }

    /**
     * 获取当前用户ID（可为null）
     */
    private Long getCurrentUserIdOrNull() {
        try {
            return getCurrentUserId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否是管理员
     */
    private boolean isAdmin(Long userId) {
        List<String> roles = userRoleMapper.selectRolesByUserId(userId);
        return roles != null && roles.contains("admin");
    }
}
