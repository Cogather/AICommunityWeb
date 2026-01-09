package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 登录响应DTO
 *
 * @author AI Community Team
 */
@Data
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Token
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfo user;

    /**
     * 用户信息内部类
     */
    @Data
    public static class UserInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String name;
        private String avatar;
        private String department;
        private String role;
    }
}
