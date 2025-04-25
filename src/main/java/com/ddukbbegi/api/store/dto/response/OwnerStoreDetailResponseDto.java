package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;

public record OwnerStoreDetailResponseDto(
        Long storeId,
        ResponseStoreBasicInfo basicInfo,
        ResponseStoreOperationInfo operationInfo,
        ResponseStoreOrderSettingsInfo orderSettings,
        StoreStatus status,
        boolean isTemporarilyClosed,
        boolean isPermanentlyClosed
) {

    public static OwnerStoreDetailResponseDto fromEntity(Store store) {
        return new OwnerStoreDetailResponseDto(
                store.getId(),
                ResponseStoreBasicInfo.fromEntity(store),
                ResponseStoreOperationInfo.fromEntity(store),
                ResponseStoreOrderSettingsInfo.fromEntity(store),
                store.getStatus(),
                store.isTemporarilyClosed(),
                store.isPermanentlyClosed()
        );
    }

}
