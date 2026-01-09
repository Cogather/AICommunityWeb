package com.aicommunity.mapper;

import com.aicommunity.entity.Honor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 荣誉Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface HonorMapper {

    /**
     * 根据条件查询荣誉列表
     *
     * @param scope      范围：all-全部，mine-我的
     * @param view       视图：grid-荣誉墙，timeline-时光轴
     * @param user       用户名（查看特定用户的时光轴）
     * @param filterType 筛选类型
     * @param filterValue 筛选值
     * @param search     搜索关键词
     * @param userId     当前用户ID
     * @return 荣誉列表
     */
    List<Map<String, Object>> selectByCondition(
            @Param("scope") String scope,
            @Param("view") String view,
            @Param("user") String user,
            @Param("filterType") String filterType,
            @Param("filterValue") String filterValue,
            @Param("search") String search,
            @Param("userId") Long userId);

    /**
     * 根据ID查询荣誉
     *
     * @param id 荣誉ID
     * @return 荣誉
     */
    Honor selectById(@Param("id") Long id);

    /**
     * 插入荣誉
     *
     * @param honor 荣誉
     * @return 影响行数
     */
    int insert(Honor honor);

    /**
     * 删除荣誉
     *
     * @param id 荣誉ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新花朵数
     *
     * @param id    荣誉ID
     * @param delta 变化量
     * @return 影响行数
     */
    int updateFlowers(@Param("id") Long id, @Param("delta") Integer delta);

    /**
     * 统计用户总花朵数
     *
     * @param userId 用户ID
     * @return 总花朵数
     */
    Integer sumFlowersByUserId(@Param("userId") Long userId);

    /**
     * 查询Top用户（根据荣誉数量）
     *
     * @param limit 返回数量
     * @return 用户列表
     */
    List<Map<String, Object>> selectTopUsers(@Param("limit") Integer limit);

    /**
     * 根据用户ID和月份查询荣誉
     *
     * @param userId 用户ID
     * @param month  月份
     * @return 荣誉列表
     */
    List<Honor> selectByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);
}
