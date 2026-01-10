package com.aicommunity.service;

import com.aicommunity.dto.TopContributorResponse;

import java.util.List;

/**
 * 用户贡献者服务接口
 *
 * @author AI Community Team
 */
public interface UserContributorService {
    /**
     * 获取热门贡献者
     *
     * @param zone 专区
     * @param limit 返回数量
     * @return 热门贡献者列表
     */
    List<TopContributorResponse> getTopContributors(String zone, Integer limit);
}
