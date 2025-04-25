package com.ddukbbegi.api.user.service;

import com.ddukbbegi.api.auth.dto.request.UpdatePasswordRequestDto;
import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.config.PasswordEncoder;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ddukbbegi.common.component.ResultCode.AUTHENTICATION_FAILED;
import static com.ddukbbegi.common.component.ResultCode.VALID_FAIL;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MyInfoResponseDto getUser(Long userId) {
        User findUser = userRepository.findByIdOrElseThrow(userId);
        return MyInfoResponseDto.fromEntity(findUser);
    }

    @Override
    public UserInfoResponseDto getUserById(Long userId) {
        User findUser = userRepository.findByIdOrElseThrow(userId);
        return UserInfoResponseDto.fromEntity(findUser);
    }

    @Transactional
    public void updateEmail(Long userId, UpdateEmailRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        if (userRepository.existsByEmail(requestDto.email())) {
            new BusinessException(VALID_FAIL, "이미 존재하는 Email 입니다.");
        }

        findUser.updateEmail(requestDto.email());
    }

    @Transactional
    public void updateName(Long userId, UpdateNameRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        findUser.updateName(requestDto.name());
    }

    @Transactional
    public void updatePhone(Long userId, UpdatePhoneRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        findUser.updatePhone(requestDto.phone());
    }

    @Transactional
    @Override
    public void updatePassword(Long userId, UpdatePasswordRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        if (!passwordEncoder.matches(requestDto.oldPassword(), findUser.getPassword())) {
            throw new BusinessException(AUTHENTICATION_FAILED, "비밀번호가 일치하지 않습니다.");
        }

        String newPassword = passwordEncoder.encode(requestDto.newPassword());

        findUser.updatePassword(newPassword);
    }

    @Override
    public void deleteUser(Long userId, DeleteUserRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        if (!passwordEncoder.matches(requestDto.password(), findUser.getPassword())) {
            throw new BusinessException(AUTHENTICATION_FAILED);
        }

        userRepository.delete(findUser);
    }
}
