package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;

public record OwnerStoreResponseDto(
        Long storeId,
        String name,
        String category,
        StoreStatus status,
        boolean isTemporarilyClosed,
        boolean isPermanentlyClosed
) {

    public static OwnerStoreResponseDto fromEntity(Store store) {
        return new OwnerStoreResponseDto(
                store.getId(),
                store.getName(),
                store.getCategory().name(),
                StoreStatus.OPEN,   // TODO: status 연산 로직 필요
                store.isTemporarilyClosed(),
                store.isPermanentlyClosed()
        );
    }

}
