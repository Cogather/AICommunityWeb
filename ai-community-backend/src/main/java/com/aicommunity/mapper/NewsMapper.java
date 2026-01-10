package com.aicommunity.mapper;

import com.aicommunity.dto.HonorHomeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 社区头条Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface NewsMapper {
    /**
     * 查询所有头条
     */
    List<com.aicommunity.dto.NewsResponse.NewsItem> selectAll();

    /**
     * 查询Top N头条
     */
    List<HonorHomeResponse.NewsItem> selectTop(@Param("limit") Integer limit);

    /**
     * 删除所有头条
     */
    void deleteAll();

    /**
     * 插入头条
     */
    void insert(com.aicommunity.dto.NewsResponse.NewsItem item);
}
