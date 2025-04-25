package com.ddukbbegi.api.store.dto.request;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.util.Pair;

import java.time.LocalTime;

public record StoreRegisterRequestDto(
        @Valid RequestStoreBasicInfo basicInfoDto,
        @Valid RequestStoreOperationInfo operationInfo,
        @Valid RequestStoreOrderSettings orderSettingsInfo
) {

    public Store toEntity(User user) {

        RequestStoreOperationInfo.ParsedOperationInfo parsedOperationInfo = operationInfo.toParsedData();

        Pair<LocalTime, LocalTime> weekdayWorkingTime = parsedOperationInfo.getWeekdayWorkingTime();
        Pair<LocalTime, LocalTime> weekdayBreakTime = parsedOperationInfo.getWeekdayBreakTime();
        Pair<LocalTime, LocalTime> weekendWorkingTime = parsedOperationInfo.getWeekendWorkingTime();
        Pair<LocalTime, LocalTime> weekendBreakTime = parsedOperationInfo.getWeekendBreakTime();

        return Store.builder()
                .user(user)
                .name(basicInfoDto.name())
                .category(basicInfoDto.getCategory())
                .phoneNumber(basicInfoDto.phoneNumber())
                .description(basicInfoDto.description())

                .closedDays(parsedOperationInfo.getClosedDays())
                .weekdayWorkingStartTime(weekdayWorkingTime.getFirst())
                .weekdayWorkingEndTime(weekdayWorkingTime.getSecond())
                .weekdayBreakStartTime(weekdayBreakTime.getFirst())
                .weekdayBreakEndTime(weekdayBreakTime.getSecond())
                .weekendWorkingStartTime(weekendWorkingTime.getFirst())
                .weekendWorkingEndTime(weekendWorkingTime.getSecond())
                .weekendBreakStartTime(weekendBreakTime.getFirst())
                .weekendBreakEndTime(weekendBreakTime.getSecond())

                .minDeliveryPrice(orderSettingsInfo.minDeliveryPrice())
                .deliveryTip(orderSettingsInfo.deliveryTip())

                .build();
    }

}
