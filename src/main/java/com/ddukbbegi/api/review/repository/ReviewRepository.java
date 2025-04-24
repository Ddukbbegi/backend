package com.ddukbbegi.api.review.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends BaseRepository<Review, Long> {


    Page<Review> findAllByUser(User user, Pageable pageable);

    @Query("select r from Review r join fetch r.user where r.id = :reviewId")
    Optional<Review> findByIdWithUser(@Param("reviewId") Long reviewId);
    default Review findReviewByIdWithUser(Long reviewId){
        return findByIdWithUser(reviewId).orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
    }
    Page<Review> findByOrder_Store(@NotNull Store orderStore, Pageable pageable);

}
