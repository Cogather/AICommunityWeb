package com.aicommunity.mapper;

import com.aicommunity.dto.HonorAwardsResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 荣誉配置Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface HonorConfigMapper {
    /**
     * 查询Banner图片
     */
    String selectBannerImage();

    /**
     * 更新Banner图片
     */
    void updateBannerImage(String bannerImage);

    /**
     * 查询奖项列表
     */
    List<HonorAwardsResponse.AwardItem> selectAwards();

    /**
     * 删除所有奖项配置
     */
    void deleteAllAwards();

    /**
     * 插入奖项配置
     */
    void insertAward(HonorAwardsResponse.AwardItem item);
}
