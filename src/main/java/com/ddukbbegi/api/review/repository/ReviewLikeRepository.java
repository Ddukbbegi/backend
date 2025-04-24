package com.ddukbbegi.api.review.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.review.entity.Reviews;
import com.ddukbbegi.api.review.entity.ReviewLikes;
import com.ddukbbegi.api.user.entity.User;

import java.util.Optional;

public interface ReviewLikeRepository extends BaseRepository<ReviewLikes, Long> {

    Optional<ReviewLikes> findByUserAndReview(User user, Reviews reviews);

    boolean existsReviewLikeByUserAndReview(User user, Reviews reviews);
}
