package com.ddukbbegi.api.user.controller;

import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.user.dto.response.SignupResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;
import com.ddukbbegi.api.user.service.UserService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/auth/signup")
    public BaseResponse<SignupResponseDto> createUser(@RequestBody SignupRequestDto requestDto) {

        SignupResponseDto signUpResponseDto = userService.signup(requestDto);
        return BaseResponse.success(signUpResponseDto, ResultCode.CREATED);
    }

    // 내 정보 조회
    // 추후에 PathVariable 대신 토큰 값 이용할 예정.
    @GetMapping("/users/me/{userId}")
    public BaseResponse<MyInfoResponseDto> getUser(@PathVariable Long userId) {
        MyInfoResponseDto myInfoResponseDto = userService.getUser(userId);

        return BaseResponse.success(myInfoResponseDto, ResultCode.OK);
    }

    // 다른 유저 조회
    @GetMapping("/users/{userId}")
    public BaseResponse<UserInfoResponseDto> getUserById(@PathVariable Long userId) {
        UserInfoResponseDto userInfoResponseDto = userService.getUserById(userId);

        return BaseResponse.success(userInfoResponseDto, ResultCode.OK);
    }

    /**
     * 추후에 PathVariable 대신 토큰 값 이용할 예정.
     * Auth 경로인 경우 Auth 패키지에 추가할 예정 (기본적인 User Crud 만드는 것이 목적)
     * -> 필터와 토큰 로직이 만들어질 경우 리팩토링 할 것임.
     */

    // 패스워드 변경
    @PatchMapping("/auth/changePassword/{userId}")
    public BaseResponse<Void> updatePassword(@PathVariable Long userId,
                                             @RequestBody UpdatePasswordRequestDto requestDto) {
        userService.updatePassword(userId, requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    // 이메일 변경
    @PatchMapping("/users/me/email/{userId}")
    public BaseResponse<Void> updateEmail(@PathVariable Long userId,
                                          @RequestBody UpdateEmailRequestDto requestDto) {
        userService.updateEmail(userId, requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    // 이름 변경
    @PatchMapping("/users/me/name/{userId}")
    public BaseResponse<Void> updateName(@PathVariable Long userId,
                                         @RequestBody UpdateNameRequestDto requestDto
    ) {
        userService.updateName(userId, requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    // 전화번호 변경
    @PatchMapping("/users/me/phone/{userId}")
    public BaseResponse<Void> updatePhone(@PathVariable Long userId,
                                          @RequestBody UpdatePhoneRequestDto requestDto) {
        userService.updatePhone(userId, requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    // 회원 탈퇴
    @DeleteMapping("/users/{userId}")
    public BaseResponse<Void> deleteUser(@PathVariable Long userId, @RequestBody DeleteUserRequestDto requestDto) {
        userService.deleteUser(userId, requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }
}
