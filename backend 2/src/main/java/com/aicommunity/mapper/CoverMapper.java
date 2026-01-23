package com.aicommunity.mapper;

import com.aicommunity.vo.CoverVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 封面Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface CoverMapper {

    /**
     * 查询推荐封面列表
     *
     * @param zone  专区类型（可选）
     * @param count 返回数量
     * @return 封面列表
     */
    List<CoverVO> selectRecommendedCovers(@Param("zone") String zone, @Param("count") Integer count);
}
