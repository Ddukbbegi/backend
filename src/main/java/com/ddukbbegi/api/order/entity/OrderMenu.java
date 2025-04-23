package com.ddukbbegi.api.order.entity;

import com.ddukbbegi.api.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private int count;

    @Builder
    public OrderMenu(Order order, Menu menu, int count) {
        this.order = order;
        this.menu = menu;
        this.count = count;
    }
}
