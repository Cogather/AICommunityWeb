package com.aicommunity.service;

/**
 * 首页服务接口
 *
 * @author AI Community Team
 */
public interface HomeService {

    /**
     * 获取首页轮播图
     *
     * @return 轮播图列表
     */
    Object getCarousel();

    /**
     * 获取荣誉殿堂和社区头条信息
     *
     * @return 荣誉殿堂和社区头条数据
     */
    Object getHonor();
}
