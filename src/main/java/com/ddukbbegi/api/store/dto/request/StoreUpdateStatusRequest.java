package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.constraints.NotNull;

public record StoreUpdateStatusRequest(@NotNull boolean status) {

}
