package com.aicommunity.mapper;

import com.aicommunity.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface MessageMapper {

    /**
     * 分页查询用户消息列表
     *
     * @param userId   用户ID
     * @param type     消息类型（可选）
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 消息列表
     */
    List<Message> selectMessagesByUserId(@Param("userId") String userId,
                                         @Param("type") String type,
                                         @Param("offset") Integer offset,
                                         @Param("pageSize") Integer pageSize);

    /**
     * 统计用户消息总数
     *
     * @param userId 用户ID
     * @param type   消息类型（可选）
     * @return 消息总数
     */
    Long countMessagesByUserId(@Param("userId") String userId, @Param("type") String type);

    /**
     * 统计用户未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Long countUnreadMessages(@Param("userId") String userId);

    /**
     * 根据ID查询消息
     *
     * @param id 消息ID
     * @return 消息信息
     */
    Message selectById(@Param("id") Long id);

    /**
     * 插入消息
     *
     * @param message 消息信息
     * @return 影响行数
     */
    int insertMessage(Message message);

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
    int markAllAsRead(@Param("userId") String userId);

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 影响行数
     */
    int deleteMessage(@Param("id") Long id);
}
