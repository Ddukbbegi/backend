package com.ddukbbegi.api.user.dto.response;

import com.ddukbbegi.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponseDto {

    private final Long id;

    public static SignupResponseDto toDto(Long id) {
        return new SignupResponseDto(id);
    }
}
