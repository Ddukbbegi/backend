package com.ddukbbegi.api.review.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.dto.ReviewWithLikeCountDto;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.review.entity.ReviewLike;
import com.ddukbbegi.api.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends BaseRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByUserAndReview(User user, Review review);

    boolean existsReviewLikeByUserAndReview(User user, Review review);

    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review.id = :reviewId")
    long countLikesByReviewId(@Param("reviewId") Long reviewId);


    @Query("""
    SELECT new com.ddukbbegi.api.review.dto.ReviewResponseDto(
         r.id,
         r.order.id,
         r.contents,
         r.rate,
         CASE
             WHEN r.anonymousStatus = 'ANONYMOUS' THEN '익명'
             ELSE r.user.email
         END,
         r.reply,
         COUNT(rl.id)
     )
    FROM Review r
    LEFT JOIN ReviewLike rl ON rl.review = r
    WHERE r.user.id = :userId
    GROUP BY r
""")
    Page<ReviewResponseDto> countLikesByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT new com.ddukbbegi.api.review.dto.ReviewResponseDto(
            r.id, 
            r.order.id, 
            r.contents, 
            r.rate, 
            CASE 
                WHEN r.anonymousStatus = 'ANONYMOUS' THEN '익명' 
                ELSE r.user.email 
            END, 
            r.reply, 
            COUNT(rl.id)
        )
    FROM Review r
    LEFT JOIN ReviewLike rl ON rl.review = r
    WHERE r.order.store.id = :storeId
    GROUP BY r
""")
    Page<ReviewResponseDto> countLikesByStoreId(@Param("storeId") Long storeId, Pageable pageable);




}
