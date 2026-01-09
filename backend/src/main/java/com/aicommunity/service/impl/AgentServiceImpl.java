package com.aicommunity.service.impl;

import com.aicommunity.entity.AgentFeaturedPost;
import com.aicommunity.mapper.AgentFeaturedPostMapper;
import com.aicommunity.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 扶摇Agent应用服务实现类
 *
 * @author AI Community Team
 */
@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentFeaturedPostMapper agentFeaturedPostMapper;

    @Override
    public Object getFeaturedPost() {
        AgentFeaturedPost featuredPost = agentFeaturedPostMapper.selectOne();
        if (featuredPost == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", featuredPost.getId());
        result.put("title", featuredPost.getTitle());
        result.put("description", featuredPost.getDescription());
        result.put("author", featuredPost.getAuthor());
        result.put("authorAvatar", featuredPost.getAuthorAvatar());
        result.put("createTime", featuredPost.getCreateTime());
        result.put("views", featuredPost.getViews());
        result.put("comments", featuredPost.getComments());
        result.put("likes", featuredPost.getLikes());
        result.put("cover", featuredPost.getCover());
        result.put("image", featuredPost.getCover()); // 兼容字段
        result.put("link", featuredPost.getLink());

        // TODO: 查询标签（如果有postId关联）
        result.put("tags", new java.util.ArrayList<>());

        return result;
    }
}
