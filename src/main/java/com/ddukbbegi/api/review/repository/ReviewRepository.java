package com.ddukbbegi.api.review.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends BaseRepository<Review, Long> {


    Page<Review> findAllByUser(User user, Pageable pageable);

    @Query("select r from Review r join fetch r.user where r.id = :reviewId")
    Optional<Review> findByIdWithUser(@Param("reviewId") Long reviewId);

    @Query("SELECT r.order.id FROM Review r WHERE r.order.id IN :orderIds")
    List<Long> findReviewedOrderIds(@Param("orderIds") List<Long> orderIds);
}
