package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.entity.Post;
import com.aicommunity.entity.PostTag;
import com.aicommunity.entity.UserInfo;
import com.aicommunity.mapper.*;
import com.aicommunity.service.PostService;
import com.aicommunity.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostLikeMapper postLikeMapper;

    @Autowired
    private PostCollectMapper postCollectMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PracticeMapper practiceMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ReplyMapper replyMapper;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public PostDetailVO getPostDetail(String postId, String userId) {
        // 查询帖子信息
        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "帖子不存在");
        }

        // 增加浏览量
        postMapper.incrementViews(postId);

        // 查询作者信息
        UserInfo author = userInfoMapper.selectByUserId(post.getAuthorId());
        if (author == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "作者信息不存在");
        }

        // 查询标签
        List<PostTag> tags = practiceMapper.selectTagsByPostId(postId);
        List<String> tagNames = tags.stream()
                .map(PostTag::getTag)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 查询统计数据
        Integer likes = postMapper.countLikes(postId);
        Integer collects = postMapper.countCollects(postId);
        Integer comments = postMapper.countComments(postId);

        // 查询用户是否点赞、收藏
        Boolean isLiked = false;
        Boolean isCollected = false;
        if (StringUtils.hasText(userId)) {
            Integer likeCount = postLikeMapper.selectLikeCount(postId, userId);
            isLiked = likeCount != null && likeCount > 0;
            Integer collectCount = postCollectMapper.selectCollectCount(postId, userId);
            isCollected = collectCount != null && collectCount > 0;
        }

        // 判断权限
        Boolean isAuthor = StringUtils.hasText(userId) && userId.equals(post.getAuthorId());
        Boolean canEdit = isAuthor; // TODO: 管理员也可以编辑
        Boolean canDelete = isAuthor; // TODO: 管理员也可以删除

        // 构建返回对象
        PostDetailVO vo = new PostDetailVO();
        vo.setId(Integer.parseInt(postId));
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setCover(post.getFrontCover());
        vo.setImage(post.getFrontCover());
        vo.setAuthor(author.getChnName());
        vo.setAuthorName(author.getChnName());
        vo.setAuthorId(Integer.parseInt(author.getUserId()));
        vo.setAuthorAvatar(author.getAuthorAvatar());
        vo.setDepartment(author.getDepartmentL1());
        vo.setZone(getZoneName(post.getZoneId()));
        vo.setToolId(post.getToolId());
        vo.setTags(tagNames);
        vo.setViews(post.getViewsNums() != null ? post.getViewsNums() : 0);
        vo.setComments(comments != null ? comments : 0);
        vo.setLikes(likes != null ? likes : 0);
        vo.setIsLiked(isLiked);
        vo.setIsCollected(isCollected);
        vo.setIsAuthor(isAuthor);
        vo.setCanEdit(canEdit);
        vo.setCanDelete(canDelete);
        vo.setFeatured("1".equals(post.getEssencePost()));
        vo.setCreateTime(formatDate(post.getCreatedAt()));
        vo.setUpdateTime(formatDate(post.getUpdatedAt()));

        // 数据库表中没有summary和description字段，设置为null
        vo.setSummary(null);
        vo.setDescription(null);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostDetailVO createPost(PostCreateRequestVO request, String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "请先登录");
        }

        // 生成帖子ID
        String postId = generatePostId();

        // 构建帖子实体
        Post post = new Post();
        post.setPostId(postId);
        post.setAuthorId(userId);
        post.setTitle(request.getTitle());
        post.setFrontCover(request.getCover());
        post.setContent(request.getContent());
        post.setContentType("richtext");
        post.setStatus("0");
        post.setEssencePost("0");
        post.setZoneId(getZoneId(request.getZone()));
        post.setToolId(request.getToolId());
        post.setViewsNums(0);
        Date now = new Date();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        // 保存帖子
        int result = postMapper.insertPost(post);
        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "创建帖子失败");
        }

        // 保存标签
        if (!CollectionUtils.isEmpty(request.getTags())) {
            // TODO: 保存标签关联关系
        }

        // 返回帖子详情
        return getPostDetail(postId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostDetailVO updatePost(String postId, PostUpdateRequestVO request, String userId) {
        // 查询帖子
        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "帖子不存在");
        }

        // 权限校验
        if (!userId.equals(post.getAuthorId())) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN.getCode(), "无权限修改此帖子");
        }

        // 更新帖子
        if (StringUtils.hasText(request.getTitle())) {
            post.setTitle(request.getTitle());
        }
        if (StringUtils.hasText(request.getCover())) {
            post.setFrontCover(request.getCover());
        }
        if (StringUtils.hasText(request.getContent())) {
            post.setContent(request.getContent());
        }
        post.setUpdatedAt(new Date());

        int result = postMapper.updatePost(post);
        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "更新帖子失败");
        }

        // 更新标签
        if (!CollectionUtils.isEmpty(request.getTags())) {
            // TODO: 更新标签关联关系
        }

        return getPostDetail(postId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(String postId, String userId) {
        // 查询帖子
        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "帖子不存在");
        }

        // 权限校验
        if (!userId.equals(post.getAuthorId())) {
            throw new BusinessException(ErrorCodeEnum.FORBIDDEN.getCode(), "无权限删除此帖子");
        }

        // 删除帖子（逻辑删除）
        int result = postMapper.deletePost(postId);
        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "删除帖子失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LikeResponseVO likePost(String postId, String action, String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "请先登录");
        }

        // 查询帖子是否存在
        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "帖子不存在");
        }

        // 执行点赞或取消点赞
        if ("like".equals(action)) {
            // 检查是否已点赞
            Integer likeCount = postLikeMapper.selectLikeCount(postId, userId);
            if (likeCount == null || likeCount == 0) {
                postLikeMapper.insertLike(postId, userId);
            }
        } else if ("unlike".equals(action)) {
            postLikeMapper.deleteLike(postId, userId);
        } else {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST.getCode(), "操作类型错误");
        }

        // 查询当前点赞数
        Integer likes = postMapper.countLikes(postId);
        Boolean liked = "like".equals(action);

        LikeResponseVO vo = new LikeResponseVO();
        vo.setLiked(liked);
        vo.setLikes(likes != null ? likes : 0);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CollectResponseVO collectPost(String postId, String action, String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "请先登录");
        }

        // 查询帖子是否存在
        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "帖子不存在");
        }

        // 执行收藏或取消收藏
        if ("collect".equals(action)) {
            // 检查是否已收藏
            Integer collectCount = postCollectMapper.selectCollectCount(postId, userId);
            if (collectCount == null || collectCount == 0) {
                postCollectMapper.insertCollect(postId, userId);
            }
        } else if ("uncollect".equals(action)) {
            postCollectMapper.deleteCollect(postId, userId);
        } else {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST.getCode(), "操作类型错误");
        }

        CollectResponseVO vo = new CollectResponseVO();
        vo.setCollected("collect".equals(action));
        return vo;
    }

    /**
     * 生成帖子ID
     *
     * @return 帖子ID
     */
    private String generatePostId() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 获取专区ID
     *
     * @param zone 专区名称
     * @return 专区ID
     */
    private Integer getZoneId(String zone) {
        if ("practices".equals(zone)) {
            return 1;
        } else if ("tools".equals(zone)) {
            return 3;
        } else if ("agent".equals(zone)) {
            return 5;
        } else if ("empowerment".equals(zone)) {
            return 4;
        }
        return null;
    }

    /**
     * 获取专区名称
     *
     * @param zoneId 专区ID
     * @return 专区名称
     */
    private String getZoneName(Integer zoneId) {
        if (zoneId == null) {
            return null;
        }
        switch (zoneId) {
            case 1:
                return "practices";
            case 3:
                return "tools";
            case 5:
                return "agent";
            case 4:
                return "empowerment";
            default:
                return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostFeaturedSetResponseVO setFeaturedPost(String postId, PostFeaturedSetRequestVO request, String userId) {
        // TODO: 权限校验 - 仅管理员可以操作
        // if (!isAdmin(userId)) {
        //     throw new BusinessException(ErrorCodeEnum.FORBIDDEN.getCode(), "无权限，仅管理员可以操作");
        // }

        // 查询帖子是否存在
        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "帖子不存在");
        }

        // 确定zone和toolId（优先使用请求参数，否则从帖子中获取）
        String zone = request.getZone();
        Integer toolId = request.getToolId();
        if (zone == null) {
            zone = getZoneName(post.getZoneId());
        }
        if (toolId == null) {
            toolId = post.getToolId();
        }

        // 扶摇Agent应用的特殊处理：只能有一个置顶帖子
        if ("agent".equals(zone) && (toolId == null || toolId == -1)) {
            if (request.getFeatured()) {
                // 检查是否已有其他置顶帖子
                Post existingFeaturedPost = postMapper.selectAgentFeaturedPost(5, -1);
                if (existingFeaturedPost != null && !existingFeaturedPost.getPostId().equals(postId)) {
                    String errorMessage = String.format("已有置顶帖子「%s」，请先取消该帖子的置顶后再操作",
                            existingFeaturedPost.getTitle());
                    throw new BusinessException(ErrorCodeEnum.BAD_REQUEST.getCode(), errorMessage);
                }
            }
            // 设置或取消置顶
            if (request.getFeatured()) {
                postMapper.setAgentFeaturedPost(postId);
            } else {
                postMapper.cancelAgentFeaturedPost(5, -1);
            }
        } else {
            // 其他区域：直接设置精华状态
            String essencePost = request.getFeatured() ? "1" : "0";
            int result = postMapper.updatePostEssence(postId, essencePost);
            if (result <= 0) {
                throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "设置精华帖子失败");
            }
        }

        PostFeaturedSetResponseVO vo = new PostFeaturedSetResponseVO();
        vo.setSuccess(true);
        vo.setFeatured(request.getFeatured());
        return vo;
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @return ISO格式日期字符串
     */
    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return ISO_DATE_FORMAT.format(date);
    }
}
