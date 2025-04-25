package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;

public record StoreBasicInfoResponse(
        String name,
        String category,
        String phoneNumber,
        String description
) {

    public static StoreBasicInfoResponse fromEntity(Store store) {
        return new StoreBasicInfoResponse(
                store.getName(),
                store.getCategory().name(),
                store.getPhoneNumber(),
                store.getDescription()
        );
    }

}
