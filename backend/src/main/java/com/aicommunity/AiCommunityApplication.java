package com.aicommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI社区平台启动类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@SpringBootApplication
@MapperScan("com.aicommunity.mapper")
public class AiCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCommunityApplication.class, args);
    }
}
