package com.ddukbbegi.common.auth;

import com.ddukbbegi.api.user.entity.CustomOAuth2User;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Social Login 성공 시 실행되는 메서드
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) oAuth2User;

        // Token 발행
        String accessToken = jwtUtil.generateAccessToken(customOAuth2User.getId(), customOAuth2User.getEmail(), UserRole.USER);
        String refreshToken = jwtUtil.generateRefreshToken(customOAuth2User.getId());
        System.out.println(customOAuth2User.getId());

        // 4. RefreshToken 저장
        redisTemplate.opsForValue().set(
                "userId:" + customOAuth2User.getId(),
                "refreshToken:" + refreshToken,
                Duration.ofDays(7));

        // Json 형태로 AccessToken 과 RefreshToken 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("{\"accessToken\":\"" + accessToken + "\", \"refreshToken\":\"" + refreshToken + "\"}");
    }
}
