package com.aicommunity.service.impl;

import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.HomeService;
import com.aicommunity.vo.*;
import com.aicommunity.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 首页服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeBannerConfigMapper homeBannerConfigMapper;

    @Autowired
    private HonorMapper honorMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private HomeToolMapper homeToolMapper;

    @Autowired
    private ToolBannerMapper toolBannerMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PostTagMapper postTagMapper;

    @Override
    public List<CarouselItemVO> getCarouselList() {
        List<HomeBannerConfig> configs = homeBannerConfigMapper.selectAllOrderByOrder();
        if (CollectionUtils.isEmpty(configs)) {
            return new ArrayList<>();
        }
        return configs.stream().map(config -> {
            CarouselItemVO vo = new CarouselItemVO();
            vo.setId(config.getId().intValue());
            vo.setImage(config.getBannerImage());
            vo.setTitle(config.getBannerTitle());
            vo.setDesc(config.getBannerDesc());
            vo.setLink(config.getBannerUrl());
            vo.setShowContent(config.getBannerTitle() != null && !config.getBannerTitle().isEmpty());
            // 使用order字段排序，如果order为null则使用id作为排序值
            vo.setOrder(config.getOrder() != null ? config.getOrder() : config.getId().intValue());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public HonorInfoVO getHonorInfo() {
        HonorInfoVO honorInfo = new HonorInfoVO();
        
        // 获取Banner图片
        HonorBanner banner = honorMapper.selectFirstBanner();
        if (banner != null) {
            honorInfo.setBannerImage(banner.getImage());
        }
        
        // 获取奖项列表
        List<Honor> honors = honorMapper.selectAllHonors();
        List<HonorAwardVO> awards = new ArrayList<>();
        if (!CollectionUtils.isEmpty(honors)) {
            awards = honors.stream().map(honor -> {
                HonorAwardVO award = new HonorAwardVO();
                award.setId(honor.getHonorId());
                award.setName(honor.getHonorName());
                award.setDesc(honor.getHonorDesc());
                award.setImage(honor.getHonorImage());
                return award;
            }).collect(Collectors.toList());
        }
        honorInfo.setAwards(awards);
        
        // 获取Top用户（前6名）
        List<User> topUsers = honorMapper.selectTopUsers(6);
        List<TopUserVO> topUserVOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(topUsers)) {
            topUserVOs = topUsers.stream().map(user -> {
                TopUserVO vo = new TopUserVO();
                // 如果user_id是数字字符串，直接转换；否则使用hashCode
                try {
                    vo.setId(Integer.parseInt(user.getUserId()));
                } catch (NumberFormatException e) {
                    vo.setId(user.getUserId() != null ? user.getUserId().hashCode() : 0);
                }
                
                // 关联用户表查询用户详细信息
                UserInfo userInfo = null;
                if (user.getUserId() != null) {
                    try {
                        userInfo = userInfoMapper.selectByUserId(user.getUserId());
                    } catch (Exception e) {
                        log.warn("查询用户信息失败: userId={}, error={}", user.getUserId(), e.getMessage());
                    }
                }
                
                // 设置用户信息
                if (userInfo != null) {
                    vo.setName(userInfo.getChnName() != null && !userInfo.getChnName().isEmpty() 
                        ? userInfo.getChnName() : userInfo.getUserName());
                    vo.setAvatar(userInfo.getAuthorAvatar());
                    vo.setDepartment(userInfo.getDepartmentL1() != null && !userInfo.getDepartmentL1().isEmpty()
                        ? userInfo.getDepartmentL1() 
                        : (userInfo.getDepartmentL2() != null && !userInfo.getDepartmentL2().isEmpty()
                            ? userInfo.getDepartmentL2() : ""));
                    // 用户等级：从userInfo中获取level字段，如果为null则使用默认值0
                    vo.setLevel(userInfo.getLevel() != null ? userInfo.getLevel() : 0);
                } else {
                    // 如果查询不到用户信息，使用默认值
                    vo.setName(user.getName() != null && !user.getName().isEmpty() 
                        ? user.getName() : "用户" + user.getUserId());
                    vo.setAvatar(user.getAvatar());
                    vo.setDepartment(user.getDepartment());
                    vo.setLevel(user.getLevel() != null ? user.getLevel() : 0);
                }
                
                // 设置荣誉数量
                vo.setHonorCount(user.getHonorCount() != null ? user.getHonorCount() : 0);
                return vo;
            }).collect(Collectors.toList());
        }
        honorInfo.setTopUsers(topUserVOs);
        
        return honorInfo;
    }

    @Override
    public List<EmpowermentPostVO> getEmpowermentList(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 5;
        }
        List<Post> posts = postMapper.selectEmpowermentPosts(limit);
        if (CollectionUtils.isEmpty(posts)) {
            return new ArrayList<>();
        }
        return posts.stream().map(post -> {
            EmpowermentPostVO vo = new EmpowermentPostVO();
            vo.setId(Integer.parseInt(post.getPostId()));
            vo.setTitle(post.getTitle());
            
            // 关联用户表获取作者信息
            String authorName = "用户" + post.getAuthorId();
            String authorDept = "";
            if (post.getAuthorId() != null) {
                try {
                    com.aicommunity.entity.UserInfo userInfo = userInfoMapper.selectByUserId(post.getAuthorId());
                    if (userInfo != null && userInfo.getChnName() != null && !userInfo.getChnName().isEmpty()) {
                        authorName = userInfo.getChnName();
                        authorDept = userInfo.getDepartmentL5();
                    }
                } catch (Exception e) {
                    log.warn("查询用户信息失败: userId={}, error={}", post.getAuthorId(), e.getMessage());
                }
            }
            vo.setAuthor(authorName);
            vo.setAuthorDept(authorDept);

            // 根据label_id查询标签信息
            String tag = "讨论";
            String tagType = "blue";
            if (post.getLabelId() != null) {
                try {
                    com.aicommunity.entity.PostTag postTag = postTagMapper.selectByLabelId(post.getLabelId());
                    if (postTag != null) {
                        tag = postTag.getTag() != null ? postTag.getTag() : "讨论";
                        // 根据color字段确定tagType，如果没有color则使用默认值
                        if (postTag.getColor() != null && !postTag.getColor().isEmpty()) {
                            // 将颜色值映射到tagType（blue/green/orange/red/purple）
                            String color = postTag.getColor().toLowerCase();
                            if (color.contains("blue") || color.startsWith("#36") || color.startsWith("#409")) {
                                tagType = "blue";
                            } else if (color.contains("green") || color.startsWith("#73") || color.startsWith("#52")) {
                                tagType = "green";
                            } else if (color.contains("orange") || color.startsWith("#ff") || color.startsWith("#fa")) {
                                tagType = "orange";
                            } else if (color.contains("red") || color.startsWith("#f5")) {
                                tagType = "red";
                            } else if (color.contains("purple") || color.startsWith("#92") || color.startsWith("#76")) {
                                tagType = "purple";
                            } else {
                                tagType = "blue";
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("查询标签信息失败: labelId={}, error={}", post.getLabelId(), e.getMessage());
                }
            }
            vo.setTag(tag);
            vo.setTagType(tagType);
            
            // 计算相对时间
            vo.setTime(calculateRelativeTime(post.getCreatedAt()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public PracticesInfoVO getPracticesInfo(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 5;
        }
        PracticesInfoVO info = new PracticesInfoVO();
        
        // 查询AI优秀实践帖子（zone_id=1）
        List<Post> posts = postMapper.selectPracticePosts();
        
        // 根据label_id分类（需要根据实际业务逻辑调整）
        Map<String, List<PracticeItemVO>> practices = new HashMap<>();

        List<PostTag> postTags = postTagMapper.selectAllLabel();
        Map<Integer, PostTag> postTagMap = postTags.stream()
            .collect(Collectors.toMap(PostTag::getId, tag -> tag,
                (existing, replacement) -> existing));
        
        if (!CollectionUtils.isEmpty(posts)) {
            for (Post post : posts) {
                PracticeItemVO vo = new PracticeItemVO();
                vo.setId(Integer.parseInt(post.getPostId()));
                vo.setTitle(post.getTitle());
                
                // 关联用户表获取作者信息
                String authorName = "用户" + post.getAuthorId();
                String authorDept = "";
                if (post.getAuthorId() != null) {
                    try {
                        UserInfo userInfo = userInfoMapper.selectByUserId(post.getAuthorId());
                        if (userInfo != null && userInfo.getChnName() != null && !userInfo.getChnName().isEmpty()) {
                            authorName = userInfo.getChnName();
                            authorDept = userInfo.getDepartmentL5();
                        }
                    } catch (Exception e) {
                        log.warn("查询用户信息失败: userId={}, error={}", post.getAuthorId(), e.getMessage());
                    }
                }
                vo.setAuthor(authorName);
                vo.setAuthorDept(authorDept);
                vo.setTime(calculateRelativeTime(post.getCreatedAt()));
                
                // 根据label_id分类
                // 注意：label_id与分类的映射关系需要根据实际业务逻辑确定
                // 这里假设label_id对应：1-培训赋能，2-AI训战，3-用户交流
                // 根据label_id分类
                if (post.getLabelId() != null) {
                    PostTag postTag = postTagMap.get(post.getLabelId());
                    List<PracticeItemVO> practiceItemVOList;
                    if (practices.containsKey(postTag.getTag())) {
                        practiceItemVOList = practices.get(postTag.getTag());
                        vo.setCategory(postTag.getTag());
                        practiceItemVOList.add(vo);
                    } else {
                        practiceItemVOList = new ArrayList<>();
                        vo.setCategory(postTag.getTag());
                        practiceItemVOList.add(vo);
                    }
                    practices.put(postTag.getTag(), practiceItemVOList);
                }
            }
        }

        Map<String, List<PracticeItemVO>> returnPractices = new HashMap<>();
        // 限制每个分类的数量
        for (Map.Entry<String, List<PracticeItemVO>> stringListEntry : practices.entrySet()) {
            List<PracticeItemVO> value = stringListEntry.getValue();
            if (value.size() > limit) {
                value = value.subList(0, limit);
            }
            returnPractices.put(stringListEntry.getKey(), value);
        }
        info.setPractices(returnPractices);

        return info;
    }

    @Override
    public List<ToolPlatformItemVO> getToolPlatformList() {
        List<HomeTool> tools = homeToolMapper.selectAllOrderByOrder();
        if (CollectionUtils.isEmpty(tools)) {
            return new ArrayList<>();
        }
        return tools.stream().map(tool -> {
            ToolPlatformItemVO vo = new ToolPlatformItemVO();
            vo.setId(tool.getId().intValue());
            vo.setName(tool.getName());
            vo.setDesc(tool.getDesc());
            vo.setLogo(tool.getLogo());
            vo.setColor(tool.getColor());
            vo.setPlatformUrl(tool.getLink());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ToolBannerItemVO> getToolBanners(Long toolId) {
        List<ToolBanner> banners = toolBannerMapper.selectToolBanners(toolId);
        if (CollectionUtils.isEmpty(banners)) {
            return new ArrayList<>();
        }
        return banners.stream().map(banner -> {
            ToolBannerItemVO vo = new ToolBannerItemVO();
            vo.setId(banner.getId().intValue());
            vo.setImage(banner.getImage());
            vo.setTitle(banner.getTitle());
            vo.setDesc(banner.getDesc());
            vo.setOrder(banner.getOrder());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LatestWinnerVO> getLatestWinners(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 9;
        }
        List<java.util.Map<String, Object>> winners = honorMapper.selectLatestWinners(limit);
        if (CollectionUtils.isEmpty(winners)) {
            return new ArrayList<>();
        }
        return winners.stream().map(winner -> {
            LatestWinnerVO vo = new LatestWinnerVO();
            vo.setId((Integer) winner.get("id"));
            vo.setName((String) winner.get("name"));
            vo.setAvatar((String) winner.get("avatar"));
            vo.setAwardName((String) winner.get("awardName"));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 计算相对时间
     *
     * @param date 时间
     * @return 相对时间字符串
     */
    private String calculateRelativeTime(java.util.Date date) {
        if (date == null) {
            return "未知";
        }
        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;
        
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + "天前";
        } else if (hours > 0) {
            return hours + "小时前";
        } else if (minutes > 0) {
            return minutes + "分钟前";
        } else {
            return "刚刚";
        }
    }
}
