package com.ddukbbegi.common.config;

import com.ddukbbegi.api.user.enums.UserRole;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private static final Long ACCESS_TOKEN = 60 * 1000 * 5L;  // 5분
    private static final Long REFRESH_TOKEN = 60 * 1000 * 10L; // 10 분


    /**
     * Access Token 발행
     *
     * @param userId
     * @param email
     * @param userRole
     * @return
     */
    public String generateAccessToken(Long userId, String email, UserRole userRole) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("userRole", userRole)
                        .setExpiration(createExpireDate(ACCESS_TOKEN))
                        .signWith(signatureAlgorithm, createSigningKey(secretKey))
                        .compact();
    }

    /**
     * Refresh Token 발행
     *
     * @param userId
     * @return
     */
    public String generateRefreshToken(Long userId) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .setExpiration(createExpireDate(REFRESH_TOKEN))
                        .signWith(signatureAlgorithm, createSigningKey(secretKey))
                        .compact();
    }

    /**
     * Token Valid 검증
     *
     * @param token
     * @return
     */
    public boolean isValidToken(String token) {
        if (token == null || token.isEmpty()) {
            log.info("Token is Null or Empty");
            return false;
        }
        try {
            Claims claims = getClaimsFromToken(token);
            log.info("Valid Token");
            return true;
        } catch (ExpiredJwtException exception) {
            log.info("Token Expired UserID: {}", exception.getClaims().getSubject());
            return false;
        } catch (JwtException exception) {
            log.warn("Token Tampered");
            return false;
        }
    }

    /**
     * 토큰 정보 추출
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createSigningKey(secretKey))
                .build()
                .parseClaimsJws(token).getBody();
    }

    /**
     * JWT 토큰의 만료 시간 생성
     *
     * @param expireDate : 시간 연산에 사용되는 밀리초 값 = long
     * @return Date
     */
    public Date createExpireDate(Long expireDate) {
        return new Date(System.currentTimeMillis() + expireDate);
    }

    /**
     * JWT 서명을 위한 비밀 키 생성
     *
     * @param key
     * @return Key
     */
    private Key createSigningKey(String key) {
        return new SecretKeySpec(Base64.getDecoder().decode(key), SignatureAlgorithm.HS256.getJcaName());
    }

}
