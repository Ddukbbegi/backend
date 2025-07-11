package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.DayOfWeek;
import com.ddukbbegi.api.store.util.TimeRangeFormatter;

import java.util.List;

public record ResponseStoreOperationInfo(
        List<String> closedDays,
        String weekdayWorkingTime,
        String weekdayBreakTime,
        String weekendWorkingTime,
        String weekendBreakTime
) {

    public static ResponseStoreOperationInfo fromEntity(Store store) {
        return new ResponseStoreOperationInfo(
                store.getClosedDays().stream().map(DayOfWeek::name).toList(),
                TimeRangeFormatter.format(store.getWeekdayWorkingStartTime(), store.getWeekdayWorkingEndTime()),
                TimeRangeFormatter.format(store.getWeekdayBreakStartTime(), store.getWeekdayBreakEndTime()),
                TimeRangeFormatter.format(store.getWeekendWorkingStartTime(), store.getWeekendWorkingEndTime()),
                TimeRangeFormatter.format(store.getWeekendBreakStartTime(), store.getWeekendBreakEndTime())
        );
    }

}
