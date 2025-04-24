package com.ddukbbegi.api.store.enums;

import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;

public enum StoreStatus {
    OPEN,                   // 정상 영업 중
    CLOSED,                 // 영업 종료
    BREAK,                  // 휴게 시간
    TEMPORARILY_CLOSED,     // 임시 휴업 (사장이 직접 ON/OFF)
    PERMANENTLY_CLOSED;     // 폐업

    public static StoreStatus fromString(String name) {
        try {
            return StoreStatus.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResultCode.VALID_FAIL, "존재하지 않는 상태명: " + name);
        }
    }
}
