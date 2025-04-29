package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;

public record StorePageItemResponseDto(
        Long storeId,
        String name,
        String description,
        String storeCategory,
        Integer minDeliveryPrice,
        Integer deliveryTip,
        StoreStatus status
) {

    public static StorePageItemResponseDto fromEntity(Store store) {

        return new StorePageItemResponseDto(
                store.getId(),
                store.getName(),
                store.getDescription(),
                store.getCategory().getDesc(),
                store.getMinDeliveryPrice(),
                store.getDeliveryTip(),
                store.getStatus()
        );
    }

}
