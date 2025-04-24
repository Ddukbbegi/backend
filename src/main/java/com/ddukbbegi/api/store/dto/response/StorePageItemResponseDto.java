package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;

public record StorePageItemResponseDto(
        String name,
        String description,
        String storeCategory,
        Integer minDeliveryPrice,
        Integer deliveryTip
) {

    public static StorePageItemResponseDto fromEntity(Store store) {

        return new StorePageItemResponseDto(
                store.getName(),
                store.getDescription(),
                store.getCategory().name(),
                store.getMinDeliveryPrice(),
                store.getDeliveryTip()
        );
    }

}
