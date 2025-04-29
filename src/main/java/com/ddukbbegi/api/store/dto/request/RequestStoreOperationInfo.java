package com.ddukbbegi.api.store.dto.request;

import com.ddukbbegi.api.store.enums.DayOfWeek;
import com.ddukbbegi.api.store.util.TimeRangeParser;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public record RequestStoreOperationInfo(
        @Pattern(
                regexp = "^$|^(MON|TUE|WED|THU|FRI|SAT|SUN)(\\s*,\\s*(MON|TUE|WED|THU|FRI|SAT|SUN)){0,6}$",
                message = "정기 휴무일 형식이 올바르지 않습니다. 예: SUN,MON"
        ) String closedDays,
        @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm-HH:mm 이어야 합니다.") String weekdayWorkingTime,
        @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm-HH:mm 이어야 합니다.") String weekdayBreakTime,
        @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm-HH:mm 이어야 합니다.") String weekendWorkingTime,
        @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm-HH:mm 이어야 합니다.") String weekendBreakTime) {

    /**
     * 내부 파싱 로직을 통해 Entity 빌더에서 사용하기 좋은 자료형으로 변환
     */
    public ParsedOperationInfo toParsedData() {
        List<DayOfWeek> closedDayList;

        if (closedDays == null || closedDays.isBlank()) {
            closedDayList = List.of();
        } else {
            closedDayList = Arrays.stream(closedDays.split(","))
                    .map(DayOfWeek::valueOf)
                    .toList();
        }

        return new ParsedOperationInfo(
                closedDayList,
                TimeRangeParser.parse(weekdayWorkingTime),
                TimeRangeParser.parse(weekdayBreakTime),
                TimeRangeParser.parse(weekendWorkingTime),
                TimeRangeParser.parse(weekendBreakTime)
        );
    }

    @Getter
    @AllArgsConstructor
    public static class ParsedOperationInfo {
        private List<DayOfWeek> closedDays;
        private Pair<LocalTime, LocalTime> weekdayWorkingTime;
        private Pair<LocalTime, LocalTime> weekdayBreakTime;
        private Pair<LocalTime, LocalTime> weekendWorkingTime;
        private Pair<LocalTime, LocalTime> weekendBreakTime;
    }

}
