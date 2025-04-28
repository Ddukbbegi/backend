package com.ddukbbegi.api.point.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.point.entity.PointUsage;
import com.ddukbbegi.api.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointUsageRepository extends BaseRepository<PointUsage, Long> {

    Page<PointUsage> findAllByUser_Id(Long userId, Pageable pageable);

    @Query("SELECT SUM(p.usageHistory) FROM PointUsage p WHERE p.user.id = :userId")
    Long sumUsageHistoryByUserId(@Param("userId") Long userId);

}
