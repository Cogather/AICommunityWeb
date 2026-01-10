package com.aicommunity.service.impl;

import com.aicommunity.dto.RecommendedWinnersResponse;
import com.aicommunity.mapper.PointsMapper;
import com.aicommunity.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 积分服务实现类
 *
 * @author AI Community Team
 */
@Service
public class PointsServiceImpl implements PointsService {

    @Autowired
    private PointsMapper pointsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPoints(Long userId, Integer points, String type, Long targetId, String targetType) {
        // 获取当前月份
        String month = new SimpleDateFormat("yyyy-MM").format(new Date());

        // 插入积分记录
        pointsMapper.insert(userId, points, type, targetId, targetType, month);
    }

    @Override
    public Integer calculateUserPoints(Long userId) {
        return pointsMapper.calculateUserPoints(userId);
    }

    @Override
    public Integer calculatePointsByType(Long userId, String type) {
        return pointsMapper.calculatePointsByType(userId, type);
    }

    @Override
    public Integer calculateMonthlyPoints(Long userId, String month) {
        return pointsMapper.calculateMonthlyPoints(userId, month);
    }

    @Override
    public List<RecommendedWinnersResponse.WinnerItem> getMonthlyTopUsers(String month, Integer limit) {
        return pointsMapper.selectMonthlyTopUsers(month, limit);
    }
}
