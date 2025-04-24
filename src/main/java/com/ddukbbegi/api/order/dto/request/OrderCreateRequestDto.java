package com.ddukbbegi.api.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequestDto(
        @NotEmpty(message = "메뉴 목록은 비어 있을 수 없습니다.")
        List<@Valid MenuOrderDto> menus,

        String requestComment

) {
    public record MenuOrderDto(
            @NotNull(message = "메뉴 ID는 필수입니다.")
            Long menuId,
            @NotNull(message = "메뉴 수량은 필수입니다.")
            Integer count
    ) {}
}