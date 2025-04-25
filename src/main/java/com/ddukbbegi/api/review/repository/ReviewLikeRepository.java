package com.ddukbbegi.api.review.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.review.entity.ReviewLike;
import com.ddukbbegi.api.user.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewLikeRepository extends BaseRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByUserAndReview(User user, Review review);

    boolean existsReviewLikeByUserAndReview(User user, Review review);

    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review.id = :reviewId")
    long countLikesByReviewId(@Param("reviewId") Long reviewId);






}
