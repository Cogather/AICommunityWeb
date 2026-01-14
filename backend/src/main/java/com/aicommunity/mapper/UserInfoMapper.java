package com.aicommunity.mapper;

import com.aicommunity.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface UserInfoMapper {

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfo selectByUserId(@Param("userId") String userId);

    /**
     * 根据工号查询用户信息
     *
     * @param userName 工号
     * @return 用户信息
     */
    UserInfo selectByUserName(@Param("userName") String userName);

    /**
     * 更新用户信息（头像和简介）
     *
     * @param userId 用户ID
     * @param avatar 头像URL
     * @param bio    个人简介
     * @return 更新行数
     */
    int updateUserProfile(@Param("userId") String userId,
                          @Param("avatar") String avatar,
                          @Param("bio") String bio);

    /**
     * 统计用户发布的帖子数
     *
     * @param userId 用户ID
     * @return 帖子数
     */
    Integer countUserPosts(@Param("userId") String userId);

    /**
     * 统计用户收藏数
     *
     * @param userId 用户ID
     * @return 收藏数
     */
    Integer countUserFavorites(@Param("userId") String userId);

    /**
     * 统计用户评论数
     *
     * @param userId 用户ID
     * @return 评论数
     */
    Integer countUserComments(@Param("userId") String userId);

    /**
     * 统计用户参与的活动数
     *
     * @param userId 用户ID
     * @return 活动数
     */
    Integer countUserActivities(@Param("userId") String userId);

    /**
     * 统计用户获得的小红花数
     *
     * @param userId 用户ID
     * @return 小红花数
     */
    Integer countUserFlowers(@Param("userId") String userId);

    /**
     * 查询用户列表（支持关键词搜索和角色筛选）
     *
     * @param keyword 搜索关键词（姓名、邮箱）
     * @param role    角色筛选：admin/tool_owner
     * @return 用户列表
     */
    List<UserInfo> selectUsers(@Param("keyword") String keyword, @Param("role") String role);

    /**
     * 根据用户ID查询用户信息（数字ID）
     *
     * @param userId 用户ID（数字）
     * @return 用户信息
     */
    UserInfo selectByUserIdInt(@Param("userId") Integer userId);

    /**
     * 更新用户角色
     *
     * @param userId 用户ID
     * @param role   角色
     * @return 影响行数
     */
    int updateUserRole(@Param("userId") String userId, @Param("role") String role);
}
