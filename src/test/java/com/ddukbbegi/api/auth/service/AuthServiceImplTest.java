package com.ddukbbegi.api.auth.service;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.auth.dto.response.LoginResponseDto;
import com.ddukbbegi.api.auth.dto.response.ReissueResponseDto;
import com.ddukbbegi.api.auth.dto.response.SignupResponseDto;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.config.PasswordEncoder;
import com.ddukbbegi.common.exception.BusinessException;
import com.ddukbbegi.common.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    private SignupRequestDto signupRequestDto;
    private LoginRequestDto loginRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.of("test@example.com", "encodedPassword", "Test User", "010-1234-5678", UserRole.USER);

        signupRequestDto = new SignupRequestDto(
                "test@test.com", "password", "test", "010-1234-5678", "USER"
        );
        loginRequestDto = new LoginRequestDto("test@example.com", "password123");
    }

    @DisplayName("성공 - 이메일이 존재하지 않을 경우 사용자 등록 성공")
    @Test
    void givenNotExistEmail_whenEmailNotExist_thenSignupSuccess() {
        // given
        given(userRepository.existsByEmail(signupRequestDto.email())).willReturn(false);
        given(passwordEncoder.encode(signupRequestDto.password())).willReturn("encodedPassword");

        User mockUser = new User(
                signupRequestDto.email(),
                "encodedPassword",
                signupRequestDto.name(),
                signupRequestDto.phone(),
                UserRole.USER
        );
        given(userRepository.save(any(User.class))).willReturn(mockUser);

        // when
        SignupResponseDto response = authService.signup(signupRequestDto);

        // then
        assertThat(response).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("실패 - 이메일이 존재할 경우 사용자 등록 실패")
    @Test
    void givenExistEmail_whenEmailExist_thenThrowException() {
        // given
        when(userRepository.existsByEmail(signupRequestDto.email())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.signup(signupRequestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 존재하는 Email 입니다.");
    }

    @DisplayName("성공 - 정상적인 아이디와 비밀번호 입력 시 로그인 성공")
    @Test
    void givenValidLogin_whenCredentialsValid_thenReturnTokens() {
        // Given
        given(userRepository.findByEmailAndIsDeletedFalse(loginRequestDto.email())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequestDto.password(), user.getPassword())).willReturn(true);
        given(jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getUserRole())).willReturn("accessToken");
        given(jwtUtil.generateRefreshToken(user.getId())).willReturn("refreshToken");
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        // When
        LoginResponseDto response = authService.login(loginRequestDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("accessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken");
    }

    @DisplayName("실패 - 잘못된 비밀번호 입력 시 로그인 실패")
    @Test
    void givenInvalidPassword_whenCredentialsInvalid_thenThrowException() {
        // given
        given(userRepository.findByEmailAndIsDeletedFalse(loginRequestDto.email())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequestDto.password(), user.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("아이디(로그인 이메일) 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.");
    }

    @DisplayName("성공 - 유효한 리프레시 토큰일 시 액세스 토큰 재발급 성공")
    @Test
    void givenValidRefreshToken_whenValidRefreshToken_thenReturnNewAccessToken() {
        // given
        String refreshToken = "validRefreshToken";
        when(jwtUtil.isValidToken(refreshToken)).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(refreshToken)).thenReturn(user.getId());
        when(userRepository.findByIdOrElseThrow(user.getId())).thenReturn(user);
        when(jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getUserRole())).thenReturn("newAccessToken");

        // when
        ReissueResponseDto response = authService.reissue(refreshToken);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("newAccessToken");
    }

    @DisplayName("실패 - 유효하지 않은 리프레시 토큰일 시 액세스 토큰 재발급 실패")
    @Test
    void givenInvalidRefreshToken_whenInvalidRefreshToken_thenThrowException() {
        // given
        String refreshToken = "invalidRefreshToken";
        given(jwtUtil.isValidToken(refreshToken)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.reissue(refreshToken))
                .isInstanceOf(BusinessException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }
}