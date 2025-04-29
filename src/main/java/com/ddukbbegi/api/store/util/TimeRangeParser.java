package com.ddukbbegi.api.store.util;

import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import org.springframework.data.util.Pair;

import java.time.LocalTime;

public class TimeRangeParser {

    public static Pair<LocalTime, LocalTime> parse(String range) {

        if (range == null || !range.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d-(?:[01]\\d|2[0-3]):[0-5]\\d$")) {
            throw new BusinessException(ResultCode.VALID_FAIL, "올바르지 않은 시간 범위 형식입니다.");
        }

        String[] parts = range.split("-");
        LocalTime start = LocalTime.parse(parts[0]);
        LocalTime end = LocalTime.parse(parts[1]);

        return Pair.of(start, end);
    }

}
