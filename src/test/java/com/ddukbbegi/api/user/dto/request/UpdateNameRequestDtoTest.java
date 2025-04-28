package com.ddukbbegi.api.user.dto.request;

import com.ddukbbegi.api.store.dto.request.AbstractValidatorTest;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UpdateNameRequestDtoTest extends AbstractValidatorTest {

    @DisplayName("성공 - 유효한 이름 입력 시 validation 성공")
    @Test
    void givenValidName_whenValidate_thenWorksFine() {
        // given
        UpdateNameRequestDto request = new UpdateNameRequestDto("ValidName");

        // when
        Set<ConstraintViolation<UpdateNameRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @DisplayName("실패 - 빈 이름 입력 시 validation 실패")
    @Test
    void givenEmptyName_whenValidate_thenFailValidation() {
        // given
        UpdateNameRequestDto request = new UpdateNameRequestDto("");

        // when
        Set<ConstraintViolation<UpdateNameRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("이름은 필수 입력값입니다.");
    }

    @DisplayName("실패 - 이름 길이 초과 시 validation 실패")
    @Test
    void givenNameExceedingMaxLength_whenValidate_thenFailValidation() {
        // given
        String longName = "a".repeat(11); // 길이가 11인 이름
        UpdateNameRequestDto request = new UpdateNameRequestDto(longName);

        // when
        Set<ConstraintViolation<UpdateNameRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("이름은 10자 이내로 입력해주세요.");
    }
}