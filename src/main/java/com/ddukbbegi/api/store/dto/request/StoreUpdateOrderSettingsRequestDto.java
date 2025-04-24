package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreUpdateOrderSettingsRequestDto {

    @NotNull(message = "최소 배달 금액은 필수입니다.")
    @Min(value = 0, message = "최소 배달 금액은 0원 이상이어야 합니다.")
    private Integer minDeliveryPrice;

    @NotNull(message = "배달 팁은 필수입니다.")
    @Min(value = 0, message = "배달 팁은 0원 이상이어야 합니다.")
    private Integer deliveryTip;

}
