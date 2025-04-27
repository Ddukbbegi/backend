package com.ddukbbegi.api.store.enums;

public enum StoreStatus {
    OPEN,                   // 정상 영업 중
    CLOSED,                 // 영업 종료
    BREAK,                  // 휴게 시간
    TEMPORARILY_CLOSED,     // 영업 임시 중지 (사장이 직접 ON/OFF)
    PERMANENTLY_CLOSED      // 폐업
}
