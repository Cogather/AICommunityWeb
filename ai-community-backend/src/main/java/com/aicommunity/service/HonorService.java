package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.HonorFlowerResponse;
import com.aicommunity.dto.HonorInfluenceResponse;
import com.aicommunity.entity.Honor;

/**
 * 荣誉服务接口
 *
 * @author AI Community Team
 */
public interface HonorService {
    /**
     * 获取荣誉列表
     *
     * @param scope 范围：all-全部，mine-我的荣誉
     * @param view 视图：grid-荣誉墙，timeline-时光轴
     * @param user 用户名（查看特定用户的时光轴）
     * @param filterType 筛选类型
     * @param filterValue 筛选值
     * @param search 搜索关键词
     * @param pageQuery 分页参数
     * @return 荣誉列表
     */
    PageResult<Honor> getHonors(String scope, String view, String user, String filterType, 
                                String filterValue, String search, PageQuery pageQuery);

    /**
     * 获取荣誉影响力
     *
     * @return 荣誉影响力响应
     */
    HonorInfluenceResponse getHonorInfluence();

    /**
     * 获取AI使用达人Top用户
     *
     * @param limit 返回数量
     * @return Top用户列表
     */
    PageResult<Honor.TopUser> getTopUsers(Integer limit);

    /**
     * 给荣誉送花
     *
     * @param id 荣誉ID
     * @return 送花响应
     */
    HonorFlowerResponse giveFlower(Long id);
}
