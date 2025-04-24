package com.ddukbbegi.api.user.service;

import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.request.UpdatePasswordRequestDto;
import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;

public interface UserService {

    void updateEmail(Long userId, UpdateEmailRequestDto requestDto);

    void updateName(Long userId, UpdateNameRequestDto requestDto);

    void updatePhone(Long userId, UpdatePhoneRequestDto requestDto);

    MyInfoResponseDto getUser(Long userId);

    UserInfoResponseDto getUserById(Long userId);

    void deleteUser(Long userId, DeleteUserRequestDto requestDto);

    void updatePassword(Long userId, UpdatePasswordRequestDto requestDto);
}
