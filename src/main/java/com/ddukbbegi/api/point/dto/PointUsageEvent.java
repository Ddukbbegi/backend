package com.ddukbbegi.api.point.dto;

import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PointUsageEvent {

    private User user;
    private Order order;
    private Long price;

}
