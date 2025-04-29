package com.ddukbbegi.api.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(String accessToken, String refreshToken) {

    public static LoginResponseDto from(String accessToken, String refreshToken) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
