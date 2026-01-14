package com.aicommunity.service;

import com.aicommunity.vo.CoverListVO;

/**
 * 封面服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface CoverService {

    /**
     * 获取推荐封面
     *
     * @param zone  专区类型（可选）
     * @param count 返回数量
     * @return 封面列表
     */
    CoverListVO getRecommendedCovers(String zone, Integer count);
}
