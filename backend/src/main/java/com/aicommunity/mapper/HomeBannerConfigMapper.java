package com.aicommunity.mapper;

import com.aicommunity.entity.HomeBannerConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页Banner配置Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface HomeBannerConfigMapper {

    /**
     * 查询所有Banner配置，按order排序
     *
     * @return Banner配置列表
     */
    List<HomeBannerConfig> selectAllOrderByOrder();

    /**
     * 删除所有Banner配置
     *
     * @return 影响行数
     */
    int deleteAll();

    /**
     * 插入Banner配置
     *
     * @param config Banner配置
     * @return 影响行数
     */
    int insert(HomeBannerConfig config);
}
