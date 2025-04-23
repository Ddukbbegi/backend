package com.ddukbbegi.api.user.dto.response;

import com.ddukbbegi.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyInfoResponseDto {
    private final String name;
    private final String email;

    public static MyInfoResponseDto toDto(User user) {
        return new MyInfoResponseDto(user.getName(), user.getEmail());
    }
}
