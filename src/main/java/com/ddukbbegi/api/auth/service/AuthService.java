package com.ddukbbegi.api.auth.service;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.request.UpdatePasswordRequestDto;
import com.ddukbbegi.api.auth.dto.response.LoginResponseDto;
import com.ddukbbegi.api.auth.dto.response.ReissueResponseDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;

public interface AuthService {

    /**
     * 회원 가입
     *
     * @param signUpRequestDto
     * @return SignUpResponseDto
     */
    SignupResponseDto signup(SignupRequestDto signUpRequestDto);

    /**
     * 로그인
     *
     * @param requestDto
     * @return LoginResponseDto
     */
    LoginResponseDto login(LoginRequestDto requestDto);

    /**
     * 로그아웃
     *
     * @param accessToken
     */
    void logout(String accessToken);

    /**
     * 토큰 재발급
     *
     * @param refreshToken
     * @return ReissueResponseDto
     */
    ReissueResponseDto reissue(String refreshToken);
}
