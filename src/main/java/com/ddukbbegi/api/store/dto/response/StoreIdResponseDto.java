package com.ddukbbegi.api.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreIdResponseDto {

    private Long storeId;

    public static StoreIdResponseDto of(Long id) {
        return new StoreIdResponseDto(id);
    }

}
