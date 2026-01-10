package com.aicommunity.config;

import com.aicommunity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 *
 * @author AI Community Team
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        
        if (token != null && jwtUtil.validateToken(token)) {
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            // 将用户信息存储到ThreadLocal中
            com.aicommunity.util.UserContext.setUserId(userId);
            com.aicommunity.util.UserContext.setUsername(username);
        }
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 清除ThreadLocal，防止内存泄漏
            com.aicommunity.util.UserContext.clear();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中提取Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_NAME);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
