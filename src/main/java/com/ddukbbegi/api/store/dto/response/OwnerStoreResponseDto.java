package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import com.ddukbbegi.api.store.util.TimeRangeFormatter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record OwnerStoreResponseDto(
        Long storeId,
        String name,
        String category,
        StoreStatus status,

        String breakTime,
        boolean isTemporarilyClosed,
        boolean isPermanentlyClosed
) {

    public static OwnerStoreResponseDto fromEntity(Store store) {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY) || (dayOfWeek == DayOfWeek.SUNDAY);

        LocalTime startTime = isWeekend ? store.getWeekendBreakStartTime() : store.getWeekdayBreakStartTime();
        LocalTime endTime = isWeekend ? store.getWeekendBreakEndTime() : store.getWeekdayBreakEndTime();

        return new OwnerStoreResponseDto(
                store.getId(),
                store.getName(),
                store.getCategory().getDesc(),
                store.getStatus(),
                TimeRangeFormatter.format(startTime, endTime),
                store.isTemporarilyClosed(),
                store.isPermanentlyClosed()
        );
    }

}
