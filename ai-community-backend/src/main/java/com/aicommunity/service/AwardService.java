package com.aicommunity.service;

import com.aicommunity.dto.AwardRulesResponse;
import com.aicommunity.entity.Award;

import java.util.List;

/**
 * 奖项服务接口
 *
 * @author AI Community Team
 */
public interface AwardService {
    /**
     * 获取奖项列表
     *
     * @param category 奖项分类
     * @return 奖项列表
     */
    List<Award> getAwards(String category);

    /**
     * 获取奖项规则详情
     *
     * @param id 奖项ID
     * @return 奖项规则响应
     */
    AwardRulesResponse getAwardRules(Long id);
}
