package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.entity.Post;
import com.aicommunity.entity.UserInfo;
import com.aicommunity.entity.PostTag;
import com.aicommunity.mapper.PostMapper;
import com.aicommunity.mapper.ToolPostMapper;
import com.aicommunity.mapper.UserInfoMapper;
import com.aicommunity.service.AgentService;
import com.aicommunity.service.ToolService;
import com.aicommunity.vo.AgentFeaturedPostResponseVO;
import com.aicommunity.vo.AgentFeaturedPostSetResponseVO;
import com.aicommunity.vo.AgentFeaturedPostVO;
import com.aicommunity.vo.OwnerPermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * 扶摇Agent应用服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ToolPostMapper toolPostMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ToolService toolService;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final int AGENT_ZONE_ID = 5; // 扶摇Agent应用的zone_id
    private static final int AGENT_TOOL_ID = -1; // 扶摇Agent应用的tool_id

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public AgentFeaturedPostResponseVO getFeaturedPost() {
        // 查询扶摇Agent应用的精华帖子（zone_id=5, tool_id=-1, essence_post='1'）
        Post post = postMapper.selectAgentFeaturedPost(AGENT_ZONE_ID, AGENT_TOOL_ID);
        
        AgentFeaturedPostResponseVO response = new AgentFeaturedPostResponseVO();
        
        if (post == null) {
            response.setPost(null);
            return response;
        }

        // 转换为VO
        AgentFeaturedPostVO postVO = convertToAgentFeaturedPostVO(post);
        response.setPost(postVO);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AgentFeaturedPostSetResponseVO setFeaturedPost(Integer postId, String userId) {
        // 权限校验：检查是否为工具Owner（toolId=-1表示扶摇Agent应用）
        OwnerPermissionVO permission = toolService.checkOwnerPermission(AGENT_TOOL_ID, userId);
        if (!permission.getIsOwner()) {
            throw new BusinessException(403, "无权限，只有工具Owner可以设置置顶帖子");
        }

        // 如果postId为null或0，表示取消置顶
        if (postId == null || postId == 0) {
            // 取消当前置顶帖子
            postMapper.cancelAgentFeaturedPost(AGENT_ZONE_ID, AGENT_TOOL_ID);
            
            AgentFeaturedPostSetResponseVO response = new AgentFeaturedPostSetResponseVO();
            response.setPostId(0);
            response.setSetTime(ISO_DATE_FORMAT.format(new Date()));
            return response;
        }

        // 查询帖子是否存在
        Post post = postMapper.selectByPostId(String.valueOf(postId));
        if (post == null) {
            throw new BusinessException(404, "帖子不存在");
        }

        // 验证帖子是否属于扶摇Agent应用（zone_id=5, tool_id=-1）
        if (post.getZoneId() == null || !post.getZoneId().equals(AGENT_ZONE_ID)) {
            throw new BusinessException(400, "该帖子不属于扶摇Agent应用");
        }
        if (post.getToolId() == null || !post.getToolId().equals(AGENT_TOOL_ID)) {
            throw new BusinessException(400, "该帖子不属于扶摇Agent应用");
        }

        // 检查是否已有其他置顶帖子（排除当前要设置的帖子）
        Post existingFeaturedPost = postMapper.selectAgentFeaturedPost(AGENT_ZONE_ID, AGENT_TOOL_ID);
        if (existingFeaturedPost != null && !existingFeaturedPost.getPostId().equals(String.valueOf(postId))) {
            // 已有其他置顶帖子，返回错误信息
            String errorMessage = String.format("已有置顶帖子「%s」，请先取消该帖子的置顶后再操作", 
                    existingFeaturedPost.getTitle());
            throw new BusinessException(400, errorMessage);
        }

        // 先取消当前置顶帖子（如果存在）
        postMapper.cancelAgentFeaturedPost(AGENT_ZONE_ID, AGENT_TOOL_ID);

        // 设置新的置顶帖子
        postMapper.setAgentFeaturedPost(String.valueOf(postId));

        AgentFeaturedPostSetResponseVO response = new AgentFeaturedPostSetResponseVO();
        response.setPostId(postId);
        response.setSetTime(ISO_DATE_FORMAT.format(new Date()));

        return response;
    }

    /**
     * 将Post实体转换为AgentFeaturedPostVO
     *
     * @param post 帖子实体
     * @return AgentFeaturedPostVO
     */
    private AgentFeaturedPostVO convertToAgentFeaturedPostVO(Post post) {
        AgentFeaturedPostVO vo = new AgentFeaturedPostVO();
        
        // 帖子基本信息
        vo.setId(Integer.parseInt(post.getPostId()));
        vo.setTitle(post.getTitle());
        vo.setDescription(extractDescription(post.getContent()));
        vo.setImage(post.getFrontCover());
        vo.setCover(post.getFrontCover());
        vo.setFeatured(true);
        
        // 时间格式化
        if (post.getCreatedAt() != null) {
            vo.setCreateTime(ISO_DATE_FORMAT.format(post.getCreatedAt()));
        }

        // 查询作者信息
        if (StringUtils.hasText(post.getAuthorId())) {
            UserInfo userInfo = userInfoMapper.selectByUserId(post.getAuthorId());
            if (userInfo != null) {
                vo.setAuthor(userInfo.getChnName() != null ? userInfo.getChnName() : userInfo.getUserName());
                vo.setAuthorId(Integer.parseInt(userInfo.getUserId()));
                vo.setAuthorAvatar(userInfo.getAuthorAvatar());
            }
        }

        // 查询统计信息
        Integer views = post.getViewsNums() != null ? post.getViewsNums() : 0;
        vo.setViews(views);

        Integer comments = postMapper.countComments(post.getPostId());
        vo.setComments(comments != null ? comments : 0);

        Integer likes = postMapper.countLikes(post.getPostId());
        vo.setLikes(likes != null ? likes : 0);

        // 查询标签
        List<PostTag> postTags = toolPostMapper.selectTagsByPostId(post.getPostId());
        List<String> tags = new ArrayList<>();
        if (postTags != null && !postTags.isEmpty()) {
            tags = postTags.stream()
                    .map(PostTag::getTag)
                    .filter(tag -> tag != null && !tag.isEmpty())
                    .collect(Collectors.toList());
        }
        vo.setTags(tags);

        return vo;
    }

    /**
     * 从内容中提取摘要
     *
     * @param content 帖子内容
     * @return 摘要
     */
    private String extractDescription(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        
        // 去除HTML标签
        String text = content.replaceAll("<[^>]*>", "");
        
        // 截取前200个字符
        if (text.length() > 200) {
            text = text.substring(0, 200) + "...";
        }
        
        return text;
    }
}
