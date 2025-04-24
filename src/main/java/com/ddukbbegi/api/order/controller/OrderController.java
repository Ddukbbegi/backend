package com.ddukbbegi.api.order.controller;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;
import com.ddukbbegi.api.order.dto.response.OrderHistoryOwnerResponseDto;
import com.ddukbbegi.api.order.dto.response.OrderHistoryUserResponseDto;
import com.ddukbbegi.api.order.service.OrderService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public BaseResponse<Long> createOrder(@Validated @RequestBody OrderCreateRequestDto request) {
        return BaseResponse.success(orderService.createOrder(request, 1L), ResultCode.OK);
    }

    @GetMapping("/orders")
    public BaseResponse<PageResponseDto<OrderHistoryUserResponseDto>> getMyOrders(Pageable pageable) {
        long userId = 1L;
        PageResponseDto<OrderHistoryUserResponseDto> result = orderService.getOrdersForUser(userId, pageable);
        return BaseResponse.success(result, ResultCode.OK);
    }

    @GetMapping("/owner/store/{storeId}/orders")
    public BaseResponse<PageResponseDto<OrderHistoryOwnerResponseDto>> getOrdersForOwner(@PathVariable long storeId, Pageable pageable) {
        long userId = 2L;
        PageResponseDto<OrderHistoryOwnerResponseDto> result = orderService.getOrdersForOwner(userId, pageable);
        return BaseResponse.success(result, ResultCode.OK);
    }

}
