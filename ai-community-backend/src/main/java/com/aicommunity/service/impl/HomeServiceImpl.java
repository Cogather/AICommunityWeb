package com.aicommunity.service.impl;

import com.aicommunity.dto.CarouselResponse;
import com.aicommunity.dto.HonorHomeResponse;
import com.aicommunity.mapper.*;
import com.aicommunity.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页服务实现类
 *
 * @author AI Community Team
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeCarouselMapper homeCarouselMapper;

    @Autowired
    private HonorConfigMapper honorConfigMapper;

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private HonorMapper honorMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public CarouselResponse getCarousel() {
        CarouselResponse response = new CarouselResponse();
        List<CarouselResponse.CarouselItem> items = homeCarouselMapper.selectAll();
        response.setList(items);
        return response;
    }

    @Override
    public HonorHomeResponse getHonor() {
        HonorHomeResponse response = new HonorHomeResponse();

        // 荣誉殿堂信息
        HonorHomeResponse.HonorInfo honorInfo = new HonorHomeResponse.HonorInfo();
        honorInfo.setBannerImage(honorConfigMapper.selectBannerImage());
        
        // 奖项列表
        List<com.aicommunity.entity.Award> awards = awardMapper.selectAll();
        List<HonorHomeResponse.AwardItem> awardItems = awards.stream()
                .map(award -> {
                    HonorHomeResponse.AwardItem item = new HonorHomeResponse.AwardItem();
                    item.setId(award.getId());
                    item.setName(award.getName());
                    item.setDesc(award.getDesc());
                    item.setImage(award.getImage());
                    item.setOrder(award.getOrder());
                    return item;
                })
                .collect(Collectors.toList());
        honorInfo.setAwards(awardItems);

        // AI使用达人Top用户（根据荣誉数量动态计算）
        List<HonorHomeResponse.TopUser> topUsers = honorMapper.selectTopUsers(6);
        honorInfo.setTopUsers(topUsers);

        response.setHonor(honorInfo);

        // 社区头条信息
        HonorHomeResponse.NewsInfo newsInfo = new HonorHomeResponse.NewsInfo();
        List<HonorHomeResponse.NewsItem> newsItems = newsMapper.selectTop(4);
        newsInfo.setList(newsItems);
        response.setNews(newsInfo);

        return response;
    }
}
