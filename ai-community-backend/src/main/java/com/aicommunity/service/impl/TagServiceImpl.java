package com.aicommunity.service.impl;

import com.aicommunity.mapper.PostTagMapper;
import com.aicommunity.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标签服务实现类
 *
 * @author AI Community Team
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private PostTagMapper postTagMapper;

    @Override
    public Object getTags(String zone, Long toolId) {
        // 根据专区和工具获取标签列表
        List<String> tags = postTagMapper.selectTagsByZoneAndTool(zone, toolId);

        // 如果是AI工具专区且指定了具体工具（toolId不为-1），返回固定标签
        if ("tools".equals(zone) && toolId != null && toolId != -1) {
            List<Map<String, Object>> fixedTags = new ArrayList<>();
            Map<String, Object> tag1 = new HashMap<>();
            tag1.put("name", "操作指导");
            tag1.put("count", 0);
            tag1.put("preset", true);
            fixedTags.add(tag1);

            Map<String, Object> tag2 = new HashMap<>();
            tag2.put("name", "优秀使用");
            tag2.put("count", 0);
            tag2.put("preset", true);
            fixedTags.add(tag2);

            return Map.of("list", fixedTags);
        }

        // 其他情况返回实际使用的标签
        List<Map<String, Object>> tagList = tags.stream()
                .map(tagName -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", tagName);
                    // TODO: 统计该标签下的帖子数量
                    map.put("count", 0);
                    map.put("preset", false);
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of("list", tagList);
    }
}
