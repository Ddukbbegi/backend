package com.ddukbbegi.api.review.service;


import com.ddukbbegi.api.review.dto.ReviewRequestDto;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    //private final OrderRepository orderRepository;

    public ReviewResponseDto saveReview(Long userId, ReviewRequestDto requestDto){
        //주문테이블에 주문식별자로 존재 확인

        //dto to entity
        Review review = Review.from(requestDto);
        //리뷰 저장
        Review savedReview = reviewRepository.save(review);
        //리턴
        return ReviewResponseDto.from(savedReview);

    }


}
