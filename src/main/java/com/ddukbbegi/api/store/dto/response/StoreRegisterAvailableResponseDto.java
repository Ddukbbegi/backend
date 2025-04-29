package com.ddukbbegi.api.store.dto.response;

public record StoreRegisterAvailableResponseDto(boolean isAvailable) {

    public static StoreRegisterAvailableResponseDto of(boolean isAvailable) {
        return new StoreRegisterAvailableResponseDto(isAvailable);
    }
}
