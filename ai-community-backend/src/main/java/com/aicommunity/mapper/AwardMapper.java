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
     * 查询奖项列表
     *
     * @param category 奖项分类
     * @return 奖项列表
     */
    List<Award> selectByCategory(@Param("category") String category);

    /**
     * 根据ID查询奖项
     *
     * @param id 奖项ID
     * @return 奖项
     */
    Award selectById(@Param("id") Long id);
}
