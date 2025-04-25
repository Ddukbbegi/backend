package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;

public record ResponseStoreOrderSettingsInfo(
        Integer minDeliveryPrice,
        Integer deliveryTip
) {
    public static ResponseStoreOrderSettingsInfo fromEntity(Store store) {
        return new ResponseStoreOrderSettingsInfo(
                store.getMinDeliveryPrice(),
                store.getDeliveryTip()
        );
    }
}
