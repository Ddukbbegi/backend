package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;

public record StoreDetailResponseDto(
        Long storeId,
        StoreBasicInfoResponse basicInfo,
        StoreOperationInfoResponse operationInfo,
        StoreOrderSettingsInfoResponse orderSettings,
        StoreStatus status
) {

    public static StoreDetailResponseDto fromEntity(Store store) {
        return new StoreDetailResponseDto(
                store.getId(),
                StoreBasicInfoResponse.fromEntity(store),
                StoreOperationInfoResponse.fromEntity(store),
                StoreOrderSettingsInfoResponse.fromEntity(store),
                store.getStatus()
        );
    }

}
