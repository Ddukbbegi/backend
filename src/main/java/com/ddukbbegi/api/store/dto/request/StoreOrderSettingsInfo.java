package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StoreOrderSettingsInfo(
        @NotNull(message = "최소 배달 금액은 필수입니다.")
        @Min(value = 0, message = "최소 배달 금액은 0원 이상이어야 합니다.")
        Integer minDeliveryPrice,

        @NotNull(message = "배달 팁은 필수입니다.")
        @Min(value = 0, message = "배달 팁은 0원 이상이어야 합니다.")
        Integer deliveryTip) {

}
