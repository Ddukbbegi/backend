package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreUpdateOperationInfoRequestDto {

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

}
