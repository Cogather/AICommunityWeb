package com.aicommunity.service;

import com.aicommunity.common.PageQuery;
import com.aicommunity.vo.*;

/**
 * 荣誉服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface HonorService {

    /**
     * 获取个人荣誉列表
     *
     * @param pageQuery     分页参数
     * @param scope         筛选范围：all（全员）/mine（我的），默认all
     * @param filterType    筛选类型：award（按奖项）/department（按部门）
     * @param filterValue   筛选值（奖项名称或部门名称）
     * @param keyword       搜索关键词（匹配用户姓名）
     * @param view          视图模式：grid（荣誉墙）/timeline（时光轴）
     * @param userName      时光轴模式下，指定用户姓名查看其荣誉历程
     * @param currentUserId 当前登录用户ID（用于判断isMine和hasGivenFlower）
     * @return 荣誉列表
     */
    HonorListVO getHonorList(PageQuery pageQuery, String scope, String filterType, 
                             String filterValue, String keyword, String view, 
                             String userName, String currentUserId);

    /**
     * 获取团队奖项列表
     *
     * @param year          按年份筛选
     * @param currentUserId 当前登录用户ID（用于判断hasGivenFlower）
     * @return 团队奖项列表
     */
    TeamAwardListVO getTeamAwards(String year, String currentUserId);

    /**
     * 获取荣誉影响力排行榜
     *
     * @param limit         返回数量，默认10
     * @param scope         筛选范围：all（全员）/mine（我的），默认all
     * @param filterType    筛选类型：award（按奖项）/department（按部门）
     * @param filterValue   筛选值
     * @param currentUserId 当前登录用户ID（用于筛选mine）
     * @return 排行榜列表
     */
    LeaderboardVO getLeaderboard(Integer limit, String scope, String filterType, 
                                  String filterValue, String currentUserId);

    /**
     * 给荣誉记录送花
     *
     * @param honorId       荣誉记录ID
     * @param type          荣誉类型：individual（个人）/team（团队），默认individual
     * @param currentUserId  当前登录用户ID
     * @return 送花响应
     */
    FlowerResponseVO giveFlower(Integer honorId, String type, String currentUserId);

    /**
     * 获取所有奖项名称列表（用于筛选）
     *
     * @return 奖项名称列表
     */
    AwardListVO getAwardNames();

    /**
     * 获取所有部门列表（用于筛选）
     *
     * @return 部门列表
     */
    AwardListVO getDepartments();

    /**
     * 获取用户荣誉时光轴数据
     *
     * @param userName      用户姓名，不传则返回所有用户的时光轴
     * @param currentUserId 当前登录用户ID（用于判断hasGivenFlower）
     * @return 时光轴数据
     */
    TimelineVO getTimeline(String userName, String currentUserId);

    /**
     * 保存单个奖项（管理员操作）
     *
     * @param request 保存奖项请求
     * @return 保存结果
     */
    AwardSaveRequestVO saveAward(AwardSaveRequestVO request);

    /**
     * 删除奖项（管理员操作）
     *
     * @param id 奖项ID
     */
    void deleteAward(Integer id);

    /**
     * 获取最新获奖者列表（首页展示用）
     *
     * @param limit 返回数量，默认9
     * @return 最新获奖者列表
     */
    LatestWinnerListVO getLatestWinners(Integer limit);
}
