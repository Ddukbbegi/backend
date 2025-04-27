package com.ddukbbegi.api.store.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends BaseRepository<Store, Long> {

    /**
     * 특정 OWNER 계정으로 등록된 가게 목록 조회
     * 
     * @param userId    찾으려는 OWNER id
     * @return  조건에 맞는 가게 목록
     */
    List<Store> findAllByUser_Id(Long userId);

    /**
     * 가게 목록 조회
     * - 가게 이름을 기준으로 검색
     * - status=OPEN인 가게들이 상단에 위치하도록 정렬
     * 
     * @param name  가게 이름 검색 키워드
     * @param pageable  page 객체
     * @return 조건에 맞는 가게 목록
     */
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
    Page<Store> findAllStoreByName(String name, Pageable pageable);

    /**
     * 현재 활성화된(isPermanentlyClosed=false)인 가게가 3개 미만인지 판별
     *
     * @param userId    가게를 등록하려는 OWNER 계정 ID
     * @return  가게 추가 등록 가능 여부 (true: 가능, false: 불가능)
     */
    @Query("SELECT COUNT(*) < 3 FROM Store s WHERE s.user.id = :userId AND s.isPermanentlyClosed IS FALSE")
    boolean isStoreRegistrationAvailable(Long userId);

}
