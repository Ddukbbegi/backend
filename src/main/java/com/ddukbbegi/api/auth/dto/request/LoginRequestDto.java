package com.ddukbbegi.api.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    private final String email;
    private final String password;
}
