package com.ddukbbegi.api.store.enums;

public enum StoreStatus {
    OPEN,                   // 정상 영업 중
    CLOSED,                 // 영업 종료
    TEMPORARILY_CLOSED,     // 임시 휴업 (휴게시간 포함)
    PERMANENTLY_CLOSED      // 폐업
}
