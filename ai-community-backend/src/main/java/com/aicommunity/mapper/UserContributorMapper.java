package com.aicommunity.mapper;

import com.aicommunity.dto.TopContributorResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户贡献者Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface UserContributorMapper {
    /**
     * 查询热门贡献者
     */
    List<TopContributorResponse> selectTopContributors(@Param("zone") String zone, @Param("limit") Integer limit);
}
