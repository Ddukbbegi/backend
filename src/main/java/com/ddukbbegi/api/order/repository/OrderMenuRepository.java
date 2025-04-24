package com.ddukbbegi.api.order.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderMenuRepository extends BaseRepository<OrderMenu, Long> {
    @Query("SELECT om FROM OrderMenu om JOIN FETCH om.menu WHERE om.order.id IN :orderIds")
    List<OrderMenu> findByOrderIdInWithMenu(@Param("orderIds") List<Long> orderIds);
}
