package com.ddukbbegi.api.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponseDto {

    private final Long id;

    public static SignupResponseDto fromEntity(Long id) {
        return new SignupResponseDto(id);
    }
}
