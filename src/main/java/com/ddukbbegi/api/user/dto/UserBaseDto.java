package com.ddukbbegi.api.user.dto;

import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public record UserBaseDto(
        Long userId,
        UserRole role
) {

    public static UserBaseDto fromEntity(User user) {
        return UserBaseDto.builder()
                .userId(user.getId())
                .role(user.getUserRole())
                .build();
    }
}