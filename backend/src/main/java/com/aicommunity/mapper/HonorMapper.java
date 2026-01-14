package com.aicommunity.mapper;

import com.aicommunity.entity.Honor;
import com.aicommunity.entity.HonorBanner;
import com.aicommunity.entity.HonorFlower;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 荣誉Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface HonorMapper {

    /**
     * 查询荣誉Banner（取第一个）
     *
     * @return Banner对象
     */
    HonorBanner selectFirstBanner();

    /**
     * 查询所有荣誉奖项（状态为正常）
     *
     * @return 荣誉列表
     */
    List<Honor> selectAllHonors();

    /**
     * 查询Top用户（根据荣誉数量排序，取前6名）
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    List<com.aicommunity.entity.User> selectTopUsers(@Param("limit") Integer limit);

    /**
     * 查询个人荣誉列表（分页）
     *
     * @param params 查询参数
     * @return 荣誉详情列表
     */
    List<Map<String, Object>> selectHonorList(@Param("params") Map<String, Object> params);

    /**
     * 统计个人荣誉总数
     *
     * @param params 查询参数
     * @return 总数
     */
    Long countHonorList(@Param("params") Map<String, Object> params);

    /**
     * 查询团队奖项列表
     *
     * @param year 年份
     * @return 团队奖项列表
     */
    List<Map<String, Object>> selectTeamAwards(@Param("year") String year);

    /**
     * 查询团队奖项的年份列表
     *
     * @return 年份列表
     */
    List<String> selectTeamAwardYears();

    /**
     * 查询荣誉影响力排行榜
     *
     * @param params 查询参数
     * @return 排行榜列表
     */
    List<Map<String, Object>> selectLeaderboard(@Param("params") Map<String, Object> params);

    /**
     * 查询用户是否已给荣誉送花
     *
     * @param honorId      荣誉ID
     * @param flowersUserId 送花用户ID
     * @return 送花记录
     */
    HonorFlower selectFlowerByHonorIdAndUserId(@Param("honorId") String honorId, 
                                                @Param("flowersUserId") String flowersUserId);

    /**
     * 插入送花记录
     *
     * @param honorFlower 送花记录
     * @return 影响行数
     */
    int insertFlower(HonorFlower honorFlower);

    /**
     * 统计荣誉的花朵数
     *
     * @param honorId 荣誉ID
     * @return 花朵数
     */
    Integer countFlowersByHonorId(@Param("honorId") String honorId);

    /**
     * 查询所有奖项名称列表
     *
     * @return 奖项名称列表
     */
    List<String> selectAwardNames();

    /**
     * 查询所有部门列表
     *
     * @return 部门列表
     */
    List<String> selectDepartments();

    /**
     * 查询用户荣誉时光轴数据
     *
     * @param params 查询参数
     * @return 时光轴数据
     */
    List<Map<String, Object>> selectTimeline(@Param("params") Map<String, Object> params);

    /**
     * 查询用户信息（用于时光轴）
     *
     * @param userName 用户姓名
     * @return 用户信息
     */
    Map<String, Object> selectUserInfoByName(@Param("userName") String userName);

    /**
     * 统计用户累计花朵数
     *
     * @param userId 用户ID
     * @return 累计花朵数
     */
    Integer countUserTotalFlowers(@Param("userId") String userId);

    /**
     * 根据detail id查询honor_id
     *
     * @param detailId detail的id
     * @return honor_id
     */
    String selectHonorIdByDetailId(@Param("detailId") Integer detailId);

    /**
     * 查询最新获奖者列表（用于首页AI使用达人展示）
     *
     * @param limit 返回数量，默认9
     * @return 最新获奖者列表
     */
    List<Map<String, Object>> selectLatestWinners(@Param("limit") Integer limit);

    /**
     * 更新荣誉Banner配置
     *
     * @param banner Banner配置
     * @return 影响行数
     */
    int updateBanner(HonorBanner banner);

    /**
     * 插入荣誉Banner配置
     *
     * @param banner Banner配置
     * @return 影响行数
     */
    int insertBanner(HonorBanner banner);

    /**
     * 查询所有荣誉奖项（包含ID和名称）
     *
     * @return 荣誉列表（包含honor_id和honor_name）
     */
    List<Honor> selectAllHonorsWithId();

    /**
     * 插入荣誉奖项
     *
     * @param honor 荣誉奖项
     * @return 影响行数
     */
    int insertHonor(Honor honor);

    /**
     * 更新荣誉奖项
     *
     * @param honor 荣誉奖项
     * @return 影响行数
     */
    int updateHonor(Honor honor);

    /**
     * 删除荣誉奖项
     *
     * @param honorId 荣誉ID
     * @return 影响行数
     */
    int deleteHonor(@Param("honorId") String honorId);

    /**
     * 查询获奖者列表（个人奖项）
     *
     * @return 获奖者列表
     */
    List<Map<String, Object>> selectWinners();

    /**
     * 插入获奖者记录
     *
     * @param detail 荣誉详情
     * @return 影响行数
     */
    int insertHonorDetail(com.aicommunity.entity.NewHonorDetail detail);

    /**
     * 删除获奖者记录
     *
     * @param id 记录ID
     * @return 影响行数
     */
    int deleteHonorDetail(@Param("id") Integer id);

    /**
     * 查询推荐获奖者（根据积分）
     *
     * @param month 月份（YYYY-MM格式）
     * @param limit 限制数量
     * @return 推荐获奖者列表
     */
    List<Map<String, Object>> selectRecommendedWinners(@Param("month") String month, @Param("limit") Integer limit);

    /**
     * 检查指定奖项是否有获奖者
     *
     * @param honorId 荣誉ID
     * @return 获奖者数量
     */
    Integer countWinnersByHonorId(@Param("honorId") String honorId);

    /**
     * 查询团队奖项列表（所有年份）
     *
     * @return 团队奖项列表
     */
    List<Map<String, Object>> selectAllTeamAwards();
}
