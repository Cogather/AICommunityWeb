package com.aicommunity.service.impl;

import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private HomeHonorConfigMapper homeHonorConfigMapper;

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private HonorMapper honorMapper;

    @Autowired
    private HomeNewsMapper homeNewsMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Object getCarousel() {
        List<HomeCarousel> carousels = homeCarouselMapper.selectAll();
        List<Map<String, Object>> list = carousels.stream()
                .map(carousel -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", carousel.getId());
                    map.put("image", carousel.getImage());
                    map.put("title", carousel.getTitle());
                    map.put("desc", carousel.getDesc());
                    map.put("link", carousel.getLink());
                    map.put("showContent", carousel.getShowContent());
                    map.put("order", carousel.getOrder());
                    return map;
                })
                .sorted(Comparator.comparing(m -> (Integer) m.get("order")))
                .collect(Collectors.toList());
        return Map.of("list", list);
    }

    @Override
    public Object getHonor() {
        Map<String, Object> result = new HashMap<>();

        // 获取荣誉殿堂配置
        HomeHonorConfig config = homeHonorConfigMapper.selectOne();
        String bannerImage = config != null ? config.getBannerImage() : null;

        // 获取奖项列表
        List<Award> awards = awardMapper.selectByCategory(null);
        List<Map<String, Object>> awardList = awards.stream()
                .map(award -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", award.getId());
                    map.put("name", award.getName());
                    map.put("desc", award.getDesc());
                    map.put("image", award.getImage());
                    map.put("order", award.getOrder());
                    return map;
                })
                .sorted(Comparator.comparing(m -> (Integer) m.get("order")))
                .collect(Collectors.toList());

        // 获取AI使用达人Top用户（根据荣誉数量动态计算）
        List<Map<String, Object>> topUsers = honorMapper.selectTopUsers(6);
        List<Map<String, Object>> topUserList = topUsers.stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.get("id"));
                    map.put("name", user.get("name"));
                    map.put("avatar", user.get("avatar"));
                    map.put("level", user.get("level"));
                    map.put("department", user.get("department"));
                    return map;
                })
                .collect(Collectors.toList());

        // 构建荣誉殿堂数据
        Map<String, Object> honor = new HashMap<>();
        honor.put("bannerImage", bannerImage);
        honor.put("awards", awardList);
        honor.put("topUsers", topUserList);
        result.put("honor", honor);

        // 获取社区头条（默认返回前4条）
        List<HomeNews> newsList = homeNewsMapper.selectAll();
        List<Map<String, Object>> news = newsList.stream()
                .map(newsItem -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", newsItem.getId());
                    map.put("title", newsItem.getTitle());
                    map.put("image", newsItem.getImage());
                    map.put("date", newsItem.getDate());
                    map.put("link", newsItem.getLink());
                    map.put("order", newsItem.getOrder());
                    return map;
                })
                .sorted(Comparator.comparing(m -> (Integer) m.get("order")))
                .limit(4)
                .collect(Collectors.toList());

        Map<String, Object> newsData = new HashMap<>();
        newsData.put("list", news);
        result.put("news", newsData);

        return result;
    }
}
