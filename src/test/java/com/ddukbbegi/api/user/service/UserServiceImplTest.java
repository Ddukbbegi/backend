package com.ddukbbegi.api.user.service;

import com.ddukbbegi.api.user.dto.request.*;
import com.ddukbbegi.api.user.dto.response.MyInfoResponseDto;
import com.ddukbbegi.api.user.dto.response.UserInfoResponseDto;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

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

    private Long userId;
    private User user;
    private String rawPassword = "password";
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new User("test@test.com", passwordEncoder.encode(rawPassword), "Test", "010-0000-0000", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        fixedClock = Clock.fixed(Instant.parse("2025-06-01T12:00:00Z"), ZoneId.of("UTC"));
    }

    @DisplayName("성공 - 유저 정보 조회 성공")
    @Test
    void whenUserIdProvided_thenReturnMyInfo() {
        // given
        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        // when
        MyInfoResponseDto response = userServiceImpl.getUser(userId);

        // then
        assertNotNull(response);
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getName(), response.name());
    }

    @DisplayName("성공 - 유저 ID 제공 시 유저 정보 반환")
    @Test
    void whenUserIdProvided_thenReturnUserInfo() {
        // given
        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        // when
        UserInfoResponseDto response = userServiceImpl.getUserById(userId);

        // then
        assertNotNull(response);
        assertEquals(user.getName(), response.name());
    }

    @DisplayName("성공 - 새로운 이메일로 이메일 업데이트 성공")
    @Test
    void givenNewEmail_whenUpdateEmail_thenEmailUpdated() {
        // given
        String newEmail = "new@test.com";
        UpdateEmailRequestDto requestDto = new UpdateEmailRequestDto(newEmail);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
        given(userRepository.existsByEmail(newEmail)).willReturn(false);

        // when
        userServiceImpl.updateEmail(userId, requestDto);

        // then
        assertEquals(newEmail, user.getEmail());
    }

    @DisplayName("실패 - 기존에 존재하는 이메일로 업데이트 시 실패")
    @Test
    void givenExistEmail_whenUpdateEmail_thenThrowException() {
        // given
        String newEmail = "test@test.com";
        UpdateEmailRequestDto requestDto = new UpdateEmailRequestDto(newEmail);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
        given(userRepository.existsByEmail(newEmail)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userServiceImpl.updateEmail(userId, requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 존재하는 Email 입니다.");
    }

    @DisplayName("실패 - 기존에 존재하는 이메일로 업데이트 시 실패")
    @Test
    void givenSocialLoginUser_whenUpdateEmail_thenThrowException() {
        // given
        String newEmail = "new@example.com";
        UpdateEmailRequestDto requestDto = new UpdateEmailRequestDto(newEmail);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
        given(userRepository.existsByEmail(newEmail)).willReturn(false);

        ReflectionTestUtils.setField(user, "provider", "google");
        ReflectionTestUtils.setField(user, "providerId", "providerId");

        // when & then
        assertThatThrownBy(() -> userServiceImpl.updateEmail(userId, requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("소셜 로그인한 유저는 이메일 변경을 할 수 없습니다.");
    }

    @DisplayName("성공 - 새로운 이름로 이름 업데이트 성공")
    @Test
    void givenNewName_whenUpdateName_thenNameUpdated() {
        // given
        String newName = "newName";
        UpdateNameRequestDto requestDto = new UpdateNameRequestDto(newName);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        // when
        userServiceImpl.updateName(userId, requestDto);

        // then
        assertEquals(newName, user.getName());
    }

    @DisplayName("성공 - 새로운 전화번호로 전화번호 업데이트 성공")
    @Test
    void givenNewPhone_whenUpdatePhone_thenPhoneUpdated() {
        // given
        String newPhone = "010-1111-1111";
        UpdatePhoneRequestDto requestDto = new UpdatePhoneRequestDto(newPhone);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        // when
        userServiceImpl.updatePhone(userId, requestDto);

        // then
        assertEquals(newPhone, user.getPhone());
    }

    @DisplayName("성공 - 새로운 비밀번호로 비밀번호 업데이트 성공")
    @Test
    void givenValidOldPassword_whenUpdatePassword_thenPasswordUpdated() {
        // given
        String oldPassword = rawPassword;
        String newPassword = "newPassword";
        UpdatePasswordRequestDto requestDto = new UpdatePasswordRequestDto(oldPassword, newPassword);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
        given(passwordEncoder.matches(oldPassword, user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(newPassword)).willReturn("encodedNewPassword");

        // when
        userServiceImpl.updatePassword(userId, requestDto);

        // then
        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
    }

    @DisplayName("실패 - 기존 비밀번호가 잘못된 비밀번호일 시 실패 ")
    @Test
    void givenInvalidOldPassword_whenUpdatePassword_thenThrowException() {
        // given
        String wrongOldPassword = "wrongPassword";
        String newPassword = "newPassword";
        UpdatePasswordRequestDto requestDto = new UpdatePasswordRequestDto(wrongOldPassword, newPassword);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
        given(passwordEncoder.matches(wrongOldPassword, user.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userServiceImpl.updatePassword(userId, requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("성공 - JWT 토큰이 유효할 때 유저 삭제 성공")
    @Test
    void givenValidJwtToken_whenDeleteUser_thenUserIsDeletedAndBlacklistIsSet() {
        // given
        String accessToken = "validAccessToken";
        Instant tokenExpireTime = Instant.parse("2025-06-01T12:30:00Z");
        DeleteUserRequestDto requestDto = new DeleteUserRequestDto(rawPassword);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
        given(passwordEncoder.matches(rawPassword, user.getPassword())).willReturn(true);
        given(jwtUtil.isValidToken(accessToken)).willReturn(true);
        given(jwtUtil.getExpireDate(accessToken)).willReturn(Date.from(tokenExpireTime));
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        // when
        userServiceImpl.deleteUser(userId, requestDto, accessToken);

        // then
        verify(redisTemplate).delete("userId:" + userId);

        verify(valueOperations).set(
                eq("blacklist:" + accessToken),
                eq("userId:" + userId),
                any(Duration.class)
        );

        verify(userRepository).delete(user);
    }
}