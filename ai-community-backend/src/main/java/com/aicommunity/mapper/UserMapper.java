package com.aicommunity.mapper;

import com.aicommunity.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface UserMapper {
    /**
     * 根据工号查询用户
     */
    User selectByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据姓名查询用户
     */
    User selectByName(@Param("name") String name);

    /**
     * 更新用户
     */
    void updateById(User user);

    /**
     * 搜索用户
     */
    List<User> searchUsers(@Param("employeeId") String employeeId,
                          @Param("name") String name,
                          @Param("email") String email,
                          @Param("role") String role);

    /**
     * 查询用户列表
     */
    List<User> selectUsers(@Param("role") String role,
                          @Param("toolId") Long toolId,
                          @Param("search") String search);
}
