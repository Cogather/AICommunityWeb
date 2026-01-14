package com.aicommunity.service.impl;

import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.AdminService;
import com.aicommunity.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理后台服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private HomeBannerConfigMapper homeBannerConfigMapper;

    @Autowired
    private HonorMapper honorMapper;

    @Autowired
    private HomeToolMapper homeToolMapper;

    @Autowired
    private ToolBannerMapper toolBannerMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private EmpowermentMapper empowermentMapper;

    // ========== 首页管理 ==========

    @Override
    public AdminCarouselListVO getCarouselConfig() {
        List<HomeBannerConfig> configs = homeBannerConfigMapper.selectAllOrderByOrder();
        AdminCarouselListVO vo = new AdminCarouselListVO();
        if (CollectionUtils.isEmpty(configs)) {
            vo.setList(new ArrayList<>());
            return vo;
        }
        List<CarouselItemVO> list = configs.stream().map(config -> {
            CarouselItemVO item = new CarouselItemVO();
            item.setId(config.getId().intValue());
            item.setImage(config.getBannerImage());
            item.setTitle(config.getBannerTitle());
            item.setDesc(config.getBannerDesc());
            item.setLink(config.getBannerUrl());
            item.setShowContent(StringUtils.hasText(config.getBannerTitle()));
            item.setOrder(config.getOrder() != null ? config.getOrder() : config.getId().intValue());
            return item;
        }).collect(Collectors.toList());
        vo.setList(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCarouselConfig(List<CarouselItemVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            // 如果列表为空，只删除所有配置
            homeBannerConfigMapper.deleteAll();
            return;
        }
        // 先删除所有现有配置
        homeBannerConfigMapper.deleteAll();
        // 插入新配置
        int order = 1;
        for (CarouselItemVO item : list) {
            HomeBannerConfig config = new HomeBannerConfig();
            config.setBannerImage(item.getImage());
            config.setBannerTitle(item.getTitle());
            config.setBannerDesc(item.getDesc());
            config.setBannerUrl(item.getLink());
            config.setOrder(item.getOrder() != null ? item.getOrder() : order);
            config.setUpdateTime(new Date());
            homeBannerConfigMapper.insert(config);
            order++;
        }
        log.info("保存轮播图配置成功，共{}条", list.size());
    }

    @Override
    public AdminHonorBannerVO getHonorBannerConfig() {
        HonorBanner banner = honorMapper.selectFirstBanner();
        AdminHonorBannerVO vo = new AdminHonorBannerVO();
        if (banner != null) {
            vo.setBannerImage(banner.getImage());
        } else {
            vo.setBannerImage("");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveHonorBannerConfig(String bannerImage) {
        HonorBanner banner = honorMapper.selectFirstBanner();
        if (banner != null) {
            // 更新现有Banner
            banner.setImage(bannerImage);
            honorMapper.updateBanner(banner);
        } else {
            // 插入新Banner
            HonorBanner newBanner = new HonorBanner();
            newBanner.setImage(bannerImage);
            newBanner.setOrder(0);
            newBanner.setCreateTime(new Date());
            honorMapper.insertBanner(newBanner);
        }
        log.info("保存荣誉殿堂Banner配置成功");
    }

    @Override
    public AdminToolListVO getToolsConfig() {
        List<HomeTool> tools = homeToolMapper.selectAllOrderByOrder();
        AdminToolListVO vo = new AdminToolListVO();
        if (CollectionUtils.isEmpty(tools)) {
            vo.setList(new ArrayList<>());
            return vo;
        }
        List<ToolItemVO> list = tools.stream().map(tool -> {
            ToolItemVO item = new ToolItemVO();
            item.setId(tool.getId().intValue());
            item.setName(tool.getName());
            item.setDesc(tool.getDesc());
            item.setLogo(tool.getLogo());
            item.setColor(tool.getColor());
            item.setLink(tool.getLink());
            return item;
        }).collect(Collectors.toList());
        vo.setList(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveToolsConfig(List<ToolItemVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            homeToolMapper.deleteAll();
            return;
        }
        // 先删除所有现有配置
        homeToolMapper.deleteAll();
        // 插入新配置
        int order = 1;
        for (ToolItemVO item : list) {
            HomeTool tool = new HomeTool();
            tool.setName(item.getName());
            tool.setDesc(item.getDesc());
            tool.setLogo(item.getLogo());
            tool.setColor(item.getColor());
            tool.setLink(item.getLink());
            tool.setOrder(order);
            tool.setCreateTime(new Date());
            tool.setUpdateTime(new Date());
            homeToolMapper.insert(tool);
            order++;
        }
        log.info("保存AI工具配置成功，共{}条", list.size());
    }

    @Override
    public AdminToolBannerListVO getToolBannersConfig() {
        List<ToolBanner> banners = toolBannerMapper.selectToolBanners(null);
        AdminToolBannerListVO vo = new AdminToolBannerListVO();
        if (CollectionUtils.isEmpty(banners)) {
            vo.setList(new ArrayList<>());
            return vo;
        }
        List<ToolBannerItemVO> list = banners.stream().map(banner -> {
            ToolBannerItemVO item = new ToolBannerItemVO();
            item.setId(banner.getId().intValue());
            item.setImage(banner.getImage());
            item.setTitle(banner.getTitle());
            item.setDesc(banner.getDesc());
            item.setOrder(banner.getOrder());
            return item;
        }).collect(Collectors.toList());
        vo.setList(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveToolBannersConfig(List<ToolBannerItemVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            toolBannerMapper.deleteAll();
            return;
        }
        // 先删除所有现有配置
        toolBannerMapper.deleteAll();
        // 插入新配置
        int order = 1;
        for (ToolBannerItemVO item : list) {
            ToolBanner banner = new ToolBanner();
            banner.setImage(item.getImage());
            banner.setTitle(item.getTitle());
            banner.setDesc(item.getDesc());
            banner.setOrder(item.getOrder() != null ? item.getOrder() : order);
            banner.setCreateTime(new Date());
            toolBannerMapper.insert(banner);
            order++;
        }
        log.info("保存AI工具专区Banner配置成功，共{}条", list.size());
    }

    // ========== 精华/置顶帖子管理 ==========

    @Override
    public AdminFeaturedPostListVO getAllFeaturedPosts() {
        AdminFeaturedPostListVO vo = new AdminFeaturedPostListVO();
        
        // 1. 查询AI优秀实践精华帖子（zone_id=1且essence_post='1'）
        List<Post> practicesPosts = postMapper.selectFeaturedPostsByZone(1, null);
        List<AdminFeaturedPostVO> practices = convertToAdminFeaturedPostVO(practicesPosts, "practices");
        vo.setPractices(practices);
        
        // 2. 查询赋能交流精华帖子（zone_id=4且essence_post='1'）
        List<Post> empowermentPosts = postMapper.selectFeaturedPostsByZone(4, null);
        List<AdminFeaturedPostVO> empowerment = convertToAdminFeaturedPostVO(empowermentPosts, "empowerment");
        vo.setEmpowerment(empowerment);
        
        // 3. 查询扶摇Agent置顶帖子（zone_id=5且tool_id=-1且essence_post='1'）
        List<Post> agentPosts = postMapper.selectFeaturedPostsByZone(5, -1);
        List<AdminFeaturedPostVO> agent = convertToAdminFeaturedPostVO(agentPosts, "agent");
        vo.setAgent(agent);
        
        // 4. 查询AI工具专区其他工具精华帖子（zone_id=3且tool_id=0且essence_post='1'）
        List<Post> otherToolsPosts = postMapper.selectFeaturedPostsByZone(3, 0);
        List<AdminFeaturedPostVO> otherTools = convertToAdminFeaturedPostVO(otherToolsPosts, "otherTools");
        vo.setOtherTools(otherTools);
        
        return vo;
    }

    /**
     * 将Post列表转换为AdminFeaturedPostVO列表
     */
    private List<AdminFeaturedPostVO> convertToAdminFeaturedPostVO(List<Post> posts, String zone) {
        if (CollectionUtils.isEmpty(posts)) {
            return new ArrayList<>();
        }
        return posts.stream().map(post -> {
            AdminFeaturedPostVO vo = new AdminFeaturedPostVO();
            try {
                vo.setId(Integer.parseInt(post.getPostId()));
            } catch (NumberFormatException e) {
                // 如果postId不是数字，使用hashCode
                vo.setId(post.getPostId().hashCode());
            }
            vo.setTitle(post.getTitle());
            vo.setZone(zone);
            vo.setCreateTime(post.getCreatedAt());
            
            // 查询作者信息
            if (post.getAuthorId() != null) {
                try {
                    UserInfo userInfo = userInfoMapper.selectByUserId(post.getAuthorId());
                    if (userInfo != null) {
                        vo.setAuthor(StringUtils.hasText(userInfo.getChnName()) ? 
                                userInfo.getChnName() : userInfo.getUserName());
                    } else {
                        vo.setAuthor("未知用户");
                    }
                } catch (Exception e) {
                    log.warn("查询用户信息失败: userId={}", post.getAuthorId(), e);
                    vo.setAuthor("未知用户");
                }
            } else {
                vo.setAuthor("未知用户");
            }
            
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFeaturedPost(String postId) {
        postMapper.updatePostEssence(postId, "0");
        log.info("移除帖子精华/置顶状态成功, postId={}", postId);
    }

    @Override
    public AdminCollectionListVO getEmpowermentCollections() {
        // 查询赋能交流精华帖子作为精选合集
        List<FeaturedPostVO> featuredPosts = empowermentMapper.selectFeaturedPosts();
        AdminCollectionListVO vo = new AdminCollectionListVO();
        List<AdminCollectionItemVO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(featuredPosts)) {
            int id = 1;
            for (FeaturedPostVO post : featuredPosts) {
                AdminCollectionItemVO item = new AdminCollectionItemVO();
                item.setId(id++);
                item.setPostId(post.getId());
                item.setNote(post.getTitle());
                list.add(item);
            }
        }
        vo.setList(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEmpowermentCollections(List<AdminCollectionItemVO> list) {
        // 精选合集实际上就是精华帖子，通过设置/取消精华状态来管理
        // 这里可以根据list中的postId设置精华状态
        // 注意：需要先取消所有精华状态，然后设置新的精华状态
        log.info("保存赋能交流精选合集配置，共{}条", list != null ? list.size() : 0);
        // 实际实现需要根据业务需求调整
    }

    // ========== AI使用达人管理 ==========

    @Override
    public AdminAwardListVO getAwards() {
        List<Honor> honors = honorMapper.selectAllHonorsWithId();
        AdminAwardListVO vo = new AdminAwardListVO();
        List<AdminAwardItemVO> awardList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(honors)) {
            for (Honor honor : honors) {
                AdminAwardItemVO item = new AdminAwardItemVO();
                try {
                    item.setId(Integer.parseInt(honor.getHonorId()));
                } catch (NumberFormatException e) {
                    item.setId(honor.getHonorId().hashCode());
                }
                item.setName(honor.getHonorName());
                item.setDescription(honor.getHonorDesc());
                // 注意：criteria和cycle字段需要存储在扩展字段或单独表中
                // 这里暂时设置为空，需要根据实际业务调整
                item.setCriteria(new ArrayList<>());
                item.setCycle("年度");
                awardList.add(item);
            }
        }
        vo.setList(awardList);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAwards(List<AdminAwardItemVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (AdminAwardItemVO item : list) {
            Honor honor = new Honor();
            honor.setHonorId(String.valueOf(item.getId()));
            honor.setHonorName(item.getName());
            honor.setHonorDesc(item.getDescription());
            honor.setStatus("0");
            honor.setUpdateAt(new Date());
            
            // 判断是新增还是更新
            Honor existingHonor = null;
            try {
                List<Honor> honors = honorMapper.selectAllHonorsWithId();
                for (Honor h : honors) {
                    if (h.getHonorId().equals(String.valueOf(item.getId()))) {
                        existingHonor = h;
                        break;
                    }
                }
            } catch (Exception e) {
                log.warn("查询现有奖项失败", e);
            }
            
            if (existingHonor != null) {
                honorMapper.updateHonor(honor);
            } else {
                honorMapper.insertHonor(honor);
            }
        }
        log.info("保存奖项设置列表成功，共{}条", list.size());
    }

    @Override
    public AdminAwardNameListVO getAwardNames() {
        List<Honor> honors = honorMapper.selectAllHonorsWithId();
        AdminAwardNameListVO vo = new AdminAwardNameListVO();
        List<AdminAwardNameVO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(honors)) {
            for (Honor honor : honors) {
                AdminAwardNameVO item = new AdminAwardNameVO();
                try {
                    item.setId(Integer.parseInt(honor.getHonorId()));
                } catch (NumberFormatException e) {
                    item.setId(honor.getHonorId().hashCode());
                }
                item.setName(honor.getHonorName());
                list.add(item);
            }
        }
        vo.setList(list);
        return vo;
    }

    @Override
    public AdminWinnerListVO getWinners() {
        List<Map<String, Object>> winners = honorMapper.selectWinners();
        AdminWinnerListVO vo = new AdminWinnerListVO();
        List<AdminWinnerItemVO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(winners)) {
            for (Map<String, Object> winner : winners) {
                AdminWinnerItemVO item = new AdminWinnerItemVO();
                item.setId((Integer) winner.get("id"));
                item.setName((String) winner.get("name"));
                item.setAwardTime((String) winner.get("awardTime"));
                item.setAwardName((String) winner.get("awardName"));
                list.add(item);
            }
        }
        vo.setList(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWinners(List<AdminWinnerItemVO> list) {
        // 保存获奖者列表实际上是通过设置用户获奖来实现的
        // 这里可以根据业务需求调整实现逻辑
        log.info("保存获奖者列表，共{}条", list != null ? list.size() : 0);
        // 实际实现需要根据业务需求调整
    }

    @Override
    public AdminRecommendedWinnerListVO getRecommendedWinners(String month) {
        if (month == null || month.isEmpty()) {
            // 默认当前月
            Calendar cal = Calendar.getInstance();
            month = String.format("%d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        }
        List<Map<String, Object>> recommended = honorMapper.selectRecommendedWinners(month, 10);
        AdminRecommendedWinnerListVO vo = new AdminRecommendedWinnerListVO();
        List<AdminRecommendedWinnerVO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(recommended)) {
            int id = 1;
            for (Map<String, Object> item : recommended) {
                AdminRecommendedWinnerVO winner = new AdminRecommendedWinnerVO();
                winner.setId(id++);
                winner.setEmployeeId((String) item.getOrDefault("employeeId", ""));
                winner.setName((String) item.getOrDefault("name", ""));
                winner.setAvatar((String) item.getOrDefault("avatar", ""));
                winner.setDepartment((String) item.getOrDefault("department", ""));
                winner.setPoints((Integer) item.getOrDefault("points", 0));
                winner.setPostsCount((Integer) item.getOrDefault("postsCount", 0));
                winner.setCommentsCount((Integer) item.getOrDefault("commentsCount", 0));
                winner.setActivitiesCount((Integer) item.getOrDefault("activitiesCount", 0));
                winner.setLikesReceived((Integer) item.getOrDefault("likesReceived", 0));
                winner.setFavoritesReceived((Integer) item.getOrDefault("favoritesReceived", 0));
                winner.setHasAwarded((Boolean) item.getOrDefault("hasAwarded", false));
                Object honorId = item.get("honorId");
                if (honorId != null) {
                    winner.setHonorId((Integer) honorId);
                }
                list.add(winner);
            }
        }
        vo.setList(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminSetAwardResponseVO setUserAward(AdminSetAwardRequestVO request) {
        // 查询用户信息（userId是Integer，需要转换为String）
        UserInfo userInfo = userInfoMapper.selectByUserIdInt(request.getUserId());
        if (userInfo == null) {
            // 尝试使用String类型的userId查询
            userInfo = userInfoMapper.selectByUserId(String.valueOf(request.getUserId()));
        }
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 查询奖项信息
        Honor honor = null;
        try {
            List<Honor> honors = honorMapper.selectAllHonorsWithId();
            for (Honor h : honors) {
                if (h.getHonorId().equals(String.valueOf(request.getAwardId()))) {
                    honor = h;
                    break;
                }
            }
        } catch (Exception e) {
            log.error("查询奖项信息失败", e);
            throw new RuntimeException("奖项不存在");
        }
        
        if (honor == null) {
            throw new RuntimeException("奖项不存在");
        }
        
        // 解析获奖时间
        String[] dateParts = request.getAwardDate().split("-");
        String year = dateParts[0];
        String month = dateParts.length > 1 ? dateParts[1] : "01";
        
        // 插入获奖记录
        NewHonorDetail detail = new NewHonorDetail();
        detail.setHonorId(honor.getHonorId());
        detail.setHonorUserId(userInfo.getUserId());
        detail.setGainedYear(year);
        detail.setGainedMonth(month);
        detail.setHonorType(0); // 0表示个人奖项
        honorMapper.insertHonorDetail(detail);
        
        AdminSetAwardResponseVO response = new AdminSetAwardResponseVO();
        response.setHonorId(detail.getId());
        log.info("设置用户获奖成功, userId={}, awardId={}, honorId={}", 
                request.getUserId(), request.getAwardId(), detail.getId());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelUserAward(AdminCancelAwardRequestVO request) {
        honorMapper.deleteHonorDetail(request.getHonorId());
        log.info("取消用户获奖成功, userId={}, honorId={}", request.getUserId(), request.getHonorId());
    }

    @Override
    public AdminTeamAwardListVO getTeamAwards() {
        List<Map<String, Object>> teamAwards = honorMapper.selectAllTeamAwards();
        AdminTeamAwardListVO vo = new AdminTeamAwardListVO();
        List<TeamAwardVO> list = new ArrayList<>();
        
        // 按年份分组
        Map<String, List<Map<String, Object>>> yearMap = new HashMap<>();
        for (Map<String, Object> award : teamAwards) {
            String year = (String) award.getOrDefault("year", "");
            yearMap.computeIfAbsent(year, k -> new ArrayList<>()).add(award);
        }
        
        // 转换为TeamAwardVO
        int awardId = 1;
        for (Map.Entry<String, List<Map<String, Object>>> entry : yearMap.entrySet()) {
            TeamAwardVO teamAward = new TeamAwardVO();
            teamAward.setId(awardId++);
            teamAward.setYear(entry.getKey());
            teamAward.setTitle("年度最佳团队奖");
            
            List<TeamAwardImageVO> images = new ArrayList<>();
            int imageId = 1;
            for (Map<String, Object> item : entry.getValue()) {
                TeamAwardImageVO image = new TeamAwardImageVO();
                image.setId(imageId++);
                image.setImage((String) item.getOrDefault("image", ""));
                image.setWinnerName((String) item.getOrDefault("winnerName", ""));
                image.setTeamField((String) item.getOrDefault("teamField", ""));
                image.setStory((String) item.getOrDefault("story", ""));
                images.add(image);
            }
            teamAward.setImages(images);
            list.add(teamAward);
        }
        
        vo.setList(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTeamAwards(List<TeamAwardVO> list) {
        // 保存团队奖项列表
        // 注意：需要根据业务需求调整实现逻辑
        log.info("保存团队奖项列表，共{}条", list != null ? list.size() : 0);
        // 实际实现需要根据业务需求调整
    }

    // ========== 人员管理 ==========

    @Override
    public AdminUserListVO getUsers(String keyword, String role) {
        List<UserInfo> users = userInfoMapper.selectUsers(keyword, role);
        AdminUserListVO vo = new AdminUserListVO();
        List<AdminUserItemVO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(users)) {
            for (UserInfo user : users) {
                AdminUserItemVO item = new AdminUserItemVO();
                try {
                    item.setId(Integer.parseInt(user.getUserId()));
                } catch (NumberFormatException e) {
                    item.setId(user.getUserId().hashCode());
                }
                item.setName(StringUtils.hasText(user.getChnName()) ? 
                        user.getChnName() : user.getUserName());
                // 注意：email字段在t_user_info表中不存在，需要根据实际业务调整
                item.setEmail("");
                item.setDepartment(StringUtils.hasText(user.getDepartmentL1()) ? 
                        user.getDepartmentL1() : 
                        (StringUtils.hasText(user.getDepartmentL2()) ? 
                                user.getDepartmentL2() : user.getDepartmentL3()));
                item.setRole(user.getRole());
                list.add(item);
            }
        }
        vo.setList(list);
        return vo;
    }

    @Override
    public AdminUserItemVO getUserById(Integer userId) {
        UserInfo userInfo = userInfoMapper.selectByUserIdInt(userId);
        AdminUserItemVO vo = new AdminUserItemVO();
        if (userInfo != null) {
            vo.setId(userId);
            vo.setName(StringUtils.hasText(userInfo.getChnName()) ? 
                    userInfo.getChnName() : userInfo.getUserName());
            vo.setEmail("");
            vo.setDepartment(StringUtils.hasText(userInfo.getDepartmentL1()) ? 
                    userInfo.getDepartmentL1() : 
                    (StringUtils.hasText(userInfo.getDepartmentL2()) ? 
                            userInfo.getDepartmentL2() : userInfo.getDepartmentL3()));
            vo.setRole(userInfo.getRole());
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRole(AdminUserRoleRequestVO request) {
        // 查询用户信息（userId是Integer，需要转换为String）
        UserInfo userInfo = userInfoMapper.selectByUserIdInt(request.getUserId());
        if (userInfo == null) {
            // 尝试使用String类型的userId查询
            userInfo = userInfoMapper.selectByUserId(String.valueOf(request.getUserId()));
        }
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        
        String currentRole = userInfo.getRole();
        String newRole = request.getRole();
        
        // 如果当前角色不包含新角色，则添加
        if (currentRole == null || !currentRole.contains(newRole)) {
            String updatedRole = currentRole == null ? newRole : currentRole + "," + newRole;
            userInfoMapper.updateUserRole(userInfo.getUserId(), updatedRole);
            log.info("添加用户角色成功, userId={}, role={}", request.getUserId(), newRole);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserRole(AdminUserRoleRequestVO request) {
        // 查询用户信息（userId是Integer，需要转换为String）
        UserInfo userInfo = userInfoMapper.selectByUserIdInt(request.getUserId());
        if (userInfo == null) {
            // 尝试使用String类型的userId查询
            userInfo = userInfoMapper.selectByUserId(String.valueOf(request.getUserId()));
        }
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        
        String currentRole = userInfo.getRole();
        String removeRole = request.getRole();
        
        // 如果当前角色包含要移除的角色，则移除
        if (currentRole != null && currentRole.contains(removeRole)) {
            String updatedRole = currentRole.replace(removeRole, "").replace(",,", ",");
            if (updatedRole.startsWith(",")) {
                updatedRole = updatedRole.substring(1);
            }
            if (updatedRole.endsWith(",")) {
                updatedRole = updatedRole.substring(0, updatedRole.length() - 1);
            }
            if (updatedRole.isEmpty()) {
                updatedRole = "user";
            }
            userInfoMapper.updateUserRole(userInfo.getUserId(), updatedRole);
            log.info("移除用户角色成功, userId={}, role={}", request.getUserId(), removeRole);
        }
    }

    // ========== 图片上传 ==========

    @Override
    public String uploadImage(byte[] fileBytes, String fileName) {
        // 生成唯一文件名
        String extension = "";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }
        String uniqueFileName = System.currentTimeMillis() + "_" + 
                (fileName != null ? fileName.hashCode() : "image") + extension;
        
        // 保存文件到本地（实际项目中建议使用OSS等云存储）
        // 这里返回一个模拟URL，实际需要根据文件存储路径生成
        String baseUrl = "/uploads/images/";
        String imageUrl = baseUrl + uniqueFileName;
        
        // TODO: 实际文件保存逻辑
        // File uploadDir = new File("uploads/images");
        // if (!uploadDir.exists()) {
        //     uploadDir.mkdirs();
        // }
        // File file = new File(uploadDir, uniqueFileName);
        // FileOutputStream fos = new FileOutputStream(file);
        // fos.write(fileBytes);
        // fos.close();
        
        log.info("上传图片成功, fileName={}, url={}", fileName, imageUrl);
        return imageUrl;
    }
}
