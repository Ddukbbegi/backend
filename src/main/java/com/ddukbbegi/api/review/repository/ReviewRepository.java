package com.ddukbbegi.api.review.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends BaseRepository<Review, Long> {


    @Query("select r from Review r join fetch r.user where r.id = :reviewId")
    Optional<Review> findByIdWithUser(@Param("reviewId") Long reviewId);


//        todo: 리뷰 엔티티에 주문 참조되면 주석 해제
//        @Query("SELECT r.order.id FROM Review r WHERE r.order.id IN :orderIds")
//        List<Long> findReviewedOrderIds(@Param("orderIds") List<Long> orderIds);

    default Review findReviewByIdWithUser(Long reviewId){
        return findByIdWithUser(reviewId).orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
    }


//    @Query("""
//    SELECT new com.ddukbbegi.api.review.dto.ReviewResponseDto(
//         r.id,
//         r.order.id,
//         r.contents,
//         r.rate,
//         CASE
//             WHEN r.anonymousStatus = 'ANONYMOUS' THEN '익명'
//             ELSE r.user.email
//         END,
//         r.reply,
//         COUNT(rl.id)
//     )
//    FROM Review r
//    LEFT JOIN ReviewLike rl ON rl.review = r
//    WHERE r.user.id = :userId
//    GROUP BY r
//""")
//
//    Page<ReviewResponseDto> countLikesByUserId(@Param("userId") Long userId, Pageable pageable);

//    @Query("""
//    SELECT new com.ddukbbegi.api.review.dto.ReviewResponseDto(
//            r.id,
//            r.order.id,
//            r.contents,
//            r.rate,
//            CASE
//                WHEN r.anonymousStatus = 'ANONYMOUS' THEN '익명'
//                ELSE r.user.email
//            END,
//            r.reply,
//            COUNT(rl.id)
//        )
//    FROM Review r
//    LEFT JOIN ReviewLike rl ON rl.review = r
//    WHERE r.order.store.id = :storeId
//    GROUP BY r
//""")
//    Page<ReviewResponseDto> countLikesByStoreId(@Param("storeId") Long storeId, Pageable pageable);


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
                    r.likeCount
                    ) FROM Review r WHERE r.user.id = :userId
""")
    Page<ReviewResponseDto> findReviewsByUserId(@Param("userId") Long userId, Pageable pageable);


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
                r.likeCount
            )
    FROM Review r
    LEFT JOIN r.order o
    WHERE o.store.id = :storeId
""")
    Page<ReviewResponseDto> findReviewsByStoreId(@Param("storeId") Long storeId, Pageable pageable);

    @Query(
            value = """
        SELECT
            COALESCE(rc.count, 0) AS rating_count
        FROM (
            SELECT 0 AS star UNION ALL
            SELECT 1 UNION ALL
            SELECT 2 UNION ALL
            SELECT 3 UNION ALL
            SELECT 4 UNION ALL
            SELECT 5
        ) s
        LEFT JOIN (
            SELECT FLOOR(r.rate) AS star, COUNT(*) AS count
            FROM reviews r
            JOIN orders o ON r.order_id = o.id
            WHERE o.store_id = :storeId
            GROUP BY FLOOR(r.rate)
        ) rc ON s.star = rc.star
        ORDER BY s.star
        """,
            nativeQuery = true
    )
    List<Long> getRatingCountsByStar(@Param("storeId") Long storeId);

    @Query(value = """
    SELECT ROUND(AVG(r.rate), 2)
    FROM reviews r
    JOIN (
        SELECT id FROM orders WHERE store_id = :storeId
    ) o ON r.order_id = o.id
    """, nativeQuery = true)
    Float getAverageRatingByStoreId(@Param("storeId") Long storeId);



}
