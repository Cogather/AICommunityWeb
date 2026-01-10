package com.aicommunity.service.impl;

import com.aicommunity.dto.TopContributorResponse;
import com.aicommunity.mapper.UserContributorMapper;
import com.aicommunity.service.UserContributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户贡献者服务实现类
 *
 * @author AI Community Team
 */
@Service
public class UserContributorServiceImpl implements UserContributorService {

    @Autowired
    private UserContributorMapper userContributorMapper;

    @Override
    public List<TopContributorResponse> getTopContributors(String zone, Integer limit) {
        return userContributorMapper.selectTopContributors(zone, limit);
    }
}
