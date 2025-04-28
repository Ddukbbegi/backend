package com.ddukbbegi.api.store.util;

import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.util.Pair;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeRangeParserTest {

    @DisplayName("문자열 입력받으면 두 개의 시간 데이터를 반환한다.")
    @ParameterizedTest(name = "[성공] 입력값: {0}")
    @CsvSource({
            "00:00-23:59, 00:00, 23:59",
            "12:30-13:45, 12:30, 13:45",
            "01:00-01:01, 01:00, 01:01",
    })
    void givenTimeRangeString_whenParse_thenReturnPairOfLocalTime(String input, String expectedStart, String expectedEnd) {
        // when
        Pair<LocalTime, LocalTime> result = TimeRangeParser.parse(input);

        // then
        assertThat(result.getFirst()).isEqualTo(LocalTime.parse(expectedStart));
        assertThat(result.getSecond()).isEqualTo(LocalTime.parse(expectedEnd));
    }

    @DisplayName("올바르지 않은 시간 범위 문자열 전달받으면 VALID_FAIL 예외가 발생한다.")
    @ParameterizedTest(name = "[실패] 잘못된 입력값: {0}")
    @ValueSource(strings = {
            "26:00-13:00",  // 시간 초과
            "23:60-00:00",  // 분 초과
            "00:00-24:00",  // 24시는 없음 (LocalTime은 23:59까지)
            "12:345-13:00", // 잘못된 분
            "9:00-10:00",   // 잘못된 형식 (한 자리)
            "12:00/13:00",  // 잘못된 구분자
            "abcd-efgh",    // 엉뚱한 문자열
            "",             // 빈 문자열
            "   ",          // 공백
            "12:00-",       // 하나만 존재
            "-13:00",       // 하나만 존재
    })
    void givenInvalidTimeRangeString_whenParse_thenThrowsVALID_FAIL(String input) {
        // when & then
        assertThatThrownBy(() -> TimeRangeParser.parse(input))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ResultCode.STORE_INVALID_TIME_RANGE.getDefaultMessage());
    }

    @DisplayName("null을 입력 받으면 VALID_FAIL 예외가 발생한다.")
    @Test
    void givenNull_whenParse_thenThrowsVALID_FAIL() {
        // given
        String range = null;

        // when & then
        assertThatThrownBy(() -> TimeRangeParser.parse(range))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ResultCode.STORE_INVALID_TIME_RANGE.getDefaultMessage());
    }

}
