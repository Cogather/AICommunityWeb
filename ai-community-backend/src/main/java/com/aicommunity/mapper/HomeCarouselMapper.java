package com.aicommunity.mapper;

import com.aicommunity.dto.CarouselResponse;
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
     */
    List<CarouselResponse.CarouselItem> selectAll();

    /**
     * 删除所有轮播图
     */
    void deleteAll();

    /**
     * 插入轮播图
     */
    void insert(CarouselResponse.CarouselItem item);
}
