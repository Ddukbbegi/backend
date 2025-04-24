package com.ddukbbegi.api.order.dto.response;

import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.entity.OrderMenu;

import java.util.List;

public record OrderHistoryOwnerResponseDto(
        Long orderId,
        List<MenuItem> orderItems,
        String status,
        String userPhoneNumber
) {
    public static OrderHistoryOwnerResponseDto from(Order order, List<OrderMenu> orderMenus) {
        return new OrderHistoryOwnerResponseDto(
                order.getId(),
                orderMenus.stream()
                        .map(MenuItem::from)
                        .toList(),
                order.getOrderStatus().name(),
                order.getUser().getPhone()
        );
    }

    public record MenuItem(Long menuId, String menuName, int count) {
        public static MenuItem from(OrderMenu om) {
            return new MenuItem(
                    om.getMenu().getId(),
                    om.getMenu().getName(),
                    om.getCount()
            );
        }
    }
}

