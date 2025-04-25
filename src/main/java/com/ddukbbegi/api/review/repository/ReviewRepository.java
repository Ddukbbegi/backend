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
