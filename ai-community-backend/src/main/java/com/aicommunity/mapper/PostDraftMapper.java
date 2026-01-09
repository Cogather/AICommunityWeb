package com.aicommunity.mapper;

import com.aicommunity.entity.PostDraft;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子草稿Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface PostDraftMapper {

    /**
     * 根据用户ID查询草稿
     *
     * @param userId 用户ID
     * @return 草稿
     */
    PostDraft selectByUserId(@Param("userId") Long userId);

    /**
     * 插入草稿
     *
     * @param draft 草稿
     * @return 影响行数
     */
    int insert(PostDraft draft);

    /**
     * 更新草稿
     *
     * @param draft 草稿
     * @return 影响行数
     */
    int update(PostDraft draft);
}
