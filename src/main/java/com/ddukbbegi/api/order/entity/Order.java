package com.ddukbbegi.api.order.entity;

import com.ddukbbegi.api.common.entity.BaseTimeEntity;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String requestComment;

    @Builder
    public Order(User user, Store store, String requestComment) {
        this.user = user;
        this.store = store;
        this.orderStatus = OrderStatus.WAITING;
        this.requestComment = requestComment;
    }
}
