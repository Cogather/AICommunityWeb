package com.aicommunity.mapper;

import com.aicommunity.entity.Draft;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 草稿Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface DraftMapper {

    /**
     * 根据用户ID查询草稿
     *
     * @param userId 用户ID
     * @return 草稿信息
     */
    Draft selectByUserId(@Param("userId") String userId);

    /**
     * 插入草稿
     *
     * @param draft 草稿信息
     * @return 影响行数
     */
    int insertDraft(Draft draft);

    /**
     * 更新草稿
     *
     * @param draft 草稿信息
     * @return 影响行数
     */
    int updateDraft(Draft draft);

    /**
     * 删除草稿
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteDraft(@Param("userId") String userId);
}
