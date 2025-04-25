package com.ddukbbegi.api.store.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeRangeFormatter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatTimeRange(LocalTime startTime, LocalTime endTime) {
        return startTime.format(TIME_FORMATTER) + "-" + endTime.format(TIME_FORMATTER);
    }

}
