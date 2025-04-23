package com.ddukbbegi.api.store.entity;

import com.ddukbbegi.api.common.entity.BaseUserEntity;
import com.ddukbbegi.api.store.enums.DayOfWeek;
import com.ddukbbegi.api.store.enums.DayOfWeekListConverter;
import com.ddukbbegi.api.store.enums.StoreStatus;
import com.ddukbbegi.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String name;        // 가게 이름

    @Column(nullable = false)
    private String describe;    // 가게 소개

    @Column(nullable = false)
    private Integer minDeliveryPrice;   // 최소주문금액

    @Column(nullable = false)
    private Integer deliveryTip;    // 배달팁

    @Column(nullable = false)
    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> closedDays;   // 휴무일 ("SUN,MON"과 같은 형식으로 저장됨)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus status;     // 가게 영업 상태

    // 평일 영업/휴게 시간
    @Column(nullable = false) private LocalTime weekdayWorkingStartTime;
    @Column(nullable = false) private LocalTime weekdayWorkingEndTime;
    @Column(nullable = false) private LocalTime weekdayBreakStartTime;
    @Column(nullable = false) private LocalTime weekdayBreakEndTime;

    // 주말 영업/휴게 시간
    @Column(nullable = false) private LocalTime weekendWorkingStartTime;
    @Column(nullable = false) private LocalTime weekendWorkingEndTime;
    @Column(nullable = false) private LocalTime weekendBreakStartTime;
    @Column(nullable = false) private LocalTime weekendBreakEndTime;

    @Column(nullable = false)
    private boolean isPermanentlyClosed;    // 폐업 여부

}
