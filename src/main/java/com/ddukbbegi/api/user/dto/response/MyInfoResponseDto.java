package com.ddukbbegi.api.user.dto.response;

import com.ddukbbegi.api.user.entity.User;
import lombok.Builder;

@Builder
public record MyInfoResponseDto(String name, String email) {
    public static MyInfoResponseDto fromEntity(User user) {
        return MyInfoResponseDto
                .builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
