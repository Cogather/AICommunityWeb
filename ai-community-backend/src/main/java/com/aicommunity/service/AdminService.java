package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.*;
import com.aicommunity.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理后台服务接口
 *
 * @author AI Community Team
 */
public interface AdminService {
    // ==================== 首页管理 ====================
    CarouselResponse getCarouselConfig();
    void saveCarouselConfig(CarouselResponse request);
    HonorBannerResponse getHonorBannerConfig();
    void saveHonorBannerConfig(HonorBannerResponse request);
    HonorAwardsResponse getHonorAwardsConfig();
    void saveHonorAwardsConfig(HonorAwardsResponse request);
    NewsResponse getNewsConfig();
    void saveNewsConfig(NewsResponse request);

    // ==================== 工具管理 ====================
    ToolsConfigResponse getToolsConfig();
    void saveToolsConfig(ToolsConfigResponse request);

    // ==================== 扶摇Agent应用 ====================
    AgentFeaturedPostResponse getAgentFeaturedPostConfig();
    void saveAgentFeaturedPostConfig(AgentFeaturedPostResponse request);

    // ==================== 推荐封面 ====================
    RecommendedCoversResponse getRecommendedCoversConfig();
    void saveRecommendedCoversConfig(RecommendedCoversResponse request);

    // ==================== 人员管理 ====================
    List<User> searchUsers(String employeeId, String name, String email, String role);
    PageResult<User> getUsers(String role, Long toolId, String search, PageQuery pageQuery);
    void addUserRole(Long userId, UserRoleRequest request);
    void removeUserRole(Long userId, UserRoleRequest request);

    // ==================== 获奖者推荐 ====================
    RecommendedWinnersResponse getRecommendedWinners(String month, Integer limit);
    HonorCreateResponse createHonor(HonorCreateRequest request);
    void deleteHonor(Long id);

    // ==================== 文件上传 ====================
    ImageUploadResponse uploadImage(MultipartFile file);
}
