package com.aicommunity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 点赞Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface LikeMapper {
    /**
     * 插入点赞记录
     */
    void insert(@Param("userId") Long userId, 
                @Param("targetType") String targetType, 
                @Param("targetId") Long targetId);

    /**
     * 删除点赞记录
     */
    void deleteByUserAndTarget(@Param("userId") Long userId, 
                               @Param("targetType") String targetType, 
                               @Param("targetId") Long targetId);

    /**
     * 检查是否存在点赞记录
     */
    boolean existsByUserAndTarget(@Param("userId") Long userId, 
                                  @Param("targetType") String targetType, 
                                  @Param("targetId") Long targetId);

    /**
     * 根据目标删除点赞记录
     */
    void deleteByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId);
}
