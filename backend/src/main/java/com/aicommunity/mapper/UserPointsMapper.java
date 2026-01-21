package com.aicommunity.mapper;

import com.aicommunity.vo.PointsHistoryVO;
import com.aicommunity.vo.PointsRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户积分Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface UserPointsMapper {

    /**
     * 查询用户积分
     *
     * @param userId 用户ID
     * @return 积分值，如果不存在返回0
     */
    Integer selectUserPoints(@Param("userId") String userId);

    /**
     * 查询用户积分历史记录（分页）
     *
     * @param userId 用户ID
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 积分历史记录列表
     */
    List<PointsHistoryVO> selectPointsHistory(@Param("userId") String userId,
                                               @Param("offset") Integer offset,
                                               @Param("pageSize") Integer pageSize);

    /**
     * 统计用户积分历史记录总数
     *
     * @param userId 用户ID
     * @return 记录总数
     */
    Long countPointsHistory(@Param("userId") String userId);

    /**
     * 查询积分规则列表
     *
     * @return 积分规则列表
     */
    List<PointsRuleVO> selectPointsRules();
}
