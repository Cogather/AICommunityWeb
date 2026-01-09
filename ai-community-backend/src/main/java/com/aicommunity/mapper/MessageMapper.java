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
     * 根据条件查询消息列表
     *
     * @param userId 用户ID
     * @param type   消息类型
     * @return 消息列表
     */
    List<Message> selectByCondition(@Param("userId") Long userId, @Param("type") String type);

    /**
     * 根据ID查询消息
     *
     * @param id 消息ID
     * @return 消息
     */
    Message selectById(@Param("id") Long id);

    /**
     * 插入消息
     *
     * @param message 消息
     * @return 影响行数
     */
    int insert(Message message);

    /**
     * 更新消息
     *
     * @param message 消息
     * @return 影响行数
     */
    int update(Message message);

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 标记消息为已读
     *
     * @param id 消息ID
     * @return 影响行数
     */
    int markAsRead(@Param("id") Long id);

    /**
     * 标记用户所有消息为已读
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int markAllAsRead(@Param("userId") Long userId);

    /**
     * 统计未读消息数
     *
     * @param userId 用户ID
     * @return 未读消息数
     */
    int countUnread(@Param("userId") Long userId);
}
