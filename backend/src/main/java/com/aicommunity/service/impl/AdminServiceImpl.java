package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.AdminController;
import com.aicommunity.entity.*;
import com.aicommunity.mapper.*;
import com.aicommunity.service.AdminService;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理后台服务实现类
 *
 * @author AI Community Team
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private HomeCarouselMapper homeCarouselMapper;

    @Autowired
    private HomeHonorConfigMapper homeHonorConfigMapper;

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private HomeNewsMapper homeNewsMapper;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private ToolBannerMapper toolBannerMapper;

    @Autowired
    private AgentFeaturedPostMapper agentFeaturedPostMapper;

    @Autowired
    private RecommendedCoverMapper recommendedCoverMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Autowired
    private HonorMapper honorMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Value("${file.upload.path:/uploads}")
    private String uploadPath;

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
    @Transactional(rollbackFor = Exception.class)
    public void saveCarousel(AdminController.SaveCarouselRequest request) {
        verifyAdmin();
        // 删除旧配置
        homeCarouselMapper.deleteAll();
        // 批量插入新配置
        if (request.getList() != null) {
            for (AdminController.SaveCarouselRequest.CarouselItem item : request.getList()) {
                HomeCarousel carousel = new HomeCarousel();
                carousel.setImage(item.getImage());
                carousel.setTitle(item.getTitle());
                carousel.setDesc(item.getDesc());
                carousel.setLink(item.getLink());
                carousel.setShowContent(item.getShowContent());
                carousel.setOrder(item.getOrder());
                carousel.setCreateTime(LocalDateTime.now());
                carousel.setUpdateTime(LocalDateTime.now());
                homeCarouselMapper.insert(carousel);
            }
        }
    }

    @Override
    public Object getHonorBanner() {
        HomeHonorConfig config = homeHonorConfigMapper.selectOne();
        return Map.of("bannerImage", config != null ? config.getBannerImage() : "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveHonorBanner(AdminController.SaveHonorBannerRequest request) {
        verifyAdmin();
        HomeHonorConfig config = homeHonorConfigMapper.selectOne();
        if (config == null) {
            config = new HomeHonorConfig();
            config.setBannerImage(request.getBannerImage());
            config.setUpdateTime(LocalDateTime.now());
            homeHonorConfigMapper.insert(config);
        } else {
            config.setBannerImage(request.getBannerImage());
            config.setUpdateTime(LocalDateTime.now());
            homeHonorConfigMapper.update(config);
        }
    }

    @Override
    public Object getHonorAwards() {
        List<Award> awards = awardMapper.selectByCategory(null);
        List<Map<String, Object>> list = awards.stream()
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
        return Map.of("list", list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveHonorAwards(AdminController.SaveHonorAwardsRequest request) {
        verifyAdmin();
        // 更新奖项配置（这里简化处理，实际应该更新award表的order字段）
        // TODO: 实现更新逻辑
    }

    @Override
    public Object getNews() {
        List<HomeNews> newsList = homeNewsMapper.selectAll();
        List<Map<String, Object>> list = newsList.stream()
                .map(news -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", news.getId());
                    map.put("title", news.getTitle());
                    map.put("image", news.getImage());
                    map.put("date", news.getDate());
                    map.put("link", news.getLink());
                    map.put("order", news.getOrder());
                    return map;
                })
                .sorted(Comparator.comparing(m -> (Integer) m.get("order")))
                .collect(Collectors.toList());
        return Map.of("list", list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNews(AdminController.SaveNewsRequest request) {
        verifyAdmin();
        // 删除旧配置
        homeNewsMapper.deleteAll();
        // 批量插入新配置
        if (request.getList() != null) {
            for (AdminController.SaveNewsRequest.NewsItem item : request.getList()) {
                HomeNews news = new HomeNews();
                news.setTitle(item.getTitle());
                news.setImage(item.getImage());
                news.setDate(item.getDate());
                news.setLink(item.getLink());
                news.setOrder(item.getOrder());
                news.setCreateTime(LocalDateTime.now());
                news.setUpdateTime(LocalDateTime.now());
                homeNewsMapper.insert(news);
            }
        }
    }

    @Override
    public Object getTools() {
        List<Map<String, Object>> tools = toolMapper.selectAll(null);
        List<Map<String, Object>> result = tools.stream()
                .map(tool -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tool.get("id"));
                    map.put("name", tool.get("name"));
                    map.put("desc", tool.get("desc"));
                    map.put("logo", tool.get("logo"));
                    map.put("color", tool.get("color"));
                    map.put("link", tool.get("link"));

                    // 查询工具Banner
                    Long toolId = (Long) tool.get("id");
                    List<ToolBanner> banners = toolBannerMapper.selectByToolId(toolId);
                    List<Map<String, Object>> bannerList = banners.stream()
                            .map(banner -> {
                                Map<String, Object> bannerMap = new HashMap<>();
                                bannerMap.put("id", banner.getId());
                                bannerMap.put("image", banner.getImage());
                                bannerMap.put("title", banner.getTitle());
                                bannerMap.put("desc", banner.getDesc());
                                bannerMap.put("order", banner.getOrder());
                                return bannerMap;
                            })
                            .sorted(Comparator.comparing(m -> (Integer) m.get("order")))
                            .collect(Collectors.toList());
                    map.put("banners", bannerList);

                    return map;
                })
                .collect(Collectors.toList());

        return Map.of("list", result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTools(AdminController.SaveToolsRequest request) {
        verifyAdmin();
        // TODO: 实现工具配置保存逻辑（包括工具和Banner的保存）
    }

    @Override
    public Object getFeaturedPost() {
        AgentFeaturedPost featuredPost = agentFeaturedPostMapper.selectOne();
        if (featuredPost == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", featuredPost.getId());
        result.put("title", featuredPost.getTitle());
        result.put("description", featuredPost.getDescription());
        result.put("author", featuredPost.getAuthor());
        result.put("authorAvatar", featuredPost.getAuthorAvatar());
        result.put("createTime", featuredPost.getCreateTime());
        result.put("views", featuredPost.getViews());
        result.put("comments", featuredPost.getComments());
        result.put("likes", featuredPost.getLikes());
        result.put("cover", featuredPost.getCover());
        result.put("link", featuredPost.getLink());

        // TODO: 查询标签（如果有postId关联）
        result.put("tags", new ArrayList<>());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFeaturedPost(AdminController.SaveFeaturedPostRequest request) {
        verifyAdmin();
        AgentFeaturedPost featuredPost = agentFeaturedPostMapper.selectOne();
        if (featuredPost == null) {
            featuredPost = new AgentFeaturedPost();
        }

        featuredPost.setPostId(request.getId());
        featuredPost.setTitle(request.getTitle());
        featuredPost.setDescription(request.getDescription());
        featuredPost.setAuthor(request.getAuthor());
        featuredPost.setAuthorAvatar(request.getAuthorAvatar());
        featuredPost.setCreateTime(request.getCreateTime());
        featuredPost.setViews(request.getViews());
        featuredPost.setComments(request.getComments());
        featuredPost.setLikes(request.getLikes());
        featuredPost.setCover(request.getCover());
        featuredPost.setLink(request.getLink());
        featuredPost.setUpdateTime(LocalDateTime.now());

        if (featuredPost.getId() == null) {
            agentFeaturedPostMapper.insert(featuredPost);
        } else {
            agentFeaturedPostMapper.update(featuredPost);
        }
    }

    @Override
    public Object getRecommendedCovers() {
        List<RecommendedCover> covers = recommendedCoverMapper.selectAll();
        List<Map<String, Object>> list = covers.stream()
                .map(cover -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", cover.getId());
                    map.put("url", cover.getUrl());
                    map.put("thumbnail", cover.getThumbnail());
                    map.put("order", cover.getOrder());
                    return map;
                })
                .sorted(Comparator.comparing(m -> (Integer) m.get("order")))
                .collect(Collectors.toList());
        return Map.of("list", list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecommendedCovers(AdminController.SaveRecommendedCoversRequest request) {
        verifyAdmin();
        // 删除旧配置
        recommendedCoverMapper.deleteAll();
        // 批量插入新配置
        if (request.getList() != null) {
            for (AdminController.SaveRecommendedCoversRequest.CoverItem item : request.getList()) {
                RecommendedCover cover = new RecommendedCover();
                cover.setUrl(item.getUrl());
                cover.setThumbnail(item.getThumbnail());
                cover.setOrder(item.getOrder());
                cover.setCreateTime(LocalDateTime.now());
                recommendedCoverMapper.insert(cover);
            }
        }
    }

    @Override
    public Object searchUsers(String employeeId, String name, String email, String role) {
        verifyAdmin();
        List<Map<String, Object>> users = userMapper.searchUsers(employeeId, name, email, role);
        List<Map<String, Object>> list = users.stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.get("id"));
                    map.put("employeeId", user.get("employeeId"));
                    map.put("name", user.get("name"));
                    map.put("email", user.get("email"));
                    map.put("department", user.get("department"));
                    map.put("avatar", user.get("avatar"));
                    return map;
                })
                .collect(Collectors.toList());
        return Map.of("list", list);
    }

    @Override
    public PageResult<?> getUsers(String role, Long toolId, String search, PageQuery pageQuery) {
        verifyAdmin();
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Map<String, Object>> users = userMapper.selectUsersByCondition(role, toolId, search);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(users);

        // 查询用户角色和拥有的工具
        List<Map<String, Object>> resultList = users.stream()
                .map(user -> {
                    Long userId = (Long) user.get("id");
                    List<String> roles = userRoleMapper.selectRolesByUserId(userId);
                    user.put("roles", roles);

                    // 查询拥有的工具
                    if (roles.contains("owner")) {
                        List<UserRole> ownerRoles = userRoleMapper.selectByUserIdAndRole(userId, "owner");
                        List<Map<String, Object>> ownedTools = ownerRoles.stream()
                                .map(ownerRole -> {
                                    Map<String, Object> toolMap = new HashMap<>();
                                    toolMap.put("toolId", ownerRole.getToolId());
                                    // TODO: 查询工具名称
                                    return toolMap;
                                })
                                .collect(Collectors.toList());
                        user.put("ownedTools", ownedTools);
                    }

                    return user;
                })
                .collect(Collectors.toList());

        return PageResult.of(resultList, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRole(Long userId, AdminController.AddUserRoleRequest request) {
        verifyAdmin();
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRole(request.getRole());
        userRole.setToolId(request.getToolId());
        userRole.setCreateTime(LocalDateTime.now());
        userRoleMapper.insert(userRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserRole(Long userId, AdminController.RemoveUserRoleRequest request) {
        verifyAdmin();
        userRoleMapper.delete(userId, request.getRole(), request.getToolId());
    }

    @Override
    public Object getRecommendedWinners(String month, Integer limit) {
        verifyAdmin();
        if (month == null) {
            month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        List<Map<String, Object>> winners = pointsRecordMapper.selectTopUsersByMonth(month, limit);
        List<Map<String, Object>> resultList = winners.stream()
                .map(winner -> {
                    Long userId = (Long) winner.get("id");
                    User user = userMapper.selectById(userId);
                    if (user != null) {
                        winner.put("employeeId", user.getEmployeeId());
                        winner.put("name", user.getName());
                        winner.put("avatar", user.getAvatar());
                        winner.put("department", user.getDepartment());
                    }

                    // 判断是否已评奖
                    List<Honor> honors = honorMapper.selectByUserIdAndMonth(userId, month);
                    winner.put("hasAwarded", !honors.isEmpty());

                    return winner;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("list", resultList);
        result.put("month", month);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object createHonor(AdminController.CreateHonorRequest request) {
        verifyAdmin();

        // 验证奖项是否存在
        Award award = awardMapper.selectById(request.getAwardId());
        if (award == null) {
            throw new BusinessException("奖项不存在");
        }

        // 从获奖时间中提取年份
        String year = request.getAwardDate().substring(0, 4);

        // 创建荣誉记录
        Honor honor = new Honor();
        honor.setUserId(request.getUserId());
        honor.setAwardId(request.getAwardId());
        honor.setAwardName(award.getName());
        honor.setAwardDate(request.getAwardDate());
        honor.setYear(year);
        honor.setCategory(request.getCategory());
        honor.setFlowers(0);
        honor.setCreateTime(LocalDateTime.now());
        honorMapper.insert(honor);

        // 发送获奖通知
        User user = userMapper.selectById(request.getUserId());
        if (user != null) {
            // TODO: 创建消息通知
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", honor.getId());
        result.put("message", "获奖记录创建成功");
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHonor(Long id) {
        verifyAdmin();
        honorMapper.deleteById(id);
    }

    @Override
    public Object uploadImage(MultipartFile file) {
        verifyAdmin();
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png|gif|webp)$")) {
            throw new BusinessException("只支持图片文件（jpg、jpeg、png、gif、webp）");
        }

        // 生成文件名
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        String filePath = uploadPath + "/" + fileName;

        try {
            // 创建目录
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            File destFile = new File(filePath);
            file.transferTo(destFile);

            // 返回文件URL（这里简化处理，实际应该返回完整的访问URL）
            String url = "/uploads/" + fileName;
            return Map.of("url", url);
        } catch (IOException e) {
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 验证管理员权限
     */
    private void verifyAdmin() {
        Long userId = getCurrentUserId();
        List<String> roles = userRoleMapper.selectRolesByUserId(userId);
        if (roles == null || !roles.contains("admin")) {
            throw new BusinessException(403, "无权限访问管理后台");
        }
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new BusinessException(401, "未授权，请先登录");
    }
}
