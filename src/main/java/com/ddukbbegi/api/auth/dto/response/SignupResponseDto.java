package com.ddukbbegi.api.auth.dto.response;

import lombok.Builder;

@Builder
public record SignupResponseDto(Long id) {

    public static SignupResponseDto fromEntity(Long id) {
        return SignupResponseDto.builder()
                .id(id)
                .build();
    }
}
