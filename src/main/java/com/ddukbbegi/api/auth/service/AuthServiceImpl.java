package com.ddukbbegi.api.auth.service;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.response.LoginResponseDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;
import com.ddukbbegi.api.auth.entity.Auth;
import com.ddukbbegi.api.auth.repository.AuthRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.auth.JwtUtil;
import com.ddukbbegi.common.config.PasswordEncoder;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ddukbbegi.common.component.ResultCode.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthRepository authRepository;

    @Transactional
    @Override
    public SignupResponseDto signup(SignupRequestDto signUpRequestDto) {

        if (userRepository.existsByEmail(signUpRequestDto.email())) {
            throw new BusinessException(VALID_FAIL, "이미 존재하는 Email 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.password());

        UserRole userRole = UserRole.of(signUpRequestDto.role());

        User user = User.of(
                signUpRequestDto.email(),
                encodedPassword,
                signUpRequestDto.name(),
                signUpRequestDto.phone(),
                userRole);

        userRepository.save(user);

        return SignupResponseDto.fromEntity(user.getId());
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {

        // 1. User Email 일치 여부 확인
        User findUser = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() ->
                        new BusinessException(ResultCode.NOT_FOUND, "해당 Entity를 찾을 수 없습니다. email = " + requestDto.email())
                );

        // 2. Pwd 일치 여부
        if (!passwordEncoder.matches(requestDto.password(), findUser.getPassword())) {
            throw new BusinessException(LOGIN_FAILED);
        }

        // 3. 토큰 발행
        String accessToken = jwtUtil.generateAccessToken(findUser.getId(), findUser.getEmail(), findUser.getUserRole());
        String refreshToken = jwtUtil.generateRefreshToken(findUser.getId());

        // 4. RefreshToken 저장
        Auth token = Auth.builder()
                .userId(findUser.getId())
                .refreshToken(refreshToken)
                .build();

        authRepository.save(token);

        // 5. Dto 형태로 반환
        return LoginResponseDto.from(accessToken, refreshToken);
    }


    @Override
    public void logout(String accessToken) {
        // 1. Access Token 검증 > 추후에 에러코드 변경
        if (!jwtUtil.isValidToken(accessToken)) {
            throw new BusinessException(AUTHENTICATION_FAILED);
        }

        // 2. Access Token 에서 userId 을 가져오기
        Long userIdFromToken = jwtUtil.getUserIdFromToken(accessToken);

        // 3. DB 에 저장된 RefreshToken 제거
        authRepository.deleteByUserId(userIdFromToken);

        // 4. Access Token blacklist 에 등록하여 만료
        // 미구현.

    }
}
