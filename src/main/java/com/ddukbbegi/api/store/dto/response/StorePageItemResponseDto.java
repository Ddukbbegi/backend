package com.ddukbbegi.api.store.dto.response;

import com.ddukbbegi.api.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorePageItemResponseDto {

    private String name;
    private String description;
    private String storeCategory;
    private Integer minDeliveryPrice;
    private Integer deliveryTip;

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
