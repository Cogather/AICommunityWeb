package com.aicommunity.service.impl;

import com.aicommunity.dto.AgentFeaturedPostResponse;
import com.aicommunity.mapper.AgentFeaturedPostMapper;
import com.aicommunity.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 扶摇Agent服务实现类
 *
 * @author AI Community Team
 */
@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentFeaturedPostMapper agentFeaturedPostMapper;

    @Override
    public AgentFeaturedPostResponse getFeaturedPost() {
        return agentFeaturedPostMapper.selectFeaturedPost();
    }
}
