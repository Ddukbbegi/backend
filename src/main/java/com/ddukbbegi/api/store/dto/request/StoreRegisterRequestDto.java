package com.ddukbbegi.api.store.dto.request;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.DayOfWeek;
import com.ddukbbegi.api.store.enums.StoreCategory;
import com.ddukbbegi.api.store.util.TimeRangeParser;
import com.ddukbbegi.api.user.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class StoreRegisterRequestDto {

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    private String category;

    @Pattern(
            regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
            message = "전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678 또는 02-123-4567"
    )
    private String phoneNumber;

    @NotBlank(message = "가게 설명을 입력해주세요.")
    @Size(max = 1000, message = "설명은 최대 1000자까지 입력할 수 있습니다.")
    private String description;

    @Pattern(
            regexp = "^(MON|TUE|WED|THU|FRI|SAT|SUN)(,(MON|TUE|WED|THU|FRI|SAT|SUN)){0,6}$",
            message = "정기 휴무일 형식이 올바르지 않습니다. 예: SUN,MON"
    )
    private String closedDays;

    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm-HH:mm 이어야 합니다.")
    private String weekdayWorkingTime;

    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm-HH:mm 이어야 합니다.")
    private String weekdayBreakTime;

    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm-HH:mm 이어야 합니다.")
    private String weekendWorkingTime;

    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm-HH:mm 이어야 합니다.")
    private String weekendBreakTime;

    @NotNull(message = "최소 배달 금액은 필수입니다.")
    @Min(value = 0, message = "최소 배달 금액은 0원 이상이어야 합니다.")
    private Integer minDeliveryPrice;

    @NotNull(message = "배달 팁은 필수입니다.")
    @Min(value = 0, message = "배달 팁은 0원 이상이어야 합니다.")
    private Integer deliveryTip;

    public Store toEntity(User user) {

        List<DayOfWeek> closedDaysList = Arrays.stream(this.closedDays.split(","))
                .map(DayOfWeek::valueOf)
                .toList();

        Pair<LocalTime, LocalTime> weekdayWorkingTime = TimeRangeParser.parse(this.weekdayWorkingTime);
        Pair<LocalTime, LocalTime> weekdayBreakTime = TimeRangeParser.parse(this.weekdayBreakTime);
        Pair<LocalTime, LocalTime> weekendWorkingTime = TimeRangeParser.parse(this.weekendWorkingTime);
        Pair<LocalTime, LocalTime> weekendBreakTime = TimeRangeParser.parse(this.weekendBreakTime);

        return Store.builder()
                .user(user)
                .name(name)
                .category(StoreCategory.fromString(category))
                .phoneNumber(phoneNumber)
                .description(description)
                .minDeliveryPrice(minDeliveryPrice)
                .deliveryTip(deliveryTip)
                .closedDays(closedDaysList)

                .weekdayWorkingStartTime(weekdayWorkingTime.getFirst())
                .weekdayWorkingEndTime(weekdayWorkingTime.getSecond())
                .weekdayBreakStartTime(weekdayBreakTime.getFirst())
                .weekdayBreakEndTime(weekdayBreakTime.getSecond())
                .weekendWorkingStartTime(weekendWorkingTime.getFirst())
                .weekendWorkingEndTime(weekendWorkingTime.getSecond())
                .weekendBreakStartTime(weekendBreakTime.getFirst())
                .weekendBreakEndTime(weekendBreakTime.getSecond())

                .build();
    }

}
