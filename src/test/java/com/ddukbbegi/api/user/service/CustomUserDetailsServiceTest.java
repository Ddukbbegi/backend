package com.ddukbbegi.api.user.service;

import com.ddukbbegi.api.user.entity.CustomUserDetails;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private Long userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new User("test@test.com", "password", "Test", "010-0000-0000", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId); // id 설정
    }

    @DisplayName("성공 - 사용자 정보가 있을 경우 UserDetails 반환")
    @Test
    void givenUserId_whenLoadUserByUsername_thenReturnUserDetails() {
        // given
        given(userRepository.findById(userId)).willReturn(java.util.Optional.of(user));

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(userId));

        // then
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomUserDetails);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        assertEquals(userId, customUserDetails.getUserId());
        assertEquals("ROLE_USER", customUserDetails.getAuthorities().toArray()[0].toString());
    }

    @DisplayName("실패 - 사용자 정보가 없을 경우 예외 발생")
    @Test
    void givenNonExistentUserId_whenLoadUserByUsername_thenThrowBusinessException() {
        // given
        Long nonExistentUserId = 999L;
        given(userRepository.findById(nonExistentUserId)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> customUserDetailsService.loadUserByUsername(String.valueOf(nonExistentUserId)));
    }
}