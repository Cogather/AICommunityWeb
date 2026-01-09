package com.aicommunity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * CORS跨域配置
 *
 * @author AI Community Team
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 允许的源地址（从配置文件读取，多个用逗号分隔）
     * 如果未配置，则允许所有源（仅开发环境）
     */
    @Value("${cors.allowed-origins:}")
    private String allowedOrigins;

    /**
     * 是否允许所有源（开发环境使用）
     */
    @Value("${cors.allow-all:true}")
    private boolean allowAll;

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许的源（前端地址）
                .allowedOriginPatterns(getAllowedOriginPatterns())
                // 允许的HTTP方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                // 允许的请求头
                .allowedHeaders("*")
                // 暴露的响应头（前端可以访问的响应头）
                .exposedHeaders("Authorization", "Content-Type", "X-Total-Count")
                // 允许携带凭证（Cookie、Authorization等）
                .allowCredentials(true)
                // 预检请求的缓存时间（秒）
                .maxAge(3600);
    }

    /**
     * CORS过滤器配置（用于处理OPTIONS预检请求）
     *
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 设置允许的源
        if (allowAll || !StringUtils.hasText(allowedOrigins)) {
            // 开发环境：允许所有源
            config.addAllowedOriginPattern("*");
        } else {
            // 生产环境：只允许配置的源
            List<String> origins = Arrays.asList(allowedOrigins.split(","));
            origins.forEach(origin -> {
                String trimmedOrigin = origin.trim();
                if (StringUtils.hasText(trimmedOrigin)) {
                    config.addAllowedOrigin(trimmedOrigin);
                }
            });
        }

        // 允许所有HTTP方法
        config.addAllowedMethod("*");

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 暴露的响应头
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Content-Type");
        config.addExposedHeader("X-Total-Count");

        // 允许携带凭证
        config.setAllowCredentials(true);

        // 预检请求的缓存时间
        config.setMaxAge(3600L);

        // 对所有路径生效
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    /**
     * 获取允许的源地址模式
     *
     * @return 源地址数组
     */
    private String[] getAllowedOriginPatterns() {
        if (allowAll || !StringUtils.hasText(allowedOrigins)) {
            // 开发环境：允许所有源
            return new String[]{"*"};
        } else {
            // 生产环境：返回配置的源地址
            return Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .toArray(String[]::new);
        }
    }
}
