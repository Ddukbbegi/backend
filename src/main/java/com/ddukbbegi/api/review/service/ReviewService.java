package com.ddukbbegi.api.review.service;


import com.ddukbbegi.api.review.dto.ReviewRequestDto;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.dto.ReviewUpdateRequestDto;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    //private final OrderRepository orderRepository;

    public ReviewResponseDto saveReview(Long userId, ReviewRequestDto requestDto){
        //주문테이블에 주문식별자로 존재 확인
        User user = userRepository.findByIdOrElseThrow(userId);

        //dto to entity
        Review review = Review.from(user, requestDto);
        //리뷰 저장
        Review savedReview = reviewRepository.save(review);
        //리턴
        return ReviewResponseDto.from(savedReview);

    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findAllMyReviews(Long userId, Pageable pageable){

        Page<Review> reviews = reviewRepository.findAllByUserId(userId, pageable);
        return reviews.map(ReviewResponseDto::from);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewUpdateRequestDto requestDto){
        Review findReview = reviewRepository.findByIdWithUser(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        if(!findReview.getUser().getId().equals(userId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReview.updateReview(requestDto);
        return ReviewResponseDto.from(findReview);
    }

    @Transactional
    public void deleteReview(Long reviewId){
        Review findReview = reviewRepository
                .findByIdWithUser(reviewId).orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        if(!findReview.getUser().getId().equals(reviewId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReview.softDelete();
    }


}
