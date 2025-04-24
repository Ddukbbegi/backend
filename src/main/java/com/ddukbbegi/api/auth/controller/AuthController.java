package com.ddukbbegi.api.auth.controller;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.UpdatePasswordRequestDto;
import com.ddukbbegi.api.auth.dto.response.LoginResponseDto;
import com.ddukbbegi.api.auth.service.AuthService;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;
import com.ddukbbegi.common.auth.JwtUtil;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public BaseResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto signUpResponseDto = authService.signup(requestDto);
        return BaseResponse.success(signUpResponseDto, ResultCode.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto loginResponseDto = authService.login(requestDto);
        return BaseResponse.success(loginResponseDto, ResultCode.OK);
    }

    // 로그아웃
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {

        String token = JwtUtil.extractToken(authorizationHeader);

        authService.logout(token);

        return BaseResponse.success(ResultCode.NO_CONTENT);
    }


    // 토큰재발급 미구현
}
