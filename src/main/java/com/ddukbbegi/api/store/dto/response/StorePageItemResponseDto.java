package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;

public record StorePageItemResponseDto(
        String name,
        String description,
        String storeCategory,
        Integer minDeliveryPrice,
        Integer deliveryTip,
        StoreStatus status
) {

    public static StorePageItemResponseDto fromEntity(Store store) {

        return new StorePageItemResponseDto(
                store.getName(),
                store.getDescription(),
                store.getCategory().name(),
                store.getMinDeliveryPrice(),
                store.getDeliveryTip(),
                store.getStatus()
        );
    }

}
