package com.ddukbbegi.api.store.enums;

import com.ddukbbegi.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreCategoryTest {

    @DisplayName("유효한 카테고리 입력 시 enum 반환")
    @ParameterizedTest(name = "[성공] 입력값: {0}, 기대하는 Enum: {1}")
    @CsvSource({
            "한식, KOREAN",
            "중식, CHINESE",
            "일식, JAPANESE",
            "양식, WESTERN",
            "치킨, CHICKEN",
            "피자, PIZZA",
            "버거, BURGER",
            "분식, SNACK",
            "카페/디저트, CAFE",
            "야식, LATE_NIGHT",
            "도시락/죽, BENTO",
            "아시안, ASIAN",
            "고기/구이, MEAT",
            "퓨전, FUSION",
            "기타, ETC"
    })
    void givenValidCategory_whenFromString_thenSuccess(String input, StoreCategory expected) {
        // when
        StoreCategory result = StoreCategory.fromString(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("유효하지 않은 카테고리명 입력 시 예외 발생")
    @ParameterizedTest(name = "[실패] 잘못된 입력값: {0}")
    @ValueSource(strings = {
            "패스트푸드",
            "이탈리안",
            "한",       // 일부만 입력
            "카페",     // 부분 매칭 (카페/디저트 아님)
            "분식류",
            "도시락",
            "",         // 빈 문자열
            "   ",      // 공백
    })
    void givenInvalidCategory_whenFromString_thenThrowsException(String input) {
        // when & then
        assertThatThrownBy(() -> StoreCategory.fromString(input))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("존재하지 않는 카테고리명");
    }

}
