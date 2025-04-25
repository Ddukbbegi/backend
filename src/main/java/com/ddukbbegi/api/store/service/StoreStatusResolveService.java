package com.ddukbbegi.api.store.service;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class StoreStatusResolveService {

    public StoreStatus resolveStoreStatus(Store store, LocalDateTime now) {
        
        if (store.isPermanentlyClosed()) {
            return StoreStatus.PERMANENTLY_CLOSED;
        }

        if (store.isTemporarilyClosed()) {
            return StoreStatus.TEMPORARILY_CLOSED;
        }

        DayOfWeek dayOfWeek = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();
        boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY) || (dayOfWeek == DayOfWeek.SUNDAY);

        LocalTime openTime = isWeekend ? store.getWeekendWorkingStartTime() : store.getWeekdayWorkingStartTime();
        LocalTime closeTime = isWeekend ? store.getWeekendWorkingEndTime() : store.getWeekdayWorkingEndTime();
        LocalTime breakStart = isWeekend ? store.getWeekendBreakStartTime() : store.getWeekdayBreakStartTime();
        LocalTime breakEnd = isWeekend ? store.getWeekendBreakEndTime() : store.getWeekdayBreakEndTime();


        if (isWithin(time, breakStart, breakEnd)) {
            return StoreStatus.BREAK;
        }

        if (isWithin(time, openTime, closeTime)) {
            return StoreStatus.OPEN;
        }

        return StoreStatus.CLOSED;
    }

    private boolean isWithin(LocalTime now, LocalTime start, LocalTime end) {
        if (start.isBefore(end)) {
            return now.isAfter(start) && now.isBefore(end);
        } else {
            // 자정을 넘긴 시간 범위
            return now.isAfter(start) || now.isBefore(end);
        }
    }

}
