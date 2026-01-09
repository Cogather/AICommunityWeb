package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.controller.AdminController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理后台服务接口
 *
 * @author AI Community Team
 */
public interface AdminService {

    /**
     * 获取首页轮播图配置
     */
    Object getCarousel();

    /**
     * 保存首页轮播图配置
     */
    void saveCarousel(AdminController.SaveCarouselRequest request);

    /**
     * 获取荣誉殿堂Banner配置
     */
    Object getHonorBanner();

    /**
     * 保存荣誉殿堂Banner配置
     */
    void saveHonorBanner(AdminController.SaveHonorBannerRequest request);

    /**
     * 获取荣誉殿堂奖项列表配置
     */
    Object getHonorAwards();

    /**
     * 保存荣誉殿堂奖项列表配置
     */
    void saveHonorAwards(AdminController.SaveHonorAwardsRequest request);

    /**
     * 获取社区头条配置
     */
    Object getNews();

    /**
     * 保存社区头条配置
     */
    void saveNews(AdminController.SaveNewsRequest request);

    /**
     * 获取AI工具配置
     */
    Object getTools();

    /**
     * 保存AI工具配置
     */
    void saveTools(AdminController.SaveToolsRequest request);

    /**
     * 获取扶摇Agent应用置顶帖子配置
     */
    Object getFeaturedPost();

    /**
     * 保存扶摇Agent应用置顶帖子配置
     */
    void saveFeaturedPost(AdminController.SaveFeaturedPostRequest request);

    /**
     * 获取推荐封面配置
     */
    Object getRecommendedCovers();

    /**
     * 保存推荐封面配置
     */
    void saveRecommendedCovers(AdminController.SaveRecommendedCoversRequest request);

    /**
     * 搜索用户
     */
    Object searchUsers(String employeeId, String name, String email, String role);

    /**
     * 获取用户列表
     */
    PageResult<?> getUsers(String role, Long toolId, String search, PageQuery pageQuery);

    /**
     * 添加用户角色
     */
    void addUserRole(Long userId, AdminController.AddUserRoleRequest request);

    /**
     * 移除用户角色
     */
    void removeUserRole(Long userId, AdminController.RemoveUserRoleRequest request);

    /**
     * 获取本月积分排行榜
     */
    Object getRecommendedWinners(String month, Integer limit);

    /**
     * 设置用户获奖
     */
    Object createHonor(AdminController.CreateHonorRequest request);

    /**
     * 取消用户获奖
     */
    void deleteHonor(Long id);

    /**
     * 上传图片
     */
    Object uploadImage(MultipartFile file);
}
