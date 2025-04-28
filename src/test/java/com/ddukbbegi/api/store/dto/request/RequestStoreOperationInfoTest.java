package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RequestStoreOperationInfoTest extends AbstractValidatorTest {

    @DisplayName("유효한 데이터 입력 시 validation 성공")
    @Test
    void givenValidData_whenValidate_thenWorksFine() {
        // given
        RequestStoreOperationInfo request = new RequestStoreOperationInfo(
                "SUN,MON",
                "10:00-18:00",
                "13:00-14:00",
                "11:00-17:00",
                "12:00-13:00"
        );

        // when
        Set<ConstraintViolation<RequestStoreOperationInfo>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @DisplayName("유효하지 않은 휴무일 포맷 입력 시 validation 실패")
    @Test
    void givenInvalidClosedDays_whenValidate_thenFailValidation() {
        // given
        RequestStoreOperationInfo request = new RequestStoreOperationInfo(
                "SUN,MONDAY",   // 잘못된 포맷
                "11:00-17:00",
                "13:00-14:00",
                "11:00-17:00",
                "12:00-13:00"
        );

        // when
        Set<ConstraintViolation<RequestStoreOperationInfo>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("정기 휴무일 형식이 올바르지 않습니다. 예: SUN,MON");
    }

    @DisplayName("유효하지 않은 근무시간 포맷 입력 시 validation 실패")
    @Test
    void givenInvalidWorkingTime_whenValidate_thenFailValidation() {
        // given
        RequestStoreOperationInfo request = new RequestStoreOperationInfo(
                "SUN,MON",
                "1000-1800", // 잘못된 포맷
                "13:00-14:00",
                "121:00-17:00",
                "12:00-13:00"
        );

        // when
        Set<ConstraintViolation<RequestStoreOperationInfo>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting("message")
                .contains("시간 형식은 HH:mm-HH:mm 이어야 합니다.");
    }

}
