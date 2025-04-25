package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;

public record StoreDetailResponseDto(
        Long storeId,
        ResponseStoreBasicInfo basicInfo,
        ResponseStoreOperationInfo operationInfo,
        ResponseStoreOrderSettingsInfo orderSettings,
        StoreStatus status
) {

    public static StoreDetailResponseDto fromEntity(Store store) {
        return new StoreDetailResponseDto(
                store.getId(),
                ResponseStoreBasicInfo.fromEntity(store),
                ResponseStoreOperationInfo.fromEntity(store),
                ResponseStoreOrderSettingsInfo.fromEntity(store),
                store.getStatus()
        );
    }

}
