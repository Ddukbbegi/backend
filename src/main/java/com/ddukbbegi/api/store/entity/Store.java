package com.ddukbbegi.api.store.entity;

import com.ddukbbegi.api.common.entity.BaseUserEntity;
import com.ddukbbegi.api.store.enums.DayOfWeek;
import com.ddukbbegi.api.store.enums.DayOfWeekListConverter;
import com.ddukbbegi.api.store.enums.StoreCategory;
import com.ddukbbegi.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> closedDays;   // 휴무일 ("SUN,MON"과 같은 형식으로 저장됨)

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
    private Integer minDeliveryPrice;

    @Column(nullable = false)
    private Integer deliveryTip;

    @Column(nullable = false)
    private boolean isTemporarilyClosed = false;

    @Column(nullable = false)
    private boolean isPermanentlyClosed = false;    // 폐업 여부

    @Builder
    public Store(User user,
                 String name,
                 StoreCategory category,
                 String phoneNumber,
                 String description,
                 List<DayOfWeek> closedDays,
                 LocalTime weekdayWorkingStartTime,
                 LocalTime weekdayWorkingEndTime,
                 LocalTime weekdayBreakStartTime,
                 LocalTime weekdayBreakEndTime,
                 LocalTime weekendWorkingStartTime,
                 LocalTime weekendWorkingEndTime,
                 LocalTime weekendBreakStartTime,
                 LocalTime weekendBreakEndTime,
                 Integer minDeliveryPrice,
                 Integer deliveryTip) {
        this.user = user;
        this.name = name;
        this.category = category;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.closedDays = closedDays;
        this.weekdayWorkingStartTime = weekdayWorkingStartTime;
        this.weekdayWorkingEndTime = weekdayWorkingEndTime;
        this.weekdayBreakStartTime = weekdayBreakStartTime;
        this.weekdayBreakEndTime = weekdayBreakEndTime;
        this.weekendWorkingStartTime = weekendWorkingStartTime;
        this.weekendWorkingEndTime = weekendWorkingEndTime;
        this.weekendBreakStartTime = weekendBreakStartTime;
        this.weekendBreakEndTime = weekendBreakEndTime;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
    }

    public void updateBasicInfo(String name,
                                StoreCategory category,
                                String phoneNumber,
                                String description) {
        this.name = name;
        this.category = category;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public void updateOperationInfo(LocalTime weekdayWorkingStartTime,
                                    LocalTime weekdayWorkingEndTime,
                                    LocalTime weekdayBreakStartTime,
                                    LocalTime weekdayBreakEndTime,
                                    LocalTime weekendWorkingStartTime,
                                    LocalTime weekendWorkingEndTime,
                                    LocalTime weekendBreakStartTime,
                                    LocalTime weekendBreakEndTime) {
        this.weekdayWorkingStartTime = weekdayWorkingStartTime;
        this.weekdayWorkingEndTime = weekdayWorkingEndTime;
        this.weekdayBreakStartTime = weekdayBreakStartTime;
        this.weekdayBreakEndTime = weekdayBreakEndTime;
        this.weekendWorkingStartTime = weekendWorkingStartTime;
        this.weekendWorkingEndTime = weekendWorkingEndTime;
        this.weekendBreakStartTime = weekendBreakStartTime;
        this.weekendBreakEndTime = weekendBreakEndTime;
    }

    public void updateOrderSettings(Integer minDeliveryPrice, Integer deliveryTip) {
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
    }

    public void updateTemporarilyClosed(boolean status) {
        this.isTemporarilyClosed = status;
    }

    public void updatePermanentlyClosed(boolean status) {
        this.isPermanentlyClosed = status;
    }

}
