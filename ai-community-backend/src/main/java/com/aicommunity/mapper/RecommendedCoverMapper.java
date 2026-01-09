package com.aicommunity.mapper;

import com.aicommunity.entity.RecommendedCover;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 推荐封面Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface RecommendedCoverMapper {

    /**
     * 查询所有推荐封面
     *
     * @return 推荐封面列表
     */
    List<RecommendedCover> selectAll();

    /**
     * 删除所有推荐封面
     *
     * @return 影响行数
     */
    int deleteAll();

    /**
     * 插入推荐封面
     *
     * @param cover 推荐封面
     * @return 影响行数
     */
    int insert(RecommendedCover cover);
}
