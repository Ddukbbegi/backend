package com.ddukbbegi.api.point.repository;

import com.ddukbbegi.api.point.entity.PointUsage;
import com.ddukbbegi.api.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointUsageRepository extends JpaRepository<PointUsage, Long> {

    Page<PointUsage> findAllByUser_Id(Long userId, Pageable pageable);

}
