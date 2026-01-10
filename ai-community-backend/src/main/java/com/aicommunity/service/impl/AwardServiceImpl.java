package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.dto.AwardRulesResponse;
import com.aicommunity.entity.Award;
import com.aicommunity.mapper.AwardMapper;
import com.aicommunity.service.AwardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Award> getAwards(String category) {
        return awardMapper.selectByCategory(category);
    }

    @Override
    public AwardRulesResponse getAwardRules(Long id) {
        Award award = awardMapper.selectById(id);
        if (award == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND, "奖项不存在");
        }

        AwardRulesResponse response = new AwardRulesResponse();
        BeanUtils.copyProperties(award, response);
        return response;
    }
}
