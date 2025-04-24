package com.ddukbbegi.api.order.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends BaseRepository<Order, Long> {
    Page<Order> findAllByUserId(long userId, Pageable pageable);

    @Query(
            value = "SELECT o FROM Order o JOIN FETCH o.user WHERE o.store.id = :storeId",
            countQuery = "SELECT count(o) FROM Order o WHERE o.store.id = :storeId"
    )
    Page<Order> findAllByStoreId(@Param(value = "storeId") long storeId, Pageable pageable);
}
