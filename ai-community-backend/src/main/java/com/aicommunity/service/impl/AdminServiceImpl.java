package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.*;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.*;
import com.aicommunity.service.AdminService;
import com.aicommunity.service.PointsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;

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
    private HonorConfigMapper honorConfigMapper;

    @Autowired
    private NewsMapper newsMapper;

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
    private HonorMapper honorMapper;

    @Autowired
    private PointsService pointsService;

    @Autowired
    private com.aicommunity.mapper.ToolBannerMapper toolBannerMapper;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public CarouselResponse getCarouselConfig() {
        CarouselResponse response = new CarouselResponse();
        List<CarouselResponse.CarouselItem> items = homeCarouselMapper.selectAll();
        response.setList(items);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCarouselConfig(CarouselResponse request) {
        // 删除旧数据
        homeCarouselMapper.deleteAll();
        // 插入新数据
        for (CarouselResponse.CarouselItem item : request.getList()) {
            homeCarouselMapper.insert(item);
        }
    }

    @Override
    public HonorBannerResponse getHonorBannerConfig() {
        HonorBannerResponse response = new HonorBannerResponse();
        String bannerImage = honorConfigMapper.selectBannerImage();
        response.setBannerImage(bannerImage);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveHonorBannerConfig(HonorBannerResponse request) {
        honorConfigMapper.updateBannerImage(request.getBannerImage());
    }

    @Override
    public HonorAwardsResponse getHonorAwardsConfig() {
        HonorAwardsResponse response = new HonorAwardsResponse();
        List<HonorAwardsResponse.AwardItem> items = honorConfigMapper.selectAwards();
        response.setList(items);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveHonorAwardsConfig(HonorAwardsResponse request) {
        // 删除旧数据
        honorConfigMapper.deleteAllAwards();
        // 插入新数据
        for (HonorAwardsResponse.AwardItem item : request.getList()) {
            honorConfigMapper.insertAward(item);
        }
    }

    @Override
    public NewsResponse getNewsConfig() {
        NewsResponse response = new NewsResponse();
        List<NewsResponse.NewsItem> items = newsMapper.selectAll();
        response.setList(items);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewsConfig(NewsResponse request) {
        // 删除旧数据
        newsMapper.deleteAll();
        // 插入新数据
        for (NewsResponse.NewsItem item : request.getList()) {
            newsMapper.insert(item);
        }
    }

    @Override
    public ToolsConfigResponse getToolsConfig() {
        ToolsConfigResponse response = new ToolsConfigResponse();
        List<ToolsConfigResponse.ToolItem> tools = toolMapper.selectAllWithBanners();
        response.setList(tools);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveToolsConfig(ToolsConfigResponse request) {
        // 更新工具信息
        for (ToolsConfigResponse.ToolItem toolItem : request.getList()) {
            com.aicommunity.entity.Tool tool = toolMapper.selectById(toolItem.getId());
            if (tool != null) {
                tool.setName(toolItem.getName());
                tool.setDesc(toolItem.getDesc());
                tool.setLogo(toolItem.getLogo());
                tool.setColor(toolItem.getColor());
                tool.setLink(toolItem.getLink());
                toolMapper.updateById(tool);
            }

            // 更新Banner
            if (toolItem.getBanners() != null) {
                toolBannerMapper.deleteByToolId(toolItem.getId());
                for (ToolsConfigResponse.BannerItem bannerItem : toolItem.getBanners()) {
                    // 通过反射或直接设置toolId
                    try {
                        java.lang.reflect.Field field = bannerItem.getClass().getDeclaredField("toolId");
                        field.setAccessible(true);
                        field.set(bannerItem, toolItem.getId());
                    } catch (Exception e) {
                        // 如果无法设置，则在XML中使用参数传递
                    }
                    toolBannerMapper.insert(bannerItem);
                }
            }
        }
    }

    @Override
    public AgentFeaturedPostResponse getAgentFeaturedPostConfig() {
        return agentFeaturedPostMapper.selectFeaturedPost();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAgentFeaturedPostConfig(AgentFeaturedPostResponse request) {
        agentFeaturedPostMapper.updateFeaturedPost(request);
    }

    @Override
    public RecommendedCoversResponse getRecommendedCoversConfig() {
        RecommendedCoversResponse response = new RecommendedCoversResponse();
        List<RecommendedCoversResponse.CoverItem> covers = recommendedCoverMapper.selectAll();
        response.setList(covers);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecommendedCoversConfig(RecommendedCoversResponse request) {
        // 删除旧数据
        recommendedCoverMapper.deleteAll();
        // 插入新数据
        for (RecommendedCoversResponse.CoverItem item : request.getList()) {
            recommendedCoverMapper.insert(item);
        }
    }

    @Override
    public List<User> searchUsers(String employeeId, String name, String email, String role) {
        return userMapper.searchUsers(employeeId, name, email, role);
    }

    @Override
    public PageResult<User> getUsers(String role, Long toolId, String search, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<User> users = userMapper.selectUsers(role, toolId, search);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return PageResult.of(users, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRole(Long userId, UserRoleRequest request) {
        userRoleMapper.insert(userId, request.getRole(), request.getToolId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserRole(Long userId, UserRoleRequest request) {
        userRoleMapper.delete(userId, request.getRole(), request.getToolId());
    }

    @Override
    public RecommendedWinnersResponse getRecommendedWinners(String month, Integer limit) {
        if (month == null || month.isEmpty()) {
            month = new SimpleDateFormat("yyyy-MM").format(new Date());
        }

        RecommendedWinnersResponse response = new RecommendedWinnersResponse();
        response.setMonth(month);
        
        List<RecommendedWinnersResponse.WinnerItem> winners = pointsService.getMonthlyTopUsers(month, limit);
        response.setList(winners);
        
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HonorCreateResponse createHonor(HonorCreateRequest request) {
        // 提取年份
        String year = request.getAwardDate().substring(0, 4);
        request.setYear(year);

        // 创建荣誉记录
        Long honorId = honorMapper.insert(request);

        HonorCreateResponse response = new HonorCreateResponse();
        response.setId(honorId);
        response.setMessage("获奖记录创建成功");
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHonor(Long id) {
        honorMapper.deleteById(id);
    }

    @Override
    public ImageUploadResponse uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST, "文件不能为空");
        }

        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                    : "";
            String filename = UUID.randomUUID().toString() + extension;

            // 创建上传目录
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 保存文件
            Path filePath = uploadDir.resolve(filename);
            Files.write(filePath, file.getBytes());

            // 返回文件URL
            String fileUrl = "/uploads/" + filename;
            ImageUploadResponse response = new ImageUploadResponse();
            response.setUrl(fileUrl);
            return response;

        } catch (IOException e) {
            throw new BusinessException(ErrorCodeEnum.INTERNAL_ERROR, "文件上传失败：" + e.getMessage());
        }
    }
}
