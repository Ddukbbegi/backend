package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;

public record ResponseStoreBasicInfo(
        String name,
        String category,
        String phoneNumber,
        String description
) {

    public static ResponseStoreBasicInfo fromEntity(Store store) {
        return new ResponseStoreBasicInfo(
                store.getName(),
                store.getCategory().name(),
                store.getPhoneNumber(),
                store.getDescription()
        );
    }

}
