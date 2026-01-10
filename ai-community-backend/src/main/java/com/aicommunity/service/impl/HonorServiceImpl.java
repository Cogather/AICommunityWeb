package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.dto.HonorFlowerResponse;
import com.aicommunity.dto.HonorInfluenceResponse;
import com.aicommunity.entity.Honor;
import com.aicommunity.mapper.HonorMapper;
import com.aicommunity.service.HonorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 荣誉服务实现类
 *
 * @author AI Community Team
 */
@Service
public class HonorServiceImpl implements HonorService {

    @Autowired
    private HonorMapper honorMapper;

    @Override
    public PageResult<Honor> getHonors(String scope, String view, String user, String filterType,
                                       String filterValue, String search, PageQuery pageQuery) {
        Long currentUserId = getCurrentUserId();
        
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());
        List<Honor> honors = honorMapper.selectHonors(scope, view, user, filterType, filterValue, search, currentUserId);
        
        // 填充用户信息
        for (Honor honor : honors) {
            fillHonorInfo(honor, currentUserId);
        }
        
        PageInfo<Honor> pageInfo = new PageInfo<>(honors);
        return PageResult.of(honors, pageInfo.getTotal(), pageQuery.getPage(), pageQuery.getPageSize());
    }

    @Override
    public HonorInfluenceResponse getHonorInfluence() {
        HonorInfluenceResponse response = new HonorInfluenceResponse();
        response.setTotalHonors(honorMapper.countTotalHonors());
        response.setTotalUsers(honorMapper.countTotalUsers());
        response.setTotalFlowers(honorMapper.countTotalFlowers());
        response.setCategories(honorMapper.selectCategoryStats());
        response.setTopDepartments(honorMapper.selectTopDepartments(10));
        return response;
    }

    @Override
    public PageResult<Honor.TopUser> getTopUsers(Integer limit) {
        List<Honor.TopUser> topUsers = honorMapper.selectTopUsers(limit);
        return PageResult.of(topUsers, (long) topUsers.size(), 1, limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HonorFlowerResponse giveFlower(Long id) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        Honor honor = honorMapper.selectById(id);
        if (honor == null) {
            throw new BusinessException(ErrorCodeEnum.HONOR_NOT_FOUND);
        }

        boolean exists = honorMapper.existsFlowerByUserAndHonor(currentUserId, id);
        HonorFlowerResponse response = new HonorFlowerResponse();

        if (!exists) {
            honorMapper.insertFlower(currentUserId, id);
            honorMapper.incrementFlowers(id);
            response.setHasGivenFlower(true);
        } else {
            response.setHasGivenFlower(false);
        }

        Honor updatedHonor = honorMapper.selectById(id);
        response.setFlowers(updatedHonor.getFlowers());
        return response;
    }

    /**
     * 填充荣誉信息
     */
    private void fillHonorInfo(Honor honor, Long currentUserId) {
        // 填充用户信息
        if (honor.getUserId() != null) {
            com.aicommunity.entity.User user = userMapper.selectById(honor.getUserId());
            if (user != null) {
                honor.setName(user.getName());
                honor.setDepartment(user.getDepartment());
                honor.setAvatar(user.getAvatar());
            }
        }

        // 检查是否已送花
        if (currentUserId != null) {
            honor.setHasGivenFlower(honorMapper.existsFlowerByUserAndHonor(currentUserId, honor.getId()));
            honor.setIsMine(honor.getUserId().equals(currentUserId));
        }
    }

    @Autowired
    private com.aicommunity.mapper.UserMapper userMapper;

    private Long getCurrentUserId() {
        return com.aicommunity.util.UserContext.getUserId();
    }
}
