package com.aicommunity.mapper;

import com.aicommunity.entity.HomeNews;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 社区头条Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface HomeNewsMapper {

    /**
     * 查询所有头条
     *
     * @return 头条列表
     */
    List<HomeNews> selectAll();

    /**
     * 删除所有头条
     *
     * @return 影响行数
     */
    int deleteAll();

    /**
     * 插入头条
     *
     * @param news 头条
     * @return 影响行数
     */
    int insert(HomeNews news);
}
