package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreUpdateOrderSettingsRequestDto {

    @Valid
    private StoreOrderSettingsInfo orderSettingsInfo;

}
