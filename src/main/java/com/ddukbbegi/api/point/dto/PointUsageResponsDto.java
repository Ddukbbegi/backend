package com.ddukbbegi.api.point.dto;

import com.ddukbbegi.api.point.entity.PointUsage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PointUsageResponsDto {
    private Long userId;
    private Long orderId;
    private Long usage;
    private LocalDateTime createAt;

    public static PointUsageResponsDto from(PointUsage pointUsage){
        return PointUsageResponsDto.builder()
                .userId(pointUsage.getUser().getId())
                .orderId(pointUsage.getOrder().getId())
                .usage(pointUsage.getUsage())
                .createAt(pointUsage.getCreatedAt())
                .build();
    }
}
