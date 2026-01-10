package com.aicommunity.mapper;

import com.aicommunity.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface MessageMapper {
    /**
     * 根据用户ID和类型查询消息列表
     */
    List<Message> selectByUserId(@Param("userId") Long userId, @Param("type") String type);

    /**
     * 更新消息已读状态
     */
    void updateReadStatus(@Param("id") Long id, @Param("isRead") Boolean isRead);

    /**
     * 更新用户所有消息已读状态
     */
    void updateAllReadStatus(@Param("userId") Long userId, @Param("isRead") Boolean isRead);

    /**
     * 删除消息
     */
    void deleteById(@Param("id") Long id);

    /**
     * 统计未读消息数
     */
    Integer countUnreadByUserId(@Param("userId") Long userId);
}
