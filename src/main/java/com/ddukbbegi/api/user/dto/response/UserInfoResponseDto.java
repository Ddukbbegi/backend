package com.ddukbbegi.api.user.dto.response;

import com.ddukbbegi.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private String name;

    public static UserInfoResponseDto toDto(User user) {
        return new UserInfoResponseDto(user.getName());
    }
}
