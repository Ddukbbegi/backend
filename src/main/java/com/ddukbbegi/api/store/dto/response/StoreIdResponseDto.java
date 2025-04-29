package com.ddukbbegi.api.store.dto.response;

public record StoreIdResponseDto(Long storeId) {

    public static StoreIdResponseDto of(Long id) {
        return new StoreIdResponseDto(id);
    }

}
