package com.ddukbbegi.api.user.service;

import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.user.dto.response.SignupResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;
import org.hibernate.sql.Update;

public interface UserService {

    /**
     * 유저 회원 가입
     * @param signUpRequestDto
     * @return SignUpResponseDto
     */
    SignupResponseDto signup(SignupRequestDto signUpRequestDto);


    /**
     * 패스워드 변경
     * @param requestDto
     */
    void updatePassword(Long userId, UpdatePasswordRequestDto requestDto);

    void updateEmail(Long userId, UpdateEmailRequestDto requestDto);

    void updateName(Long userId, UpdateNameRequestDto requestDto);

    void updatePhone(Long userId, UpdatePhoneRequestDto requestDto);

    MyInfoResponseDto getUser(Long userId);

    UserInfoResponseDto getUserById(Long userId);

    void deleteUser(Long userId, DeleteUserRequestDto requestDto);
}
