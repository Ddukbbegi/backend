package com.ddukbbegi.api.auth.dto.response;

import lombok.Builder;

@Builder
public record ReissueResponseDto(
        String accessToken
) {
    public static ReissueResponseDto from(String token) {
        return ReissueResponseDto.builder()
                .accessToken(token)
                .build();
    }
}
