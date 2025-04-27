package com.ddukbbegi.common.auth;

import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    // 화이트리스트에 포함될 URL 경로들
    // 동적으로 바뀌는 경우 -> .+ 이런식 표기
    // 비회원일 때 접근 가능한 경로 추가
    private static final String[] DYNAMIC_WHITELIST_URLS = {
            "/api/stores/.+/menus"
    };

    private static final List<String> WHITELIST_URLS = List.of(
            "/oauth2/", "/login/oauth2/", "/oauth2/success"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (url.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (WHITELIST_URLS.stream().anyMatch(url::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isDynamicWhitelisted(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null) {
            sendErrorResponse(response, ResultCode.TOKEN_INVALID);
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
     * 동적 경로 패턴 체크
     */
    private boolean isDynamicWhitelisted(String url) {
        for (String dynamicUrl : DYNAMIC_WHITELIST_URLS) {
            Pattern pattern = Pattern.compile(dynamicUrl);
            if (pattern.matcher(url).matches()) {
                return true;
            }
        }
        return false;
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
