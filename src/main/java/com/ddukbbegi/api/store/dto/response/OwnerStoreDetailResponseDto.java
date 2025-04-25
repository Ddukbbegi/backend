package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;

public record OwnerStoreDetailResponseDto(
        Long storeId,
        StoreBasicInfoResponse basicInfo,
        StoreOperationInfoResponse operationInfo,
        StoreOrderSettingsInfoResponse orderSettings,
        StoreStatus status,
        boolean isTemporarilyClosed,
        boolean isPermanentlyClosed
) {

    public static OwnerStoreDetailResponseDto fromEntity(Store store) {
        return new OwnerStoreDetailResponseDto(
                store.getId(),
                StoreBasicInfoResponse.fromEntity(store),
                StoreOperationInfoResponse.fromEntity(store),
                StoreOrderSettingsInfoResponse.fromEntity(store),
                store.getStatus(),
                store.isTemporarilyClosed(),
                store.isPermanentlyClosed()
        );
    }

}
