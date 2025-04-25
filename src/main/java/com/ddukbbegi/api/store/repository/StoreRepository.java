package com.ddukbbegi.api.store.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends BaseRepository<Store, Long> {

    List<Store> findAllByUser_Id(Long userId);

    @EntityGraph(attributePaths = { "user" })
    @Query("""
        SELECT s
        FROM Store s
        WHERE
            s.name LIKE :name
            AND s.status != 'PERMANENTLY_CLOSED'
        ORDER BY
            CASE WHEN s.status = 'OPEN' THEN 0 ELSE 1 END
    """)
    Page<Store> findAllOpenedStoreByName(String name, Pageable pageable);

    @Query("SELECT COUNT(*) < 3 FROM Store s WHERE s.user.id = :userId AND s.isPermanentlyClosed IS FALSE")
    boolean isStoreRegistrationAvailable(Long userId);

}
