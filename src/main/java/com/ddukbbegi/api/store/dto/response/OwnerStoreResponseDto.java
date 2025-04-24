package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerStoreResponseDto {

    private Long storeId;
    private String name;
    private String category;
    private StoreStatus status;
    private boolean isTemporarilyClosed;
    private boolean isPermanentlyClosed;

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
