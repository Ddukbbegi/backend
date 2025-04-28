package com.ddukbbegi.api.user.dto.request;

import com.ddukbbegi.api.store.dto.request.AbstractValidatorTest;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class UpdatePasswordRequestDtoTest extends AbstractValidatorTest {

    @DisplayName("성공 - 유효한 비밀번호 입력 시 validation 성공")
    @Test
    void givenValidPasswords_whenValidate_thenWorksFine() {
        // given
        UpdatePasswordRequestDto request = new UpdatePasswordRequestDto("oldPassword123!", "newPassword123!");

        // when
        Set<ConstraintViolation<UpdatePasswordRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @DisplayName("실패 - 빈 기존 비밀번호 입력 시 validation 실패")
    @Test
    void givenEmptyOldPassword_whenValidate_thenFailValidation() {
        // given
        UpdatePasswordRequestDto request = new UpdatePasswordRequestDto("", "newPassword123!");

        // when
        Set<ConstraintViolation<UpdatePasswordRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("비밀번호는 필수 입력값입니다.");
    }

    @DisplayName("실패 - 새로운 비밀번호가 패턴을 만족하지 않을 때 validation 실패")
    @Test
    void givenNewPasswordNotMatchingPattern_whenValidate_thenFailValidation() {
        // given
        UpdatePasswordRequestDto request = new UpdatePasswordRequestDto("oldPassword123!", "password");

        // when
        Set<ConstraintViolation<UpdatePasswordRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.");
    }

    @DisplayName("실패 - 새로운 비밀번호가 8자 미만일 때 validation 실패")
    @Test
    void givenNewPasswordShorterThan8_whenValidate_thenFailValidation() {
        // given
        UpdatePasswordRequestDto request = new UpdatePasswordRequestDto("oldPassword123!", "short");

        // when
        Set<ConstraintViolation<UpdatePasswordRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.");
    }
}