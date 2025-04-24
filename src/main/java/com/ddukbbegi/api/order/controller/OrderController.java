package com.ddukbbegi.api.order.controller;

import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;
import com.ddukbbegi.api.order.service.OrderService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public BaseResponse<Long> createOrder(@Validated @RequestBody OrderCreateRequestDto request) {
        return BaseResponse.success(orderService.createOrder(request, 1L), ResultCode.OK);
    }
}
