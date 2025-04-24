package com.ddukbbegi.api.review.service;


import com.ddukbbegi.api.review.dto.ReviewOwnerRequestDto;
import com.ddukbbegi.api.review.dto.ReviewRequestDto;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.dto.ReviewUpdateRequestDto;
import com.ddukbbegi.api.review.entity.Reviews;
import com.ddukbbegi.api.review.entity.ReviewLikes;
import com.ddukbbegi.api.review.repository.ReviewLikeRepository;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
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
    private final ReviewLikeRepository reviewLikeRepository;
    //private final OrderRepository orderRepository;

    public ReviewResponseDto saveReview(Long userId, ReviewRequestDto requestDto){
        //주문테이블에 주문식별자로 존재 확인
        User user = userRepository.findByIdOrElseThrow(userId);

        //dto to entity
        Reviews reviews = Reviews.from(user, requestDto);
        //리뷰 저장
        Reviews savedReviews = reviewRepository.save(reviews);
        //리턴
        return ReviewResponseDto.from(savedReviews);

    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findAllMyReviews(Long userId, Pageable pageable){
        User finduser = userRepository.findByIdOrElseThrow(userId);
        Page<Reviews> reviews = reviewRepository.findAllByUser(finduser,pageable);
        return reviews.map(ReviewResponseDto::from);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewUpdateRequestDto requestDto){
        Reviews findReviews = reviewRepository.findByIdWithUser(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        if(!findReviews.getUser().getId().equals(userId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReviews.updateReview(requestDto);
        return ReviewResponseDto.from(findReviews);
    }

    @Transactional
    public void deleteReview(Long reviewId){
        Reviews findReviews = reviewRepository
                .findByIdWithUser(reviewId).orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        if(!findReviews.getUser().getId().equals(reviewId)){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        findReviews.softDelete();
    }

    @Transactional
    public ReviewResponseDto saveReviewReply(Long ownerId , Long reviewId, ReviewOwnerRequestDto requestDto){
        User findUser = userRepository.findByIdOrElseThrow(ownerId);
        if(findUser.getUserRole() != UserRole.OWNER){
            System.out.println(findUser.getUserRole());
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        Reviews findReviews = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        findReviews.updateReply(requestDto.getContents());
        return ReviewResponseDto.from(findReviews);
    }


    @Transactional
    public ReviewResponseDto updateReviewReply(Long ownerId , Long reviewId, ReviewOwnerRequestDto requestDto){
        User findUser = userRepository.findByIdOrElseThrow(ownerId);
        if(findUser.getUserRole() != UserRole.OWNER){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        Reviews findReviews = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        findReviews.updateReply(requestDto.getContents());
        return ReviewResponseDto.from(findReviews);
    }

    @Transactional
    public void deleteReviewReply(Long ownerId, Long reviewId){
        User findUser = userRepository.findByIdOrElseThrow(ownerId);
        if(findUser.getUserRole() != UserRole.OWNER){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        Reviews findReviews = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        findReviews.updateReply(null);
    }

    @Transactional
    public void saveLike(Long userId, Long reviewId){
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Reviews findReviews = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        if(reviewLikeRepository.existsReviewLikeByUserAndReview(findUser, findReviews)){
            return ;
        }
        ReviewLikes reviewLikes = ReviewLikes.from(findReviews, findUser);
        reviewLikeRepository.save(reviewLikes);
    }

    @Transactional
    public void deleteLike(Long userId, Long reviewId){
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Reviews findReviews = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));

        reviewLikeRepository.findByUserAndReview(findUser, findReviews)
                .ifPresentOrElse(
                        reviewLikes -> {
                            reviewLikeRepository.delete(reviewLikes);
                        },
                        () -> {
                            // 없으면 바로 return
                            return;
                        }
                );
    }


}
