package com.aicommunity.service;

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
    Object getAwards(String category);

    /**
     * 获取奖项规则
     *
     * @param id 奖项ID
     * @return 奖项规则
     */
    Object getAwardRules(Long id);
}
