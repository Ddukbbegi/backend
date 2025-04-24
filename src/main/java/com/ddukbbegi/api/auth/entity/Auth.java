package com.ddukbbegi.api.auth.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 14440)
public class Auth {
    @Id
    private String id = UUID.randomUUID().toString();

    private Long userId;  // String 타입으로 변경

    private String refreshToken;

    @Builder
    public Auth(Long userId, String refreshToken) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    // refreshToken 재발급
    public void refreshUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

