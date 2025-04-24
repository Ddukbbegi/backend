package com.ddukbbegi.api.order.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.order.entity.OrderMenu;

import java.util.List;

public interface OrderMenuRepository extends BaseRepository<OrderMenu, Long> {
    List<OrderMenu> findByOrderIdInWithMenu(List<Long> orderIds);
}
