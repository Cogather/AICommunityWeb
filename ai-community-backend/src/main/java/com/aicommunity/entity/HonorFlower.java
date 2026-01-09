package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 荣誉送花记录实体类
 *
 * @author AI Community Team
 */
@Data
public class HonorFlower implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 荣誉ID
     */
    private Long honorId;

    /**
     * 送花用户ID
     */
    private Long userId;

    /**
     * 送花时间
     */
    private LocalDateTime createTime;
}
