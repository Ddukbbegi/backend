package com.ddukbbegi.api.order.entity;

import com.ddukbbegi.api.common.entity.BaseUserEntity;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.user.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseUserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

//    @Version
//    private Long version;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Nullable
    private String requestComment;

    @Column(name = "request_id", nullable = false, unique = true)
    private String requestId;

    @Builder
    public Order(User user, Store store, @Nullable String requestComment, String requestId) {
        this.user = user;
        this.store = store;
        this.orderStatus = OrderStatus.WAITING;
        this.requestComment = requestComment;
        this.requestId = requestId;
    }

    @Column(nullable = false)
    @Setter
    private Long totalPrice;


    public void cancel() {
        this.orderStatus = OrderStatus.CANCELED;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }
}
