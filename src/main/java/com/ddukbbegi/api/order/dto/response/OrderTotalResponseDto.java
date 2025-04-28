package com.ddukbbegi.api.order.dto.response;

import lombok.Builder;

@Builder
public record OrderTotalResponseDto(Long totalOrder) {
    public static OrderTotalResponseDto from(Long totalOrder) {
        return OrderTotalResponseDto.builder()
                .totalOrder(totalOrder)
                .build();
    }
}
