package com.aicommunity.mapper;

import com.aicommunity.entity.Award;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 奖项Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface AwardMapper {
    /**
     * 根据ID查询奖项
     */
    Award selectById(@Param("id") Long id);

    /**
     * 查询所有奖项
     */
    List<Award> selectAll();

    /**
     * 根据分类查询奖项
     */
    List<Award> selectByCategory(@Param("category") String category);
}
