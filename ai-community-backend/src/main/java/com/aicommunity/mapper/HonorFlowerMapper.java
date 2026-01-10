package com.aicommunity.mapper;

import com.aicommunity.entity.HonorFlower;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 荣誉送花记录Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface HonorFlowerMapper {

    /**
     * 查询用户是否已送花
     *
     * @param honorId 荣誉ID
     * @param userId  用户ID
     * @return 送花记录
     */
    HonorFlower selectByHonorAndUser(@Param("honorId") Long honorId, @Param("userId") Long userId);

    /**
     * 插入送花记录
     *
     * @param flower 送花记录
     * @return 影响行数
     */
    int insert(HonorFlower flower);
}
