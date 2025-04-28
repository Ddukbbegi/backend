package com.ddukbbegi.api.point.entity;


import com.ddukbbegi.api.common.entity.BaseTimeEntity;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "point_usages")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PointUsage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long usageHistory;
}
