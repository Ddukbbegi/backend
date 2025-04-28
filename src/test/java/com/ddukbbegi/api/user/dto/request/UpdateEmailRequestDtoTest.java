package com.ddukbbegi.api.user.dto.request;

import com.ddukbbegi.api.store.dto.request.AbstractValidatorTest;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UpdateEmailRequestDtoTest extends AbstractValidatorTest {

    @DisplayName("유효한 이메일 입력 시 validation 성공")
    @Test
    void givenValidEmail_whenValidate_thenWorksFine() {
        // given
        UpdateEmailRequestDto request = new UpdateEmailRequestDto("valid@example.com");

        // when
        Set<ConstraintViolation<UpdateEmailRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @DisplayName("빈 이메일 입력 시 validation 실패")
    @Test
    void givenEmptyEmail_whenValidate_thenFailValidation() {
        // given
        UpdateEmailRequestDto request = new UpdateEmailRequestDto("");

        // when
        Set<ConstraintViolation<UpdateEmailRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("이메일은 필수 입력값입니다.");
    }

    @DisplayName("잘못된 형식의 이메일 입력 시 validation 실패")
    @Test
    void givenInvalidEmail_whenValidate_thenFailValidation() {
        // given
        UpdateEmailRequestDto request = new UpdateEmailRequestDto("invalid-email");

        // when
        Set<ConstraintViolation<UpdateEmailRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("유효한 이메일 주소를 입력하세요.");
    }
}