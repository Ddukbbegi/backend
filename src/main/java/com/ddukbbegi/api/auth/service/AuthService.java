package com.ddukbbegi.api.auth.service;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.request.UpdatePasswordRequestDto;
import com.ddukbbegi.api.auth.dto.response.LoginResponseDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;

public interface AuthService {
    /**
     * 유저 회원 가입
     *
     * @param signUpRequestDto
     * @return SignUpResponseDto
     */
    SignupResponseDto signup(SignupRequestDto signUpRequestDto);

    LoginResponseDto login(LoginRequestDto requestDto);

    void logout(String token);
}
