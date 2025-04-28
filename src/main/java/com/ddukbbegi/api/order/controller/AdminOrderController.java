package com.ddukbbegi.api.order.controller;

import com.ddukbbegi.api.order.dto.response.OrderTotalResponseDto;
import com.ddukbbegi.api.order.service.AdminOrderService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping("/orderCount/month")
    public BaseResponse<OrderTotalResponseDto> getOrderCount(@RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") String date) {
        return BaseResponse.success(adminOrderService.getOrderCount(date), ResultCode.OK);
    }

    @GetMapping("/orderCount/date")
    public BaseResponse<OrderTotalResponseDto> getOrderCountByDate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
        return BaseResponse.success(adminOrderService.getOrderCountByDate(date), ResultCode.OK);
    }
}
