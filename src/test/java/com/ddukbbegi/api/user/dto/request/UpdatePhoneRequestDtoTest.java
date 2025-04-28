package com.ddukbbegi.api.user.dto.request;

import com.ddukbbegi.api.store.dto.request.AbstractValidatorTest;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UpdatePhoneRequestDtoTest extends AbstractValidatorTest {

    @DisplayName("성공 - 유효한 전화번호 입력 시 validation 성공")
    @Test
    void givenValidPhone_whenValidate_thenWorksFine() {
        // given
        UpdatePhoneRequestDto request = new UpdatePhoneRequestDto("010-1234-5678");

        // when
        Set<ConstraintViolation<UpdatePhoneRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @DisplayName("실패 - 잘못된 전화번호 형식 입력 시 validation 실패")
    @Test
    void givenInvalidPhoneFormat_whenValidate_thenFailValidation() {
        // given
        UpdatePhoneRequestDto request = new UpdatePhoneRequestDto("010-12345-6789");

        // when
        Set<ConstraintViolation<UpdatePhoneRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("전화번호 형식이 올바르지 않습니다.");
    }

    @DisplayName("실패 - 올바르지 않은 전화번호 형식 입력 시 validation 실패")
    @Test
    void givenWrongPhoneFormat_whenValidate_thenFailValidation() {
        // given
        UpdatePhoneRequestDto request = new UpdatePhoneRequestDto("123-123-1234");

        // when
        Set<ConstraintViolation<UpdatePhoneRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("전화번호 형식이 올바르지 않습니다.");
    }
}