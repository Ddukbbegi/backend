package com.ddukbbegi.support.fixture;

import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class OrderFixture {

    public static OrderCreateRequestDto createOrderCreateRequestDto(List<Long> menuIds) {
        // menuId 당 1~5 랜덤 수량 생성
        List<OrderCreateRequestDto.MenuOrderDto> menus = menuIds.stream()
                .map(id -> new OrderCreateRequestDto.MenuOrderDto(
                        id,
                        ThreadLocalRandom.current().nextInt(1, 6)    // 1 이상 6 미만 → 1~5
                ))
                .toList();

        return new OrderCreateRequestDto(
                menus,
                "문 앞에 놔주세요",
                UUID.randomUUID().toString(),
                false
        );
    }

}
