package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 消息DTO
 *
 * @author AI Community Team
 */
@Data
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Boolean read;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 发送用户名
     */
    private String fromUserName;

    /**
     * 跳转链接
     */
    private String link;
}
