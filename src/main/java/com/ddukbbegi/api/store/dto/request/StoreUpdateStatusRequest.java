package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreUpdateStatusRequest {

    @NotNull
    private boolean status;

}
