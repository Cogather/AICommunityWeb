package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;

/**
 * 荣誉服务接口
 *
 * @author AI Community Team
 */
public interface HonorService {

    /**
     * 获取荣誉列表
     *
     * @param scope      范围
     * @param view       视图
     * @param user       用户名
     * @param filterType 筛选类型
     * @param filterValue 筛选值
     * @param search     搜索关键词
     * @param pageQuery  分页参数
     * @return 荣誉列表
     */
    PageResult<?> getHonors(String scope, String view, String user, String filterType,
                            String filterValue, String search, PageQuery pageQuery);

    /**
     * 获取荣誉影响力
     *
     * @return 荣誉影响力数据
     */
    Object getHonorInfluence();

    /**
     * 给荣誉送花
     *
     * @param id 荣誉ID
     * @return 送花结果
     */
    Object giveFlower(Long id);
}
