package com.ddukbbegi.api.store.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class TimeRangeFormatterTest {

    @DisplayName("두 개의 시간 데이터를 입력받으면 포맷팅된 문자열을 반환한다.")
    @Test
    void givenTwoLocalTime_whenFormat_thenReturnFormattingString() {
        // given
        LocalTime start1 = LocalTime.of(11, 30);
        LocalTime start2 = LocalTime.of(2, 0);
        LocalTime start3 = LocalTime.of(22, 30);

        LocalTime end1 = LocalTime.of(7, 30);
        LocalTime end2 = LocalTime.of(20, 0);
        LocalTime end3 = LocalTime.of(14, 30);

        // when
        String format1 = TimeRangeFormatter.format(start1, end1);
        String format2 = TimeRangeFormatter.format(start2, end2);
        String format3 = TimeRangeFormatter.format(start3, end3);

        // then
        assertThat(format1).isEqualTo("11:30-07:30");
        assertThat(format2).isEqualTo("02:00-20:00");
        assertThat(format3).isEqualTo("22:30-14:30");
    }

}
