package com.ddukbbegi.api.review.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.review.entity.Reviews;
import com.ddukbbegi.api.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends BaseRepository<Reviews, Long> {


    Page<Reviews> findAllByUser(User user, Pageable pageable);

    @Query("select r from Reviews r join fetch r.user where r.id = :reviewId")
    Optional<Reviews> findByIdWithUser(@Param("reviewId") Long reviewId);
}
