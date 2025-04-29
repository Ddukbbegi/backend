package com.ddukbbegi.api.review.service;


import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.review.dto.*;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.review.entity.ReviewLike;
import com.ddukbbegi.api.review.repository.ReviewLikeRepository;
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

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final OrderRepository orderRepository;


    public ReviewResponseDto saveReview(Long userId, ReviewRequestDto requestDto){
        Order findOrder = orderRepository.findByIdOrElseThrow(requestDto.orderId());
        if (findOrder.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new BusinessException(ResultCode.ORDER_NOT_DELIVERED);
        }

        User findUser = userRepository.findByIdOrElseThrow(userId);
        Review review = Review.from(findUser, findOrder,requestDto);

        Review savedReview = reviewRepository.save(review);
        Long likeCount = reviewLikeRepository.countLikesByReviewId(savedReview.getId());
        return ReviewResponseDto.from(savedReview, likeCount);

    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findAllMyReviews(Long userId, Pageable pageable) {


        return reviewRepository.findReviewsByUserId(userId, pageable);
    }



    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findAllStoreReviews(Long storeId, Pageable pageable){


        return reviewRepository.findReviewsByStoreId(storeId, pageable);

    }

    @Transactional
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewUpdateRequestDto requestDto){
        Review findReview = reviewRepository.findReviewByIdWithUser(reviewId);
        if(!findReview.validUser(userId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReview.updateReview(requestDto);
        Long likeCount = reviewLikeRepository.countLikesByReviewId(findReview.getId());
        return ReviewResponseDto.from(findReview, likeCount);
    }

    @Transactional
    public void deleteReview(Long userId, Long reviewId){
        Review findReview = reviewRepository.findReviewByIdWithUser(reviewId);
        if(!findReview.validUser(userId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReview.softDelete();
    }

    @Transactional
    public ReviewResponseDto saveReviewReply(Long ownerId , Long reviewId, ReviewOwnerRequestDto requestDto){
        Review findReview = reviewRepository.findReviewByIdWithUser(reviewId);
        if(!findReview.getOrder().getStore().getUser().getId().equals(ownerId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReview.updateReply(requestDto.contents());
        Long likeCount = reviewLikeRepository.countLikesByReviewId(findReview.getId());
        return ReviewResponseDto.from(findReview, likeCount);
    }



    @Transactional
    public ReviewResponseDto updateReviewReply(Long ownerId , Long reviewId, ReviewOwnerRequestDto requestDto){
        Review findReview = reviewRepository.findReviewByIdWithUser(reviewId);
        if(!findReview.getOrder().getStore().getUser().getId().equals(ownerId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReview.updateReply(requestDto.contents());
        Long likeCount = reviewLikeRepository.countLikesByReviewId(findReview.getId());
        return ReviewResponseDto.from(findReview, likeCount);
    }

    @Transactional
    public void deleteReviewReply(Long ownerId, Long reviewId){
        Review findReview = reviewRepository.findReviewByIdWithUser(reviewId);
        if(!findReview.getOrder().getStore().getUser().getId().equals(ownerId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReview.updateReply(null);
    }

    @Transactional
    public void saveLike(Long userId, Long reviewId){
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Review findReview = reviewRepository.findReviewByIdWithUser(reviewId);
        if(!reviewLikeRepository.existsReviewLikeByUserAndReview(findUser, findReview)){
            ReviewLike reviewLike = ReviewLike.from(findReview, findUser);
            reviewLikeRepository.save(reviewLike);
            findReview.LikeCount();
        }
    }

    @Transactional
    public void deleteLike(Long userId, Long reviewId){
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Review findReview = reviewRepository.findReviewByIdWithUser(reviewId);
        reviewLikeRepository.findByUserAndReview(findUser, findReview)
            .ifPresent(entity -> {
                reviewLikeRepository.delete(entity);
                findReview.notLikeCount();
            });
    }

    @Transactional
    public RatingPerStarResponseDto getStoreRating(Long storeId){
        List<Long> list = reviewRepository.getRatingCountsByStar(storeId);
        Float rate = reviewRepository.getAverageRatingByStoreId(storeId);

        return new RatingPerStarResponseDto(storeId,rate,list);
    }


}