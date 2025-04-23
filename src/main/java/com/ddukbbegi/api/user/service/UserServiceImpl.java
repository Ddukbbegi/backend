package com.ddukbbegi.api.user.service;

import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.user.dto.response.SignupResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
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

    @Transactional
    @Override
    public SignupResponseDto signup(SignupRequestDto signUpRequestDto) {

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new BusinessException(VALID_FAIL, "이미 존재하는 Email 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        UserRole userRole = UserRole.of(signUpRequestDto.getRole());

        User user = User.of(signUpRequestDto.getEmail(), encodedPassword, signUpRequestDto.getName(), signUpRequestDto.getPhone(), userRole);

        userRepository.save(user);

        return SignupResponseDto.fromEntity(user.getId());
    }

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
    @Override
    public void updatePassword(Long userId, UpdatePasswordRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        if (!passwordEncoder.matches(requestDto.getOldPassword(), findUser.getPassword())) {
            throw new BusinessException(AUTHENTICATION_FAILED, "비밀번호가 일치하지 않습니다.");
        }

        String newPassword = passwordEncoder.encode(requestDto.getNewPassword());

        findUser.updatePassword(newPassword);
    }

    @Transactional
    public void updateEmail(Long userId, UpdateEmailRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            new BusinessException(VALID_FAIL, "이미 존재하는 Email 입니다.");
        }

        findUser.updateEmail(requestDto.getEmail());
    }

    @Transactional
    public void updateName(Long userId, UpdateNameRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        findUser.updateName(requestDto.getName());
    }

    @Transactional
    public void updatePhone(Long userId, UpdatePhoneRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        findUser.updatePhone(requestDto.getPhone());
    }

    @Override
    public void deleteUser(Long userId, DeleteUserRequestDto requestDto) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new BusinessException(AUTHENTICATION_FAILED, "비밀번호가 일치하지 않습니다.");
        }

        userRepository.delete(findUser);
    }
}
