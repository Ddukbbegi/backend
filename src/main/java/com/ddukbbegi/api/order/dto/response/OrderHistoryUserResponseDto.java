package com.ddukbbegi.api.order.dto.response;

import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.entity.OrderMenu;

import java.util.List;

public record OrderHistoryUserResponseDto(
        Long orderId,
        List<MenuItem> orderMenus,
        String status,
        String storeName,
        boolean isReviewed
) {
    public static OrderHistoryUserResponseDto from(Order order, List<OrderMenu> orderMenus, boolean isReviewed) {
        return new OrderHistoryUserResponseDto(
                order.getId(),
                orderMenus.stream().map(MenuItem::from).toList(),
                order.getOrderStatus().name(),
                order.getStore().getName(),
                isReviewed
        );
    }

    public record MenuItem(Long menuId, String menuName, int count) {
        public static MenuItem from(OrderMenu orderMenu) {
            return new MenuItem(
                    orderMenu.getMenu().getId(),
                    orderMenu.getMenu().getName(),
                    orderMenu.getCount()
            );
        }
    }
}