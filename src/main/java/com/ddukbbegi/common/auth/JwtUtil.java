package com.ddukbbegi.common.auth;

import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private static final String BEARER_PREFIX = "Bearer ";
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private static final Long ACCESS_TOKEN = 60 * 1000 * 5L;  // 5분
    private static final Long REFRESH_TOKEN = 60 * 1000 * 10L; // 10 분

    public JwtUtil(UserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }


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
     * @return String : refreshToken
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
        if (!StringUtils.hasText(token)) {
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
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            return false;
        } catch (JwtException exception) {
            log.warn("Token Tampered");
            return false;
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
            return false;
        }
    }

    /**
     * 토큰 정보 Extract
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
     * 토큰에서 userId Extract
     *
     * @param token
     * @return
     */
    public Long getUserIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(createSigningKey(secretKey))
                .build()
                .parseClaimsJws(token).getBody().getSubject());
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
     * @return
     */
    private Key createSigningKey(String key) {
        return new SecretKeySpec(Base64.getDecoder().decode(key), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Header 에서 Bearer 제외한 토큰 Extract
     *
     * @param authorizationHeader
     * @return
     */
    public static String extractToken(String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    /**
     * Token 속 userId 를 가져와 Spring Security 의 인증 정보로 반환
     *
     * @param token
     * @return Authentication : Spring Security 에서 인증된 사용자에 대한 정보
     */
    public Authentication getAuthentication(String token) {
        Long userId = getUserIdFromToken(token);

        // 여기서 userDetailsService.loadUserByUsername()는 userId를 username 처럼 사용
        UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(userId));

        if (userRepository.existsByIdAndIsDeletedTrue(userId)) {
            throw new BusinessException(ResultCode.AUTHENTICATION_FAILED, "탈퇴한 유저는 접근 불가.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}