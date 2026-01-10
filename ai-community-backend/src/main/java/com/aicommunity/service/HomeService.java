package com.aicommunity.service;

import com.aicommunity.dto.CarouselResponse;
import com.aicommunity.dto.HonorHomeResponse;

/**
 * 首页服务接口
 *
 * @author AI Community Team
 */
public interface HomeService {
    /**
     * 获取首页轮播图
     *
     * @return 轮播图响应
     */
    CarouselResponse getCarousel();

    /**
     * 获取荣誉殿堂和社区头条信息
     *
     * @return 荣誉殿堂和社区头条响应
     */
    HonorHomeResponse getHonor();
}
