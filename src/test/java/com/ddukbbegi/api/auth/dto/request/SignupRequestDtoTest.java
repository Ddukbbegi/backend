package com.ddukbbegi.api.auth.dto.request;

import com.ddukbbegi.api.store.dto.request.AbstractValidatorTest;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SignupRequestDtoTest extends AbstractValidatorTest {

    @DisplayName("성공 - 유효한 데이터 입력 시 validation 성공")
    @Test
    void givenValidData_whenValidate_thenWorksFine() {
        // Given
        SignupRequestDto request = new SignupRequestDto(
                "test@test.com",
                "PwdPwdPwd1!",
                "test",
                "010-0000-0000",
                "USER"
        );

        // When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @DisplayName("실패 - 이메일 형식이 잘못된 경우 validation 실패")
    @Test
    void givenInvalidEmail_whenValidate_thenFailValidation() {
        // Given
        SignupRequestDto request = new SignupRequestDto(
                "invalid-email",
                "PwdPwdPwd1!",
                "test",
                "010-0000-0000",
                "USER"
        );

        // When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("유효한 이메일 주소를 입력하세요.");
    }

    @DisplayName("실패 - 비밀번호 형식이 잘못된 경우 validation 실패")
    @Test
    void givenInvalidPassword_whenValidate_thenFailValidation() {
        // Given
        SignupRequestDto request = new SignupRequestDto(
                "test@test.com",
                "short",
                "test",
                "010-0000-0000",
                "USER"
        );

        // When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.");
    }

    @DisplayName("실패 - 전화번호 형식이 잘못된 경우 validation 실패")
    @Test
    void givenInvalidPhone_whenValidate_thenFailValidation() {
        // Given
        SignupRequestDto request = new SignupRequestDto(
                "test@test.com",
                "PwdPwdPwd1!",
                "test",
                "123-456-7890",
                "USER"
        );

        // When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("전화번호 형식이 올바르지 않습니다.");
    }

    @DisplayName("이름이 비어 있는 경우 validation 실패")
    @Test
    void givenEmptyName_whenValidate_thenFailValidation() {
        // Given
        SignupRequestDto request = new SignupRequestDto(
                "test@example.com",
                "Valid123!",
                null,
                "010-1234-5678",
                "USER"
        );

        // When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("이름은 필수 입력값입니다.");
    }

    @DisplayName("실패 - 권한이 비어 있는 경우 validation 실패")
    @Test
    void givenEmptyRole_whenValidate_thenFailValidation() {
        // Given
        SignupRequestDto request = new SignupRequestDto(
                "test@example.com",
                "Valid123!",
                "Test User",
                "010-1234-5678",
                null
        );

        // When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);  // 오류가 1개여야 함 (role이 비어있는 경우)
        assertThat(violations)
                .extracting("message")
                .contains("권한은 필수값 입니다.");
    }
}