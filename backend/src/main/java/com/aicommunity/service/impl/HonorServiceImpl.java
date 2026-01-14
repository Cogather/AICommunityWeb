package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.entity.Honor;
import com.aicommunity.entity.HonorFlower;
import com.aicommunity.mapper.HonorMapper;
import com.aicommunity.service.HonorService;
import com.aicommunity.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 荣誉服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class HonorServiceImpl implements HonorService {

    @Autowired
    private HonorMapper honorMapper;

    private static final String DEFAULT_PAGE_SIZE = "16";
    private static final String HONOR_TYPE_INDIVIDUAL = "0";

    @Override
    public HonorListVO getHonorList(PageQuery pageQuery, String scope, String filterType,
                                    String filterValue, String keyword, String view,
                                    String userName, String currentUserId) {
        // 设置分页参数
        Integer page = pageQuery.getPage() != null && pageQuery.getPage() > 0 ? pageQuery.getPage() : 1;
        Integer pageSize = pageQuery.getPageSize() != null && pageQuery.getPageSize() > 0 
                ? pageQuery.getPageSize() : Integer.parseInt(DEFAULT_PAGE_SIZE);

        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("scope", StringUtils.hasText(scope) ? scope : "all");
        params.put("filterType", filterType);
        params.put("filterValue", filterValue);
        params.put("keyword", keyword);
        params.put("view", StringUtils.hasText(view) ? view : "grid");
        params.put("userName", userName);
        params.put("currentUserId", currentUserId);
        params.put("honorType", HONOR_TYPE_INDIVIDUAL); // 个人荣誉

        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<Map<String, Object>> honorList = honorMapper.selectHonorList(params);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(honorList);

        // 转换为VO
        List<HonorItemVO> voList = honorList.stream().map(this::convertToHonorItemVO).collect(Collectors.toList());

        // 构建返回结果
        HonorListVO result = new HonorListVO();
        result.setList(voList);
        result.setTotal(pageInfo.getTotal());
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setTotalPages(pageInfo.getPages());

        return result;
    }

    @Override
    public TeamAwardListVO getTeamAwards(String year, String currentUserId) {
        // 查询团队奖项
        List<Map<String, Object>> teamAwards = honorMapper.selectTeamAwards(year);

        // 查询年份列表
        List<String> years = honorMapper.selectTeamAwardYears();

        // 转换为VO
        List<TeamAwardVO> voList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> groupedByHonorId = teamAwards.stream()
                .collect(Collectors.groupingBy(item -> (String) item.get("honorId")));

        for (Map.Entry<String, List<Map<String, Object>>> entry : groupedByHonorId.entrySet()) {
            List<Map<String, Object>> details = entry.getValue();
            if (CollectionUtils.isEmpty(details)) {
                continue;
            }

            Map<String, Object> firstDetail = details.get(0);
            TeamAwardVO awardVO = new TeamAwardVO();
            awardVO.setId(Integer.parseInt(firstDetail.get("honorId").toString()));
            awardVO.setTitle((String) firstDetail.get("honorName"));
            awardVO.setYear((String) firstDetail.get("gainedYear"));

            // 转换图片列表
            List<TeamAwardImageVO> images = details.stream().map(detail -> {
                TeamAwardImageVO imageVO = new TeamAwardImageVO();
                imageVO.setId((Integer) detail.get("id"));
                imageVO.setImage((String) detail.get("image"));
                imageVO.setImageType("url");
                imageVO.setWinnerName((String) detail.get("teamName"));
                imageVO.setTeamField((String) detail.get("teamField"));

                // 查询花朵数和是否已送花
                String honorId = (String) detail.get("honorId");
                Integer flowers = honorMapper.countFlowersByHonorId(honorId);
                imageVO.setFlowers(flowers != null ? flowers : 0);

                if (StringUtils.hasText(currentUserId)) {
                    HonorFlower flower = honorMapper.selectFlowerByHonorIdAndUserId(honorId, currentUserId);
                    imageVO.setHasGivenFlower(flower != null);
                } else {
                    imageVO.setHasGivenFlower(false);
                }

                return imageVO;
            }).collect(Collectors.toList());

            awardVO.setImages(images);
            voList.add(awardVO);
        }

        TeamAwardListVO result = new TeamAwardListVO();
        result.setList(voList);
        result.setYears(years != null ? years : new ArrayList<>());

        return result;
    }

    @Override
    public LeaderboardVO getLeaderboard(Integer limit, String scope, String filterType,
                                        String filterValue, String currentUserId) {
        // 设置默认值
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("scope", StringUtils.hasText(scope) ? scope : "all");
        params.put("filterType", filterType);
        params.put("filterValue", filterValue);
        params.put("currentUserId", currentUserId);

        // 查询排行榜
        List<Map<String, Object>> leaderboard = honorMapper.selectLeaderboard(params);

        // 转换为VO
        List<LeaderboardItemVO> voList = leaderboard.stream().map(item -> {
            LeaderboardItemVO vo = new LeaderboardItemVO();
            vo.setName((String) item.get("name"));
            vo.setDepartment((String) item.get("department"));
            vo.setAvatar((String) item.get("avatar"));
            vo.setCount(((Number) item.get("count")).intValue());
            vo.setTotalFlowers(((Number) item.get("totalFlowers")).intValue());
            return vo;
        }).collect(Collectors.toList());

        LeaderboardVO result = new LeaderboardVO();
        result.setList(voList);

        return result;
    }

    @Override
    public FlowerResponseVO giveFlower(Integer honorId, String type, String currentUserId) {
        if (honorId == null) {
            throw new BusinessException(400, "荣誉记录ID不能为空");
        }

        if (!StringUtils.hasText(currentUserId)) {
            throw new BusinessException(401, "请先登录");
        }

        // 根据type确定honorId（个人荣誉和团队荣誉都使用detail的id）
        // 注意：送花表存储的是honor_id，不是detail的id
        // 前端传入的honorId是detail表的id，需要查询获取honor_id
        String honorIdStr = honorMapper.selectHonorIdByDetailId(honorId);
        if (honorIdStr == null || honorIdStr.isEmpty()) {
            throw new BusinessException(404, "荣誉记录不存在");
        }

        // 检查是否已送花（注意：送花表存储的是honor_id，需要根据honor_id和用户ID查询）
        HonorFlower existingFlower = honorMapper.selectFlowerByHonorIdAndUserId(honorIdStr, currentUserId);
        if (existingFlower != null) {
            throw new BusinessException(400, "已送过花");
        }

        // 插入送花记录
        HonorFlower flower = new HonorFlower();
        flower.setHonorId(honorIdStr);
        flower.setFlowersUserId(currentUserId);
        int insertCount = honorMapper.insertFlower(flower);
        if (insertCount <= 0) {
            throw new BusinessException(500, "送花失败");
        }

        // 查询送花后的总花朵数
        Integer totalFlowers = honorMapper.countFlowersByHonorId(honorIdStr);

        FlowerResponseVO result = new FlowerResponseVO();
        result.setFlowers(totalFlowers != null ? totalFlowers : 1);
        result.setHasGivenFlower(true);

        return result;
    }

    @Override
    public AwardListVO getAwardNames() {
        List<String> awardNames = honorMapper.selectAwardNames();
        if (CollectionUtils.isEmpty(awardNames)) {
            awardNames = new ArrayList<>();
        }
        // 添加"全部"选项
        if (!awardNames.contains("全部")) {
            awardNames.add(0, "全部");
        }

        AwardListVO result = new AwardListVO();
        result.setList(awardNames);
        return result;
    }

    @Override
    public AwardListVO getDepartments() {
        List<String> departments = honorMapper.selectDepartments();
        if (CollectionUtils.isEmpty(departments)) {
            departments = new ArrayList<>();
        }
        // 添加"全部"选项
        if (!departments.contains("全部")) {
            departments.add(0, "全部");
        }

        AwardListVO result = new AwardListVO();
        result.setList(departments);
        return result;
    }

    @Override
    public TimelineVO getTimeline(String userName, String currentUserId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("currentUserId", currentUserId);
        params.put("honorType", HONOR_TYPE_INDIVIDUAL); // 个人荣誉

        // 查询时光轴数据
        List<Map<String, Object>> timelineData = honorMapper.selectTimeline(params);

        // 查询用户信息
        TimelineUserVO userVO = null;
        if (StringUtils.hasText(userName)) {
            Map<String, Object> userInfo = honorMapper.selectUserInfoByName(userName);
            if (userInfo != null) {
                userVO = new TimelineUserVO();
                userVO.setName((String) userInfo.get("name"));
                userVO.setAvatar((String) userInfo.get("avatar"));
                userVO.setDepartment((String) userInfo.get("department"));
                String userId = (String) userInfo.get("userId");
                if (StringUtils.hasText(userId)) {
                    Integer totalFlowers = honorMapper.countUserTotalFlowers(userId);
                    userVO.setTotalFlowers(totalFlowers != null ? totalFlowers : 0);
                } else {
                    userVO.setTotalFlowers(0);
                }
            }
        }

        // 按年份分组
        Map<String, List<TimelineItemVO>> groupedByYear = timelineData.stream()
                .map(this::convertToTimelineItemVO)
                .collect(Collectors.groupingBy(item -> {
                    String date = item.getAwardDate();
                    if (date != null && date.length() >= 4) {
                        return date.substring(0, 4);
                    }
                    return date;
                }, LinkedHashMap::new, Collectors.toList()));

        // 转换为年度VO列表
        List<TimelineYearVO> yearList = new ArrayList<>();
        for (Map.Entry<String, List<TimelineItemVO>> entry : groupedByYear.entrySet()) {
            TimelineYearVO yearVO = new TimelineYearVO();
            // 从日期中提取年份
            String year = entry.getKey();
            if (year != null && year.length() >= 4) {
                year = year.substring(0, 4);
            }
            yearVO.setYear(year);
            yearVO.setItems(entry.getValue());
            yearList.add(yearVO);
        }

        TimelineVO result = new TimelineVO();
        result.setUser(userVO);
        result.setTimeline(yearList);

        return result;
    }

    /**
     * 转换为荣誉项VO
     *
     * @param map 数据库查询结果
     * @return 荣誉项VO
     */
    private HonorItemVO convertToHonorItemVO(Map<String, Object> map) {
        HonorItemVO vo = new HonorItemVO();
        vo.setId((Integer) map.get("id"));
        vo.setName((String) map.get("name"));
        vo.setDepartment((String) map.get("department"));
        vo.setAvatar((String) map.get("avatar"));
        vo.setAwardName((String) map.get("awardName"));
        vo.setAwardDate((String) map.get("awardDate"));
        vo.setCategory((String) map.get("category"));
        vo.setYear((String) map.get("year"));
        vo.setIsMine((Boolean) map.get("isMine"));
        vo.setFlowers((Integer) map.get("flowers"));
        vo.setHasGivenFlower((Boolean) map.get("hasGivenFlower"));
        vo.setAchievement((String) map.get("achievement"));
        return vo;
    }

    /**
     * 转换为时光轴项VO
     *
     * @param map 数据库查询结果
     * @return 时光轴项VO
     */
    private TimelineItemVO convertToTimelineItemVO(Map<String, Object> map) {
        TimelineItemVO vo = new TimelineItemVO();
        vo.setId((Integer) map.get("id"));
        vo.setName((String) map.get("name"));
        vo.setAvatar((String) map.get("avatar"));
        vo.setAwardName((String) map.get("awardName"));
        vo.setAwardDate((String) map.get("awardDate"));
        vo.setCategory((String) map.get("category"));
        return vo;
    }

    @Override
    public AwardSaveRequestVO saveAward(AwardSaveRequestVO request) {
        if (request == null || !StringUtils.hasText(request.getName())) {
            throw new BusinessException(400, "奖项名称不能为空");
        }

        Honor honor = new Honor();
        Date now = new Date();
        
        if (request.getId() != null) {
            // 更新
            String honorId = String.valueOf(request.getId());
            honor.setHonorId(honorId);
            honor.setHonorName(request.getName());
            honor.setHonorDesc(request.getDescription());
            honor.setStatus("0");
            honor.setUpdateAt(now);

            // 检查奖项是否存在
            Honor existingHonor = null;
            try {
                List<Honor> honors = honorMapper.selectAllHonorsWithId();
                for (Honor h : honors) {
                    if (h.getHonorId().equals(honorId)) {
                        existingHonor = h;
                        break;
                    }
                }
            } catch (Exception e) {
                log.warn("查询现有奖项失败", e);
            }

            if (existingHonor != null) {
                int updateCount = honorMapper.updateHonor(honor);
                if (updateCount <= 0) {
                    throw new BusinessException(500, "更新奖项失败");
                }
            } else {
                throw new BusinessException(404, "奖项不存在");
            }
        } else {
            // 新增：使用时间戳生成ID
            String honorId = String.valueOf(System.currentTimeMillis());
            honor.setHonorId(honorId);
            honor.setHonorName(request.getName());
            honor.setHonorDesc(request.getDescription());
            honor.setStatus("0");
            honor.setUpdateAt(now);

            int insertCount = honorMapper.insertHonor(honor);
            if (insertCount <= 0) {
                throw new BusinessException(500, "新增奖项失败");
            }
            // 返回新插入的ID（转换为Integer，前端可能需要）
            try {
                long longId = Long.parseLong(honorId);
                // 如果ID太大，使用hashCode
                if (longId > Integer.MAX_VALUE) {
                    request.setId(honorId.hashCode());
                } else {
                    request.setId((int) longId);
                }
            } catch (NumberFormatException e) {
                // 如果ID不是数字格式，使用hashCode
                request.setId(honorId.hashCode());
            }
        }

        return request;
    }

    @Override
    public void deleteAward(Integer id) {
        if (id == null) {
            throw new BusinessException(400, "奖项ID不能为空");
        }

        String honorId = String.valueOf(id);

        // 检查该奖项是否已有获奖者
        Integer winnerCount = honorMapper.countWinnersByHonorId(honorId);
        if (winnerCount != null && winnerCount > 0) {
            throw new BusinessException(400, "该奖项已有获奖者，无法删除");
        }

        // 删除奖项（逻辑删除，设置status为'1'）
        int deleteCount = honorMapper.deleteHonor(honorId);
        if (deleteCount <= 0) {
            throw new BusinessException(404, "奖项不存在或删除失败");
        }
    }

    @Override
    public LatestWinnerListVO getLatestWinners(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 9;
        }

        List<Map<String, Object>> winners = honorMapper.selectLatestWinners(limit);
        if (CollectionUtils.isEmpty(winners)) {
            LatestWinnerListVO result = new LatestWinnerListVO();
            result.setList(new ArrayList<>());
            return result;
        }

        List<LatestWinnerVO> voList = winners.stream().map(winner -> {
            LatestWinnerVO vo = new LatestWinnerVO();
            vo.setId((Integer) winner.get("id"));
            vo.setName((String) winner.get("name"));
            vo.setAvatar((String) winner.get("avatar"));
            vo.setAwardName((String) winner.get("awardName"));
            return vo;
        }).collect(Collectors.toList());

        LatestWinnerListVO result = new LatestWinnerListVO();
        result.setList(voList);
        return result;
    }
}
