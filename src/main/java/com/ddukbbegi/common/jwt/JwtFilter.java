package com.ddukbbegi.common.jwt;

import com.ddukbbegi.common.component.ResultCode;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.extractToken(bearerJwt);

        if (!jwtUtil.isValidToken(token)) {
            sendErrorResponse(response, ResultCode.TOKEN_INVALID);
            return;
        }

        if (isTokenBlacklisted(token)) {
            sendErrorResponse(response, ResultCode.TOKEN_BLACKLISTED);
            return;
        }

        Authentication auth = jwtUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    /**
     * AccessToken 이 Blacklist 에 있는지 검증
     *
     * @param accessToken
     * @return boolean
     */
    private boolean isTokenBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:" + accessToken);
    }

    /**
     * Filter Error
     */
    public void sendErrorResponse(HttpServletResponse response, ResultCode resultCode) throws IOException {
        response.setStatus(resultCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonErrorResponse = "{"
                + "\"data\": null,"
                + "\"result\": {"
                + "\"status\": " + resultCode.getStatus().value() + ","
                + "\"code\": \"" + resultCode.getCode() + "\","
                + "\"message\": \"" + resultCode.getDefaultMessage() + "\","
                + "\"timestamp\": \"" + LocalDateTime.now() + "\""
                + "}"
                + "}";

        response.getWriter().write(jsonErrorResponse);
    }

}
