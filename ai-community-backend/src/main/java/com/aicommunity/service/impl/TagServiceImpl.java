package com.aicommunity.service.impl;

import com.aicommunity.dto.TagInfo;
import com.aicommunity.entity.Tag;
import com.aicommunity.mapper.TagMapper;
import com.aicommunity.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签服务实现类
 *
 * @author AI Community Team
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagInfo> getTags(String zone, Long toolId) {
        List<Tag> tags = tagMapper.selectByZoneAndToolId(zone, toolId);
        return tags.stream()
                .map(tag -> {
                    TagInfo info = new TagInfo();
                    info.setName(tag.getName());
                    info.setPreset(tag.getPreset());
                    // TODO: 计算该标签下的帖子数量
                    info.setCount(0);
                    return info;
                })
                .collect(Collectors.toList());
    }
}
