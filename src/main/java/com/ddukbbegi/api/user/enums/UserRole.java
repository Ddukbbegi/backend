package com.ddukbbegi.api.user.enums;

import com.ddukbbegi.common.exception.BusinessException;

import java.util.Arrays;

import static com.ddukbbegi.common.component.ResultCode.VALID_FAIL;

/**
 * User 권한에 대한 Enum Class
 */

public enum UserRole {
    USER, OWNER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new BusinessException(VALID_FAIL, "[USER], [OWNER] 값만 가능합니다."));
    }
}
