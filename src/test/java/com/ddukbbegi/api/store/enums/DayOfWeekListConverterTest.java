package com.ddukbbegi.api.store.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayOfWeekListConverterTest {

    private final DayOfWeekListConverter converter = new DayOfWeekListConverter();

    @DisplayName("convertToDatabaseColumn 테스트")
    @Nested
    class ConvertToDatabaseColumnTest {

        @DisplayName("정상 변환 - 중복 및 순서 정리된 문자열 반환")
        @Test
        void givenValidInput_whenConvert_thenSuccess() {
            // given
            List<DayOfWeek> list = Arrays.asList(DayOfWeek.SUN, DayOfWeek.MON, DayOfWeek.MON);

            // when
            String result = converter.convertToDatabaseColumn(list);

            // then
            assertThat(result).isEqualTo("MON,SUN");
        }

        @DisplayName("null 입력 시 빈 문자열 반환")
        @Test
        void givenNull_whenConvert_thenReturnEmptyString() {
            // when
            String result = converter.convertToDatabaseColumn(null);

            // then
            assertThat(result).isEqualTo("");
        }

        @DisplayName("빈 리스트 입력 시 빈 문자열 반환")
        @Test
        void givenEmptyList_whenConvert_thenReturnEmptyString() {
            // when
            String result = converter.convertToDatabaseColumn(List.of());

            // then
            assertThat(result).isEqualTo("");
        }

    }

    @DisplayName("convertToEntityAttribute 테스트")
    @Nested
    class ConvertToEntityAttributeTest {

        @DisplayName("정상 변환 - 문자열을 List로 반환")
        @Test
        void givenDbData_whenConvert_thenSuccess() {
            // given
            String input = "SUN,MON";

            // when
            List<DayOfWeek> result = converter.convertToEntityAttribute(input);

            // then
            assertThat(result).hasSize(2);
            assertThat(result)
                    .containsExactly(
                            DayOfWeek.SUN,
                            DayOfWeek.MON
                    );
        }

        @DisplayName("null 입력 시 빈 리스트 반환")
        @Test
        void givenNull_whenConvert_thenReturnEmptyList() {
            // when
            List<DayOfWeek> result = converter.convertToEntityAttribute(null);

            // then
            assertThat(result).isEmpty();
        }

        @DisplayName("빈 리스트 입력 시 빈 리스트 반환")
        @Test
        void givenEmptyString_whenConvert_thenReturnEmptyList() {
            // when
            List<DayOfWeek> result = converter.convertToEntityAttribute(" ");

            // then
            assertThat(result).isEmpty();
        }

    }

}
