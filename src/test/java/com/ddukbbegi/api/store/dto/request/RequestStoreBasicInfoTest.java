package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RequestStoreBasicInfoTest extends AbstractValidatorTest {

    @DisplayName("유효한 데이터 입력 시 validation 성공")
    @Test
    void givenValidData_whenValidate_thenWorksFine() {
        // given
        RequestStoreBasicInfo request = new RequestStoreBasicInfo(
                "맛있는김밥",
                "한식",
                "010-1234-5678",
                "신선한 재료로 매일 준비합니다."
        );

        // when
        Set<ConstraintViolation<RequestStoreBasicInfo>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @DisplayName("유효하지 않은 전화번호 입력 시 validation 실패")
    @Test
    void givenInvalidPhoneNumber_whenValid_thenFailValidation() {
        // given
        RequestStoreBasicInfo request = new RequestStoreBasicInfo(
                "맛있는김밥",
                "한식",
                "010-1234-578asd",
                "신선한 재료로 매일 준비합니다."
        );

        // when
        Set<ConstraintViolation<RequestStoreBasicInfo>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678 또는 02-123-4567");
    }

}
