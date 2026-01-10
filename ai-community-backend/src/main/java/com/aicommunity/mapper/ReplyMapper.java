package com.aicommunity.mapper;

import com.aicommunity.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 回复Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface ReplyMapper {
    /**
     * 根据ID查询回复
     */
    Reply selectById(@Param("id") Long id);

    /**
     * 根据评论ID查询回复列表
     */
    List<Reply> selectByCommentId(@Param("commentId") Long commentId);

    /**
     * 插入回复
     */
    void insert(Reply reply);

    /**
     * 删除回复
     */
    void deleteById(@Param("id") Long id);

    /**
     * 根据评论ID删除回复
     */
    void deleteByCommentId(@Param("commentId") Long commentId);
}
