package com.aicommunity.service.impl;

import com.aicommunity.mapper.CoverMapper;
import com.aicommunity.service.CoverService;
import com.aicommunity.vo.CoverListVO;
import com.aicommunity.vo.CoverVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 封面服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class CoverServiceImpl implements CoverService {

    @Autowired
    private CoverMapper coverMapper;

    @Override
    public CoverListVO getRecommendedCovers(String zone, Integer count) {
        if (count == null || count < 1) {
            count = 3;
        }

        List<CoverVO> covers = coverMapper.selectRecommendedCovers(zone, count);

        CoverListVO result = new CoverListVO();
        result.setList(covers);
        return result;
    }
}
