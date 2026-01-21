package com.aicommunity.service;

import com.aicommunity.vo.*;

import java.util.List;

/**
 * 管理后台服务接口
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
public interface AdminService {

    // ========== 首页管理 ==========

    /**
     * 获取轮播图配置
     *
     * @return 轮播图列表
     */
    AdminCarouselListVO getCarouselConfig();

    /**
     * 保存轮播图配置
     *
     * @param list 轮播图列表
     */
    void saveCarouselConfig(List<CarouselItemVO> list);

    /**
     * 获取荣誉殿堂Banner配置
     *
     * @return Banner配置
     */
    AdminHonorBannerVO getHonorBannerConfig();

    /**
     * 保存荣誉殿堂Banner配置
     *
     * @param bannerImage Banner图片URL
     */
    void saveHonorBannerConfig(String bannerImage);

    /**
     * 获取AI工具配置
     *
     * @return 工具列表
     */
    AdminToolListVO getToolsConfig();

    /**
     * 保存AI工具配置
     *
     * @param list 工具列表
     */
    void saveToolsConfig(List<ToolItemVO> list);

    /**
     * 获取AI工具专区Banner配置
     *
     * @return Banner列表
     */
    AdminToolBannerListVO getToolBannersConfig();

    /**
     * 保存AI工具专区Banner配置
     *
     * @param list Banner列表
     */
    void saveToolBannersConfig(List<ToolBannerItemVO> list);

    // ========== 精华/置顶帖子管理 ==========

    /**
     * 获取所有精华/置顶帖子列表
     *
     * @return 精华/置顶帖子列表（按区域分组）
     */
    AdminFeaturedPostListVO getAllFeaturedPosts();

    /**
     * 移除帖子精华/置顶状态
     *
     * @param postId 帖子ID
     */
    void removeFeaturedPost(String postId);

    /**
     * 获取赋能交流精选合集配置
     *
     * @return 精选合集列表
     */
    AdminCollectionListVO getEmpowermentCollections();

    /**
     * 保存赋能交流精选合集配置
     *
     * @param list 精选合集列表
     */
    void saveEmpowermentCollections(List<AdminCollectionItemVO> list);

    // ========== AI使用达人管理 ==========

    /**
     * 获取奖项设置列表
     *
     * @return 奖项列表
     */
    AdminAwardListVO getAwards();

    /**
     * 保存奖项设置列表
     *
     * @param list 奖项列表
     */
    void saveAwards(List<AdminAwardItemVO> list);

    /**
     * 获取奖项名称列表（下拉选择用）
     *
     * @return 奖项名称列表
     */
    AdminAwardNameListVO getAwardNames();

    /**
     * 获取获奖者列表
     *
     * @return 获奖者列表
     */
    AdminWinnerListVO getWinners();

    /**
     * 保存获奖者列表
     *
     * @param list 获奖者列表
     */
    void saveWinners(List<AdminWinnerItemVO> list);

    /**
     * 获取获奖者推荐列表
     *
     * @param month 月份，格式YYYY-MM，默认当前月
     * @return 推荐获奖者列表
     */
    AdminRecommendedWinnerListVO getRecommendedWinners(String month);

    /**
     * 设置用户获奖
     *
     * @param request 设置获奖请求
     * @return 设置结果（包含荣誉ID）
     */
    AdminSetAwardResponseVO setUserAward(AdminSetAwardRequestVO request);

    /**
     * 取消用户获奖
     *
     * @param request 取消获奖请求
     */
    void cancelUserAward(AdminCancelAwardRequestVO request);

    /**
     * 获取团队奖项列表
     *
     * @return 团队奖项列表
     */
    AdminTeamAwardListVO getTeamAwards();

    /**
     * 保存团队奖项列表
     *
     * @param list 团队奖项列表
     */
    void saveTeamAwards(List<TeamAwardVO> list);

    // ========== 人员管理 ==========

    /**
     * 获取用户列表
     *
     * @param keyword 搜索关键词（姓名、邮箱）
     * @param role    角色筛选：admin/tool_owner
     * @return 用户列表
     */
    AdminUserListVO getUsers(String keyword, String role);

    /**
     * 根据用户ID查找用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    AdminUserItemVO getUserById(Integer userId);

    /**
     * 添加管理员/工具Owner
     *
     * @param request 用户角色请求
     */
    void addUserRole(AdminUserRoleRequestVO request);

    /**
     * 移除管理员/工具Owner
     *
     * @param request 用户角色请求
     */
    void removeUserRole(AdminUserRoleRequestVO request);

    // ========== 图片上传 ==========

    /**
     * 上传图片
     *
     * @param fileBytes 文件字节数组
     * @param fileName  文件名
     * @return 图片URL
     */
    String uploadImage(byte[] fileBytes, String fileName);
}
