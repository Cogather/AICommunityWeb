package com.aicommunity.mapper;

import com.aicommunity.entity.LikeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 点赞记录Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface LikeRecordMapper {

    /**
     * 查询用户是否已点赞
     *
     * @param userId     用户ID
     * @param targetType 目标类型
     * @param targetId   目标ID
     * @return 点赞记录
     */
    LikeRecord selectByUserAndTarget(
            @Param("userId") Long userId,
            @Param("targetType") String targetType,
            @Param("targetId") Long targetId);

    /**
     * 插入点赞记录
     *
     * @param likeRecord 点赞记录
     * @return 影响行数
     */
    int insert(LikeRecord likeRecord);

    /**
     * 删除点赞记录
     *
     * @param userId     用户ID
     * @param targetType 目标类型
     * @param targetId   目标ID
     * @return 影响行数
     */
    int delete(
            @Param("userId") Long userId,
            @Param("targetType") String targetType,
            @Param("targetId") Long targetId);
}
