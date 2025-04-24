package com.ddukbbegi.api.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreRegisterAvailableResponseDto {

    private boolean isAvailable;

    public static StoreRegisterAvailableResponseDto of(boolean isAvailable) {
        return new StoreRegisterAvailableResponseDto(isAvailable);
    }
}
