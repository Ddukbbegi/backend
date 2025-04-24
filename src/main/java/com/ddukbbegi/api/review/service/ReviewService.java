package com.ddukbbegi.api.review.service;


import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.entity.OrderMenu;
import com.ddukbbegi.api.order.repository.OrderMenuRepository;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.review.dto.ReviewOwnerRequestDto;
import com.ddukbbegi.api.review.dto.ReviewRequestDto;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.dto.ReviewUpdateRequestDto;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.review.entity.ReviewLike;
import com.ddukbbegi.api.review.repository.ReviewLikeRepository;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.repository.StoreRepository;
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
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public ReviewResponseDto saveReview(Long userId, ReviewRequestDto requestDto){
        //주문테이블에 주문식별자로 존재 확인
        Order findOrder = orderRepository.findByIdOrElseThrow(requestDto.orderId());
        User findUser = userRepository.findByIdOrElseThrow(userId);
        //dto to entity
        Review review = Review.from(findUser, findOrder,requestDto);
        //리뷰 저장
        Review savedReview = reviewRepository.save(review);
        //리턴
        return ReviewResponseDto.from(savedReview);

    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findAllMyReviews(Long userId, Pageable pageable){
        User finduser = userRepository.findByIdOrElseThrow(userId);
        Page<Review> reviews = reviewRepository.findAllByUser(finduser,pageable);
        return reviews.map(ReviewResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findAllStoreReviews(Long storeId, Pageable pageable){
        Store findStore = storeRepository.findByIdOrElseThrow(storeId);
        Page<Review> reviews = reviewRepository.findByOrder_Store(findStore, pageable);
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

    @Transactional
    public ReviewResponseDto saveReviewReply(Long ownerId , Long reviewId, ReviewOwnerRequestDto requestDto){
        User findUser = userRepository.findByIdOrElseThrow(ownerId);
        if(findUser.getUserRole() != UserRole.OWNER){
            System.out.println(findUser.getUserRole());
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        findReview.updateReply(requestDto.contents());
        return ReviewResponseDto.from(findReview);
    }


    @Transactional
    public ReviewResponseDto updateReviewReply(Long ownerId , Long reviewId, ReviewOwnerRequestDto requestDto){
        User findUser = userRepository.findByIdOrElseThrow(ownerId);
        if(findUser.getUserRole() != UserRole.OWNER){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        findReview.updateReply(requestDto.contents());
        return ReviewResponseDto.from(findReview);
    }

    @Transactional
    public void deleteReviewReply(Long ownerId, Long reviewId){
        User findUser = userRepository.findByIdOrElseThrow(ownerId);
        if(findUser.getUserRole() != UserRole.OWNER){
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        findReview.updateReply(null);
    }

    @Transactional
    public void saveLike(Long userId, Long reviewId){
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));
        if(reviewLikeRepository.existsReviewLikeByUserAndReview(findUser, findReview)){
            return ;
        }
        ReviewLike reviewLike = ReviewLike.from(findReview, findUser);
        reviewLikeRepository.save(reviewLike);
    }

    @Transactional
    public void deleteLike(Long userId, Long reviewId){
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(()->new BusinessException(ResultCode.NOT_FOUND));

        reviewLikeRepository.findByUserAndReview(findUser, findReview)
                .ifPresentOrElse(
                        reviewLike -> {
                            reviewLikeRepository.delete(reviewLike);
                        },
                        () -> {
                            // 없으면 바로 return
                            return;
                        }
                );
    }


}
