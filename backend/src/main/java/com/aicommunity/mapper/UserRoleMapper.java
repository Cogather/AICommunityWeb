package com.aicommunity.mapper;

import com.aicommunity.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface UserRoleMapper {

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和角色查询
     *
     * @param userId 用户ID
     * @param role   角色
     * @return 角色列表
     */
    List<UserRole> selectByUserIdAndRole(@Param("userId") Long userId, @Param("role") String role);

    /**
     * 插入用户角色
     *
     * @param userRole 用户角色
     * @return 影响行数
     */
    int insert(UserRole userRole);

    /**
     * 删除用户角色
     *
     * @param userId 用户ID
     * @param role   角色
     * @param toolId 工具ID（当role为owner时必填）
     * @return 影响行数
     */
    int delete(@Param("userId") Long userId, @Param("role") String role, @Param("toolId") Long toolId);

    /**
     * 根据用户ID和角色查询
     *
     * @param userId 用户ID
     * @param role   角色
     * @return 角色列表
     */
    List<UserRole> selectByUserIdAndRole(@Param("userId") Long userId, @Param("role") String role);
}
