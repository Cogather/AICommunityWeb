package com.aicommunity.mapper;

import com.aicommunity.dto.HonorInfluenceResponse;
import com.aicommunity.entity.Honor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 荣誉Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface HonorMapper {
    /**
     * 根据ID查询荣誉
     */
    Honor selectById(@Param("id") Long id);

    /**
     * 查询荣誉列表
     */
    List<Honor> selectHonors(@Param("scope") String scope,
                            @Param("view") String view,
                            @Param("user") String user,
                            @Param("filterType") String filterType,
                            @Param("filterValue") String filterValue,
                            @Param("search") String search,
                            @Param("currentUserId") Long currentUserId);

    /**
     * 插入荣誉
     */
    Long insert(com.aicommunity.dto.HonorCreateRequest request);

    /**
     * 删除荣誉
     */
    void deleteById(@Param("id") Long id);

    /**
     * 查询Top用户
     */
    List<Honor.TopUser> selectTopUsers(@Param("limit") Integer limit);

    /**
     * 统计总荣誉数
     */
    Integer countTotalHonors();

    /**
     * 统计获奖用户总数
     */
    Integer countTotalUsers();

    /**
     * 统计总花朵数
     */
    Integer countTotalFlowers();

    /**
     * 统计用户花朵数
     */
    Integer countFlowersByUserId(@Param("userId") Long userId);

    /**
     * 查询分类统计
     */
    List<HonorInfluenceResponse.CategoryStats> selectCategoryStats();

    /**
     * 查询Top部门
     */
    List<HonorInfluenceResponse.DepartmentStats> selectTopDepartments(@Param("limit") Integer limit);

    /**
     * 检查用户是否已送花
     */
    boolean existsFlowerByUserAndHonor(@Param("userId") Long userId, @Param("honorId") Long honorId);

    /**
     * 插入送花记录
     */
    void insertFlower(@Param("userId") Long userId, @Param("honorId") Long honorId);

    /**
     * 增加花朵数
     */
    void incrementFlowers(@Param("id") Long id);
}
