package com.ddukbbegi.api.review.service;

import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.review.dto.*;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.review.entity.ReviewLike;
import com.ddukbbegi.api.review.repository.ReviewLikeRepository;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewLikeRepository reviewLikeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ReviewService reviewService;


    @Test
    void saveReview() {
        Order order = Order.builder().build();
        User user = new User();
        Long likeCount = 0L;
        ReviewRequestDto requestDto =
                new ReviewRequestDto(
                        1L,
                        "맛있어요",
                        4.5f,
                        AnonymousStatus.ANONYMOUS);
        Review savedReview = Review.builder()
                .id(1L)
                .order(order)
                .user(user)
                .rate(requestDto.rate())
                .contents(requestDto.contents())
                .reply(null)
                .anonymousStatus(AnonymousStatus.ANONYMOUS)
                .build();
        Long userId = 1L;

        given(orderRepository.findByIdOrElseThrow(1L))
                .willReturn(order);
        given(userRepository.findByIdOrElseThrow(userId))
                .willReturn(user);
        // 여기서 Review.from()은 stub하지 않고 그냥 사용

        given(reviewRepository.save(any(Review.class)))
                .willReturn(savedReview);
        given(reviewLikeRepository.countLikesByReviewId(savedReview.getId()))
                .willReturn(likeCount);

        ReviewResponseDto responseDto = reviewService.saveReview(userId, requestDto);
        assertThat(responseDto).isNotNull();
    }


    @Test
    void findAllMyReviews() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 20);
        ReviewResponseDto reviewResponseDto1 = new ReviewResponseDto(1L, 1L, "맛있어요", 4.5f, "익명", null, 0L);
        ReviewResponseDto reviewResponseDto2 = new ReviewResponseDto(2L, 2L, "별로에요", 2.0f, "익명", null, 0L);
        Page<ReviewResponseDto> responseDtoPage = new PageImpl<>(List.of(reviewResponseDto1, reviewResponseDto2), pageable, 2);

        when(reviewRepository.countLikesByUserId(userId, pageable))
                .thenReturn(responseDtoPage);

        Page<ReviewResponseDto> res = reviewService.findAllMyReviews(userId, pageable);
        assertThat(res).isNotNull();
        assertThat(res.getTotalElements()).isEqualTo(2);
        assertThat(res.getContent().get(0).contents()).isEqualTo("맛있어요");
        assertThat(res.getContent().get(1).contents()).isEqualTo("별로에요");

    }

    @Test
    void findAllStoreReviews() {
        Long storeId = 1L;
        Pageable pageable = PageRequest.of(0, 20);
        ReviewResponseDto reviewResponseDto1 = new ReviewResponseDto(1L, 1L, "맛있어요", 4.5f, "익명", null, 0L);
        ReviewResponseDto reviewResponseDto2 = new ReviewResponseDto(2L, 2L, "별로에요", 2.0f, "익명", null, 0L);
        Page<ReviewResponseDto> responseDtoPage = new PageImpl<>(List.of(reviewResponseDto1, reviewResponseDto2), pageable, 2);

        when(reviewRepository.countLikesByStoreId(storeId, pageable))
                .thenReturn(responseDtoPage);

        Page<ReviewResponseDto> res = reviewService.findAllStoreReviews(storeId, pageable);
        assertThat(res).isNotNull();
        assertThat(res.getTotalElements()).isEqualTo(2);
        assertThat(res.getContent().get(0).contents()).isEqualTo("맛있어요");
        assertThat(res.getContent().get(1).contents()).isEqualTo("별로에요");
    }

    @Test
    void updateReview() {
        User user = new User();
        Order order = Order.builder().build();;
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(user, "id", 1L);
        ReviewUpdateRequestDto requestDto =
                new ReviewUpdateRequestDto(
                        "수정할게요",
                        1.1f,
                        AnonymousStatus.NON_ANONYMOUS);
        Review findReview = Review.builder().
                id(1L)
                .order(order)
                .user(user)
                .build();
        when(reviewRepository.findReviewByIdWithUser(1L))
                .thenReturn(findReview);
        Long likeCount = 0L;
        when(reviewLikeRepository.countLikesByReviewId(findReview.getId()))
                .thenReturn(likeCount);

        ReviewResponseDto res = reviewService.updateReview(user.getId(), 1L, requestDto);
        assertThat(res).isNotNull();
        assertThat(res.contents()).isEqualTo("수정할게요");
        assertThat(res.rate()).isEqualTo(requestDto.rate());
        assertThat(res.likeCount()).isEqualTo(likeCount);

    }

    @Test
    void updateException(){
        User user = new User();
        Order order = Order.builder().build();;
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(user, "id", 2L);
        ReviewUpdateRequestDto requestDto =
                new ReviewUpdateRequestDto(
                        "수정할게요",
                        1.1f,
                        AnonymousStatus.NON_ANONYMOUS);
        Review findReview = Review.builder().
                id(1L)
                .order(order)
                .user(user)
                .build();

        when(reviewRepository.findReviewByIdWithUser(1L))
                .thenReturn(findReview);
        assertThrows(BusinessException.class, ()->{
            reviewService.updateReview(1L, 1L, requestDto);
        });
    }

    @Test
    void deleteReview() {
        User user = new User();
        Order order = Order.builder().build();;
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(user, "id", 1L);
        Review findReview = Review.builder().
                id(1L)
                .order(order)
                .user(user)
                .build();

        when(reviewRepository.findReviewByIdWithUser(1L))
                .thenReturn(findReview);
        reviewService.deleteReview(1L, 1L);
        assertThat(findReview.getDeletedAt()).isNotNull();
    }
    @Test
    void deleteReviewException() {
        User user = new User();
        Order order = Order.builder().build();;
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(user, "id", 2L);
        Review findReview = Review.builder().
                id(1L)
                .order(order)
                .user(user)
                .build();

        when(reviewRepository.findReviewByIdWithUser(1L))
                .thenReturn(findReview);
        assertThrows(BusinessException.class, ()->{
            reviewService.deleteReview(1L, 1L);
        });
        assertThat(findReview.getDeletedAt()).isNull();
    }

    @Test
    void saveReviewReply() {
        User user = new User();
        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", 2L);
        ReflectionTestUtils.setField(user, "id", 1L);
        Store store = Store.builder()
                .user(owner)
                .build();
        Order order = Order.builder()
                .store(store)
                .build();
        ReflectionTestUtils.setField(order, "id", 1L);
        Review findReview = Review.builder().
                id(1L)
                .order(order)
                .user(user)
                .build();
        ReviewOwnerRequestDto requestDto = new ReviewOwnerRequestDto("감사해요");
        when(reviewRepository.findReviewByIdWithUser(1L))
                .thenReturn(findReview);
        Long likeCount = 0L;
        when(reviewLikeRepository.countLikesByReviewId(findReview.getId()))
                .thenReturn(likeCount);
        ReviewResponseDto responseDto = reviewService.saveReviewReply(2L, 1L, requestDto);
        assertThat(responseDto.reply()).isEqualTo("감사해요");

    }
    @Test
    void saveReviewReplyException() {
        User user = new User();
        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", 3L);
        ReflectionTestUtils.setField(user, "id", 1L);
        Store store = Store.builder()
                .user(owner)
                .build();
        Order order = Order.builder()
                .store(store)
                .build();
        ReflectionTestUtils.setField(order, "id", 1L);
        Review findReview = Review.builder().
                id(1L)
                .order(order)
                .user(user)
                .build();
        ReviewOwnerRequestDto requestDto = new ReviewOwnerRequestDto("감사해요");
        Long reviewId = 1L;
        Long ownerId = 2L;
        when(reviewRepository.findReviewByIdWithUser(reviewId))
                .thenReturn(findReview);
        assertThrows(BusinessException.class, ()->{
            reviewService.saveReviewReply(ownerId, reviewId, requestDto);
        });

    }



    @Test
    void updateReviewReply() {
        User user = new User();
        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", 2L);
        ReflectionTestUtils.setField(user, "id", 1L);
        Store store = Store.builder()
                .user(owner)
                .build();
        Order order = Order.builder()
                .store(store)
                .build();
        ReflectionTestUtils.setField(order, "id", 1L);
        Review findReview = Review.builder().
                id(1L)
                .order(order)
                .user(user)
                .build();
        ReviewOwnerRequestDto requestDto = new ReviewOwnerRequestDto("감사해요");
        Long reviewid = 1L;
        when(reviewRepository.findReviewByIdWithUser(reviewid))
                .thenReturn(findReview);
        assertThat(reviewService.updateReviewReply(2L, 1L, requestDto)
                .reply())
                .isEqualTo("감사해요");
    }

    @Test
    void deleteReviewReply() {
        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", 2L);
        Store store = Store.builder()
                .user(owner)
                .build();
        Order order = Order.builder()
                .store(store)
                .build();
        ReflectionTestUtils.setField(order, "id", 1L);
        Review findReview = Review.builder().
                id(1L)
                .order(order)
                .build();
        when(reviewRepository.findReviewByIdWithUser(1L))
                .thenReturn(findReview);
        reviewService.deleteReviewReply(2L, 1L);
        assertThat(findReview.getReply()).isNull();
    }

    @Test
    void saveLike() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        Review findReview = Review.builder()
                .id(1L)
                .build();
        ReviewLike findReviewLike = ReviewLike.from(findReview, user);

        when(userRepository.findByIdOrElseThrow(user.getId()))
                .thenReturn(user);
        when(reviewRepository.findReviewByIdWithUser(findReview.getId()))
                .thenReturn(findReview);
        when(reviewLikeRepository.existsReviewLikeByUserAndReview(user, findReview))
                .thenReturn(false);

        reviewService.saveLike(1L, 1L);

    }

    @Test
    void deleteLike() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        Review findReview = Review.builder()
                .id(1L)
                .build();
        ReviewLike findReviewLike = ReviewLike.from(findReview, user);

        when(userRepository.findByIdOrElseThrow(user.getId()))
                .thenReturn(user);
        when(reviewRepository.findReviewByIdWithUser(findReview.getId()))
                .thenReturn(findReview);
        when(reviewLikeRepository.findByUserAndReview(user, findReview))
                .thenReturn(Optional.of(findReviewLike));
        reviewService.deleteLike(1L, 1L);

    }

}