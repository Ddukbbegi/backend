package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.Valid;

public record StoreUpdateOrderSettingsRequestDto(@Valid RequestStoreOrderSettings orderSettingsInfo) {

}
