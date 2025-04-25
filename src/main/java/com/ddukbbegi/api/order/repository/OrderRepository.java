package com.ddukbbegi.api.order.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.common.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.ddukbbegi.common.component.ResultCode.ORDER_NOT_FOUND;

public interface OrderRepository extends BaseRepository<Order, Long> {
    @Query(value = """
    SELECT o FROM Order o
    JOIN FETCH o.store
    WHERE o.user.id = :userId
    """,
            countQuery = """
    SELECT COUNT(o) FROM Order o
    WHERE o.user.id = :userId
    """)
    Page<Order> findAllByUserId(@Param(value = "userId") long userId, Pageable pageable);

    @Query(
            value = "SELECT o FROM Order o JOIN FETCH o.user WHERE o.store.id = :storeId",
            countQuery = "SELECT count(o) FROM Order o WHERE o.store.id = :storeId"
    )
    Page<Order> findAllByStoreId(@Param(value = "storeId") long storeId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.store WHERE o.id = :orderId")
    Optional<Order> findByIdWithStore(@Param("orderId") Long orderId);

    default Order findByIdWithStoreOrElseThrow(Long orderId) {
        return findByIdWithStore(orderId)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }
}
