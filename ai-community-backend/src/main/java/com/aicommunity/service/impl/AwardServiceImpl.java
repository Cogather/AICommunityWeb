package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.entity.Award;
import com.aicommunity.mapper.AwardMapper;
import com.aicommunity.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 奖项服务实现类
 *
 * @author AI Community Team
 */
@Service
public class AwardServiceImpl implements AwardService {

    @Autowired
    private AwardMapper awardMapper;

    @Override
    public Object getAwards(String category) {
        List<Award> awards = awardMapper.selectByCategory(category);
        List<Map<String, Object>> list = awards.stream()
                .map(award -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", award.getId());
                    map.put("name", award.getName());
                    map.put("desc", award.getDesc());
                    map.put("image", award.getImage());
                    map.put("category", award.getCategory());
                    map.put("rules", award.getRules());
                    return map;
                })
                .collect(Collectors.toList());
        return Map.of("list", list);
    }

    @Override
    public Object getAwardRules(Long id) {
        Award award = awardMapper.selectById(id);
        if (award == null) {
            throw new BusinessException("奖项不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", award.getId());
        result.put("name", award.getName());
        result.put("rules", award.getRules());
        result.put("category", award.getCategory());
        result.put("image", award.getImage());
        result.put("desc", award.getDesc());

        return result;
    }
}
