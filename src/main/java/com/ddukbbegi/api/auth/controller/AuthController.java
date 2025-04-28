package com.ddukbbegi.api.auth.controller;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.response.LoginResponseDto;
import com.ddukbbegi.api.auth.dto.response.ReissueResponseDto;
import com.ddukbbegi.api.auth.service.AuthService;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;
import com.ddukbbegi.api.point.service.PointService;
import com.ddukbbegi.common.auth.JwtUtil;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final PointService pointService;

    /**
     * 회원가입
     *
     * @param requestDto
     * @return SignupResponseDto : token
     */
    @PostMapping("/signup")
    public BaseResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto signUpResponseDto = authService.signup(requestDto);
        pointService.savePointWallet(signUpResponseDto.id());
        return BaseResponse.success(signUpResponseDto, ResultCode.CREATED);
    }

    /**
     * 로그인
     *
     * @param requestDto
     * @param response
     * @return LoginResponseDto
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto,
                                                HttpServletResponse response) {

        LoginResponseDto loginResponseDto = authService.login(requestDto);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtUtil.extractToken(loginResponseDto.refreshToken()))
                .path("/api/auth")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());

        return BaseResponse.success(loginResponseDto, ResultCode.OK);
    }

    /**
     * 로그아웃
     *
     * @param authorizationHeader
     * @return Void
     */
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {

        String accessToken = JwtUtil.extractToken(authorizationHeader);

        authService.logout(accessToken);

        return BaseResponse.success(ResultCode.NO_CONTENT);
    }


    /**
     * 토큰 재발급
     *
     * @param refreshToken
     * @return
     */
    @PostMapping("/reissue")
    public BaseResponse<ReissueResponseDto> reissue(@CookieValue(value = "refreshToken") String refreshToken) {
        System.out.println(refreshToken);
        return BaseResponse.success(authService.reissue(refreshToken), ResultCode.OK);
    }
}
