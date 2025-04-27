package com.ddukbbegi.api.order.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.common.exception.BusinessException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
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

    boolean existsByRequestId(String requestId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000"))
    @Query("SELECT o FROM Order o JOIN FETCH o.store WHERE o.id = :orderId")
    Optional<Order> findByIdWithStoreForUpdate(@Param(value = "orderId") Long orderId);

    default Order findByIdWithStoreForUpdateOrElseThrow(Long orderId) {
        return findByIdWithStoreForUpdate(orderId)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000"))
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> findByIdForUpdate(@Param(value = "orderId") long orderId);

    default Order findByIdForUpdateOrElseThrow(Long orderId) {
        return findByIdForUpdate(orderId)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }
}
