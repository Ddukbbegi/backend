package com.ddukbbegi.api.auth.service;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.response.LoginResponseDto;
import com.ddukbbegi.api.auth.dto.response.ReissueResponseDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.jwt.JwtUtil;
import com.ddukbbegi.common.config.PasswordEncoder;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

import static com.ddukbbegi.common.component.ResultCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

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

    @Transactional
    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {

        // 1. User Email 일치, 탈퇴 여부 확인
        User findUser = userRepository.findByEmailAndIsDeletedFalse(requestDto.email())
                .orElseThrow(() -> new BusinessException(LOGIN_FAILED));

        // 2. Pwd 일치 여부
        if (!passwordEncoder.matches(requestDto.password(), findUser.getPassword())) {
            throw new BusinessException(LOGIN_FAILED);
        }

        // 3. 토큰 발행
        String accessToken = jwtUtil.generateAccessToken(findUser.getId(), findUser.getEmail(), findUser.getUserRole());
        String refreshToken = jwtUtil.generateRefreshToken(findUser.getId());


        // 4. RefreshToken 저장
        redisTemplate.opsForValue().set(
                "userId:" + findUser.getId(),
                "refreshToken:" + refreshToken,
                Duration.ofDays(7));

        // 5. Dto 형태로 반환
        return LoginResponseDto.from(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public void logout(String accessToken) {

        try {
            if (jwtUtil.isValidToken(accessToken)) {
                Duration duration = Duration.between(Instant.now(), jwtUtil.getExpireDate(accessToken).toInstant());
                redisTemplate.opsForValue().set("blacklist:" + accessToken, "userId:" + jwtUtil.getUserIdFromToken(accessToken), duration);
            }
        } catch (Exception ex) {
            log.warn("토큰이 유효하지 않거나, 이미 로그아웃 된 상태입니다.");
        }

        redisTemplate.delete("userId:" + jwtUtil.getUserIdFromToken(accessToken));
    }

    @Transactional
    @Override
    public ReissueResponseDto reissue(String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BusinessException(TOKEN_INVALID, "RefreshToken 쿠키가 없습니다.");
        }

        if (!jwtUtil.isValidToken(refreshToken)) {
            throw new BusinessException(TOKEN_INVALID);
        }

        User findUser = userRepository.findByIdOrElseThrow(jwtUtil.getUserIdFromToken(refreshToken));

        String accessToken = jwtUtil.generateAccessToken(findUser.getId(), findUser.getEmail(), findUser.getUserRole());

        return ReissueResponseDto.from(accessToken);
    }
}
