package com.aicommunity.mapper;

import com.aicommunity.dto.RecommendedCoversResponse;
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
     */
    List<RecommendedCoversResponse.CoverItem> selectAll();

    /**
     * 删除所有推荐封面
     */
    void deleteAll();

    /**
     * 插入推荐封面
     */
    void insert(RecommendedCoversResponse.CoverItem item);
}
