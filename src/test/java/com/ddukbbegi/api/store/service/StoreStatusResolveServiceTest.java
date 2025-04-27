package com.ddukbbegi.api.store.service;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StoreStatusResolveServiceTest {

    private final StoreStatusResolveService service = new StoreStatusResolveService();

    @DisplayName("가게 상태 판단 테스트")
    @ParameterizedTest(name = "{index}: 현재시간 {1} => 예상 상태: {2}")
    @MethodSource("provideStoreStatusTestCases")
    void resolveStoreStatus_worksAsExpected(Store store, LocalDateTime now, StoreStatus expectedStatus) {
        StoreStatus result = service.resolveStoreStatus(store, now);
        assertThat(result).isEqualTo(expectedStatus);
    }

    private static Stream<Arguments> provideStoreStatusTestCases() {
        return Stream.of(
                // OPEN 시간 (평일)
                Arguments.of(mockStore(false, false), LocalDateTime.of(2025, 4, 24, 11, 0), StoreStatus.OPEN),
                // CLOSED (영업 종료 후)
                Arguments.of(mockStore(false, false), LocalDateTime.of(2025, 4, 24, 23, 0), StoreStatus.CLOSED),
                // BREAK 시간
                Arguments.of(mockStore(false, false), LocalDateTime.of(2025, 4, 24, 13, 30), StoreStatus.BREAK),
                // 자정 넘는 OPEN (01:00)
                Arguments.of(mockStore(false, false), LocalDateTime.of(2025, 4, 27, 1, 0), StoreStatus.OPEN),
                // PERMANENTLY_CLOSED
                Arguments.of(mockStore(true, false), LocalDateTime.of(2025, 4, 24, 12, 0), StoreStatus.PERMANENTLY_CLOSED),
                // TEMPORARILY_CLOSED
                Arguments.of(mockStore(false, true), LocalDateTime.of(2025, 4, 24, 12, 0), StoreStatus.TEMPORARILY_CLOSED)
        );
    }

    private static Store mockStore(boolean isPermanentlyClosed, boolean isTemporarilyClosed) {
        return new Store() {
            @Override public boolean isPermanentlyClosed() { return isPermanentlyClosed; }
            @Override public boolean isTemporarilyClosed() { return isTemporarilyClosed; }

            // 평일 (10:00-22:00, 13:00-14:00)
            @Override public LocalTime getWeekdayWorkingStartTime() { return LocalTime.of(10, 0); }
            @Override public LocalTime getWeekdayWorkingEndTime() { return LocalTime.of(22, 0); }
            @Override public LocalTime getWeekdayBreakStartTime() { return LocalTime.of(13, 0); }
            @Override public LocalTime getWeekdayBreakEndTime() { return LocalTime.of(14, 0); }

            // 주말 - 자정 넘는 영업 (23:00-02:00, 00:30-01:00)
            @Override public LocalTime getWeekendWorkingStartTime() { return LocalTime.of(23, 0); }
            @Override public LocalTime getWeekendWorkingEndTime() { return LocalTime.of(2, 0); }
            @Override public LocalTime getWeekendBreakStartTime() { return LocalTime.of(0, 30); }
            @Override public LocalTime getWeekendBreakEndTime() { return LocalTime.of(1, 0); }
        };
    }

}
