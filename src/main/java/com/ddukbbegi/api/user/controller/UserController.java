package com.ddukbbegi.api.user.controller;

import com.ddukbbegi.api.user.dto.request.UpdatePasswordRequestDto;
import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;
import com.ddukbbegi.api.user.service.UserService;
import com.ddukbbegi.api.user.entity.CustomUserDetails;
import com.ddukbbegi.common.jwt.JwtUtil;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 로그인 중인 회원의 정보 조회
     *
     * @param userDetails
     * @return BaseResponse<MyInfoResponseDto> : 로그인한 유저 정보 조회
     */
    @GetMapping("/users/me")
    public BaseResponse<MyInfoResponseDto> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MyInfoResponseDto myInfoResponseDto = userService.getUser(userDetails.getUserId());

        return BaseResponse.success(myInfoResponseDto, ResultCode.OK);
    }

    /**
     * 다른 회원 정보 조회
     *
     * @param userId
     * @return BaseResponse<UserInfoResponseDto> : Username 만 조회
     */
    @GetMapping("/users/{userId}")
    public BaseResponse<UserInfoResponseDto> getUserById(@PathVariable Long userId) {
        UserInfoResponseDto userInfoResponseDto = userService.getUserById(userId);

        return BaseResponse.success(userInfoResponseDto, ResultCode.OK);
    }

    /**
     * 로그인 중인 회원의 이메일 수정
     *
     * @param userDetails
     * @param requestDto
     * @return BaseResponse<Void>
     */
    @PatchMapping("/users/me/email")
    public BaseResponse<Void> updateEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @Valid @RequestBody UpdateEmailRequestDto requestDto) {
        userService.updateEmail(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    /**
     * 로그인 중인 회원의 이름 수정
     *
     * @param userDetails
     * @param requestDto
     * @return BaseResponse<Void>
     */
    @PatchMapping("/users/me/name")
    public BaseResponse<Void> updateName(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @Valid @RequestBody UpdateNameRequestDto requestDto
    ) {
        userService.updateName(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    /**
     * 로그인 중인 회원의 전화번호 수정
     *
     * @param userDetails
     * @param requestDto
     * @return
     */
    @PatchMapping("/users/me/phone")
    public BaseResponse<Void> updatePhone(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @Valid @RequestBody UpdatePhoneRequestDto requestDto) {
        userService.updatePhone(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    /**
     * 로그인 중인 회원의 비밀번호 수정
     *
     * @param userDetails
     * @param requestDto
     * @return BaseResponse<Void>
     */
    @PatchMapping("/users/changePassword")
    public BaseResponse<Void> updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Valid @RequestBody UpdatePasswordRequestDto requestDto) {
        userService.updatePassword(userDetails.getUserId(), requestDto);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    /**
     * 로그인 중인 회원의 탈퇴
     *
     * @param userDetails
     * @param requestDto
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/users")
    public BaseResponse<Void> deleteUser(@RequestHeader("Authorization") String authorizationHeader,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody DeleteUserRequestDto requestDto) {
        String accessToken = JwtUtil.extractToken(authorizationHeader);
        userService.deleteUser(userDetails.getUserId(), requestDto, accessToken);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }
}
