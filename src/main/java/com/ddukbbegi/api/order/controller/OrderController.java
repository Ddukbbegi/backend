package com.ddukbbegi.api.order.controller;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;
import com.ddukbbegi.api.order.dto.response.OrderCreateResponseDto;
import com.ddukbbegi.api.order.dto.response.OrderHistoryOwnerResponseDto;
import com.ddukbbegi.api.order.dto.response.OrderHistoryUserResponseDto;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.order.service.OrderService;
import com.ddukbbegi.api.user.entity.CustomUserDetails;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public BaseResponse<OrderCreateResponseDto> createOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @Validated @RequestBody OrderCreateRequestDto request) {
        return BaseResponse.success(orderService.createOrder(request, userDetails.getUserId()), ResultCode.OK);
    }

    @GetMapping("/orders")
    public BaseResponse<PageResponseDto<OrderHistoryUserResponseDto>> getOrdersForUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                       Pageable pageable) {
        PageResponseDto<OrderHistoryUserResponseDto> result = orderService.getOrdersForUser(userDetails.getUserId(), pageable);
        return BaseResponse.success(result, ResultCode.OK);
    }

    @GetMapping("/owner/stores/{storeId}/orders")
    public BaseResponse<PageResponseDto<OrderHistoryOwnerResponseDto>> getOrdersForOwner(@PathVariable("storeId") long storeId, Pageable pageable) {
        PageResponseDto<OrderHistoryOwnerResponseDto> result = orderService.getOrdersForOwner(storeId, pageable);
        return BaseResponse.success(result, ResultCode.OK);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    public BaseResponse<Void> cancelOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId,userDetails.getUserId());
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/owner/orders/{orderId}")
    public BaseResponse<Void> updateOrderStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable("orderId") Long orderId,
                                                @RequestParam("status")OrderStatus status) {
        orderService.updateOrderStatus(orderId, status, userDetails.getUserId());
        return BaseResponse.success(ResultCode.OK);
    }
}
