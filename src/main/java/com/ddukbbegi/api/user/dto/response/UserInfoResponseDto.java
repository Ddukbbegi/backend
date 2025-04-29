package com.ddukbbegi.api.user.dto.response;

import com.ddukbbegi.api.user.entity.User;

public record UserInfoResponseDto(String name) {
    public static UserInfoResponseDto fromEntity(User user) {
        return new UserInfoResponseDto(user.getName());
    }
}
