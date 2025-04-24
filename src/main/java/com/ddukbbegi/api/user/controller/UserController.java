package com.ddukbbegi.api.user.controller;

import com.ddukbbegi.api.auth.dto.request.UpdatePasswordRequestDto;
import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;
import com.ddukbbegi.api.user.service.UserService;
import com.ddukbbegi.common.auth.CustomUserDetails;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    // 추후에 PathVariable 대신 토큰 값 이용할 예정.
    @GetMapping("/users/me")
    public BaseResponse<MyInfoResponseDto> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MyInfoResponseDto myInfoResponseDto = userService.getUser(userDetails.getUserId());

        return BaseResponse.success(myInfoResponseDto, ResultCode.OK);
    }

    // 다른 유저 조회
    @GetMapping("/users/{userId}")
    public BaseResponse<UserInfoResponseDto> getUserById(@PathVariable Long userId) {
        UserInfoResponseDto userInfoResponseDto = userService.getUserById(userId);

        return BaseResponse.success(userInfoResponseDto, ResultCode.OK);
    }

    // 이메일 변경
    @PatchMapping("/users/me/email")
    public BaseResponse<Void> updateEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody UpdateEmailRequestDto requestDto) {
        userService.updateEmail(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    // 이름 변경
    @PatchMapping("/users/me/name")
    public BaseResponse<Void> updateName(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody UpdateNameRequestDto requestDto
    ) {
        userService.updateName(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    // 전화번호 변경
    @PatchMapping("/users/me/phone")
    public BaseResponse<Void> updatePhone(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody UpdatePhoneRequestDto requestDto) {
        userService.updatePhone(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    // 비밀번호 수정
    @PatchMapping("/auth/changePassword")
    public BaseResponse<Void> updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestBody UpdatePasswordRequestDto requestDto) {
        userService.updatePassword(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    // 회원 탈퇴
    @DeleteMapping("/users")
    public BaseResponse<Void> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody DeleteUserRequestDto requestDto) {
        userService.deleteUser(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }
}
