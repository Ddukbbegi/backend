package com.ddukbbegi.api.order.service;

import com.ddukbbegi.api.order.dto.response.OrderTotalResponseDto;
import com.ddukbbegi.api.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;

    public OrderTotalResponseDto getOrderCount(String date) {

        YearMonth yearMonth = YearMonth.parse(date);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999_999_999);

        Long count = orderRepository.countOrdersByDateRange(startDateTime, endDateTime);

        return OrderTotalResponseDto.from(count);
    }

    public OrderTotalResponseDto getOrderCountByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay(); // 자정
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59, 999999999); // 끝 시간

        Long count = orderRepository.countOrdersByDateRange(startOfDay, endOfDay);

        return OrderTotalResponseDto.from(count);
    }
}
