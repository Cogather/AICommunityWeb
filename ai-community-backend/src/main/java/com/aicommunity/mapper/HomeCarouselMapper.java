package com.aicommunity.mapper;

import com.aicommunity.entity.HomeCarousel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 首页轮播图Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface HomeCarouselMapper {

    /**
     * 查询所有轮播图
     *
     * @return 轮播图列表
     */
    List<HomeCarousel> selectAll();

    /**
     * 删除所有轮播图
     *
     * @return 影响行数
     */
    int deleteAll();

    /**
     * 插入轮播图
     *
     * @param carousel 轮播图
     * @return 影响行数
     */
    int insert(HomeCarousel carousel);
}
