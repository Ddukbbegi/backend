package com.ddukbbegi.api.user.service;

import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.request.UpdatePasswordRequestDto;
import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;

public interface UserService {

    /**
     * 로그인 중인 회원의 정보 조회
     *
     * @param userId
     * @return MyInfoResponseDto
     */
    MyInfoResponseDto getUser(Long userId);

    /**
     * 다른 회원 정보 조회
     *
     * @param userId
     * @return UserInfoResponseDto
     */
    UserInfoResponseDto getUserById(Long userId);

    /**
     * 로그인 중인 회원의 이메일 수정
     *
     * @param userId
     * @param requestDto
     */
    void updateEmail(Long userId, UpdateEmailRequestDto requestDto);

    /**
     * 로그인 중인 회원의 이름 수정
     * @param userId
     * @param requestDto
     */
    void updateName(Long userId, UpdateNameRequestDto requestDto);

    /**
     * 로그인 중인 회원의 전화번호 수정
     * @param userId
     * @param requestDto
     */
    void updatePhone(Long userId, UpdatePhoneRequestDto requestDto);

    /**
     * 로그인 중인 회원의 비밀번호 수정
     * @param userId
     * @param requestDto
     */
    void deleteUser(Long userId, DeleteUserRequestDto requestDto);

    /**
     * 로그인 중인 회원의 탈퇴
     * @param userId
     * @param requestDto
     */
    void updatePassword(Long userId, UpdatePasswordRequestDto requestDto);
}
