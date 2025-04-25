package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;

public record StoreOrderSettingsInfoResponse(
        Integer minDeliveryPrice,
        Integer deliveryTip
) {
    public static StoreOrderSettingsInfoResponse fromEntity(Store store) {
        return new StoreOrderSettingsInfoResponse(
                store.getMinDeliveryPrice(),
                store.getDeliveryTip()
        );
    }
}
