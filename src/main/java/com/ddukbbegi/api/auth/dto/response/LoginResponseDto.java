package com.ddukbbegi.api.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public static LoginResponseDto from(String accessToken, String refreshToken) {
        return new LoginResponseDto(accessToken, refreshToken);
    }
}
