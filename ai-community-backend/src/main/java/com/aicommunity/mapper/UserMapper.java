package com.aicommunity.mapper;

import com.aicommunity.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface UserMapper {

    /**
     * 根据工号查询用户
     *
     * @param employeeId 工号
     * @return 用户
     */
    User selectByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    User selectById(@Param("id") Long id);

    /**
     * 插入用户
     *
     * @param user 用户
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户
     *
     * @param user 用户
     * @return 影响行数
     */
    int update(User user);

    /**
     * 根据姓名查询用户
     *
     * @param name 姓名
     * @return 用户
     */
    User selectByName(@Param("name") String name);

    /**
     * 搜索用户
     *
     * @param employeeId 工号
     * @param name       姓名
     * @param email      邮箱
     * @param role       角色
     * @return 用户列表
     */
    List<Map<String, Object>> searchUsers(
            @Param("employeeId") String employeeId,
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") String role);

    /**
     * 根据条件查询用户列表
     *
     * @param role   角色
     * @param toolId 工具ID
     * @param search 搜索关键词
     * @return 用户列表
     */
    List<Map<String, Object>> selectUsersByCondition(
            @Param("role") String role,
            @Param("toolId") Long toolId,
            @Param("search") String search);
}
