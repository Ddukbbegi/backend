package com.ddukbbegi.api.order.entity;

import com.ddukbbegi.api.common.entity.BaseUserEntity;
import com.ddukbbegi.api.menu.entity.Menu;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders_menus")
public class OrderMenu extends BaseUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @NotNull
    private int count;

    @Builder
    public OrderMenu(Order order, Menu menu, int count) {
        this.order = order;
        this.menu = menu;
        this.count = count;
    }
}
