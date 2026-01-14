package com.aicommunity.mapper;

import com.aicommunity.entity.PostTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子标签Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface PostTagMapper {

    /**
     * 根据label_id查询标签信息
     *
     * @param labelId 标签ID
     * @return 标签信息
     */
    PostTag selectByLabelId(@Param("labelId") Integer labelId);
}
