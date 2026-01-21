package com.aicommunity.service.impl;

import com.aicommunity.mapper.ZoneTagMapper;
import com.aicommunity.service.ZoneTagService;
import com.aicommunity.vo.ZoneTagListVO;
import com.aicommunity.vo.ZoneTagVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专区标签服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class ZoneTagServiceImpl implements ZoneTagService {

    @Autowired
    private ZoneTagMapper zoneTagMapper;

    @Override
    public ZoneTagListVO getZoneTags(String zone, Integer toolId) {
        // 转换专区名称为ID
        Integer zoneId = getZoneId(zone);
        if (zoneId == null) {
            throw new IllegalArgumentException("无效的专区类型: " + zone);
        }

        // 查询标签列表
        List<ZoneTagVO> tags = zoneTagMapper.selectZoneTags(zoneId, toolId);

        ZoneTagListVO result = new ZoneTagListVO();
        result.setList(tags);
        return result;
    }

    /**
     * 获取专区ID
     */
    private Integer getZoneId(String zone) {
        if ("practices".equals(zone)) {
            return 1;
        } else if ("tools".equals(zone)) {
            return 3;
        } else if ("agent".equals(zone)) {
            return 5;
        } else if ("empowerment".equals(zone)) {
            return 4;
        }
        return null;
    }
}
