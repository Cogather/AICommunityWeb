package com.aicommunity.service;

import com.aicommunity.entity.HomeBannerConfig;
import com.aicommunity.entity.HomeTool;
import com.aicommunity.entity.ToolBanner;
import com.aicommunity.vo.*;

import java.util.List;

/**
 * 首页服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface HomeService {

    /**
     * 获取首页轮播图列表
     *
     * @return 轮播图列表
     */
    List<CarouselItemVO> getCarouselList();

    /**
     * 获取荣誉殿堂数据
     *
     * @return 荣誉殿堂信息
     */
    HonorInfoVO getHonorInfo();

    /**
     * 获取赋能交流列表
     *
     * @param limit 返回数量，默认5
     * @return 帖子列表
     */
    List<EmpowermentPostVO> getEmpowermentList(Integer limit);

    /**
     * 获取AI优秀实践列表
     *
     * @param limit 每个分类返回的数量，默认5
     * @return AI优秀实践信息
     */
    PracticesInfoVO getPracticesInfo(Integer limit);

    /**
     * 获取悬浮工具平台列表
     *
     * @return 工具列表
     */
    List<ToolPlatformItemVO> getToolPlatformList();

    /**
     * 获取工具专区轮播图
     *
     * @param toolId 指定工具ID（可选）
     * @return Banner列表
     */
    List<ToolBannerItemVO> getToolBanners(Long toolId);

    /**
     * 获取最新获奖者列表（用于首页AI使用达人展示）
     *
     * @param limit 返回数量，默认9
     * @return 最新获奖者列表
     */
    List<LatestWinnerVO> getLatestWinners(Integer limit);
}
