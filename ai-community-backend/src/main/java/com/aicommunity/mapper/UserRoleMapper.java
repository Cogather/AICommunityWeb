package com.aicommunity.mapper;

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
     */
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询拥有的工具列表
     */
    List<OwnedTool> selectOwnedToolsByUserId(@Param("userId") Long userId);

    /**
     * 插入用户角色
     */
    void insert(@Param("userId") Long userId, 
                @Param("role") String role, 
                @Param("toolId") Long toolId);

    /**
     * 删除用户角色
     */
    void delete(@Param("userId") Long userId, 
                @Param("role") String role, 
                @Param("toolId") Long toolId);

    /**
     * 检查用户是否为指定工具的Owner
     */
    boolean existsByUserAndToolAndRole(@Param("userId") Long userId, 
                                      @Param("toolId") Long toolId, 
                                      @Param("role") String role);

    /**
     * 拥有的工具
     */
    class OwnedTool {
        private Long toolId;
        private String toolName;

        public Long getToolId() {
            return toolId;
        }

        public void setToolId(Long toolId) {
            this.toolId = toolId;
        }

        public String getToolName() {
            return toolName;
        }

        public void setToolName(String toolName) {
            this.toolName = toolName;
        }
    }
}
