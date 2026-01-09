package com.aicommunity.mapper;

import com.aicommunity.entity.HomeHonorConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 荣誉殿堂配置Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface HomeHonorConfigMapper {

    /**
     * 查询配置（只有一条记录）
     *
     * @return 配置
     */
    HomeHonorConfig selectOne();

    /**
     * 插入配置
     *
     * @param config 配置
     * @return 影响行数
     */
    int insert(HomeHonorConfig config);

    /**
     * 更新配置
     *
     * @param config 配置
     * @return 影响行数
     */
    int update(HomeHonorConfig config);
}
