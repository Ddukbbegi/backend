//package com.ddukbbegi.api.review;
//
//import com.ddukbbegi.api.menu.entity.Menu;
//import com.ddukbbegi.api.menu.repository.MenuRepository;
//import com.ddukbbegi.api.order.entity.Order;
//import com.ddukbbegi.api.order.repository.OrderMenuRepository;
//import com.ddukbbegi.api.order.repository.OrderRepository;
//import com.ddukbbegi.api.order.service.OrderService;
//import com.ddukbbegi.api.review.dto.AnonymousStatus;
//import com.ddukbbegi.api.review.dto.ReviewRequestDto;
//import com.ddukbbegi.api.review.dto.ReviewResponseDto;
//import com.ddukbbegi.api.review.entity.Review;
//import com.ddukbbegi.api.review.repository.ReviewLikeRepository;
//import com.ddukbbegi.api.review.repository.ReviewRepository;
//import com.ddukbbegi.api.review.service.ReviewService;
//import com.ddukbbegi.api.store.entity.Store;
//import com.ddukbbegi.api.store.repository.StoreRepository;
//import com.ddukbbegi.api.user.entity.User;
//import com.ddukbbegi.api.user.enums.UserRole;
//import com.ddukbbegi.api.user.repository.UserRepository;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.sql.Ref;
//import java.time.LocalTime;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//public class ReviewServiceTest {
//
//    @Mock
//    private ReviewRepository reviewRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ReviewLikeRepository reviewLikeRepository;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @InjectMocks
//    private ReviewService reviewService;
//
//    private User user;
//    private User owner;
//    private Order order;
//    private Store store;
//    private Menu menu;
//    private ReviewRequestDto dto;
//
//    @BeforeEach
//    void setup() {
//        user = new User("a@a.com", "pw", "닉", "010-1234-5678", UserRole.USER);
//        owner = new User("a@a.com", "pw", "닉", "010-1234-5678", UserRole.OWNER);
//        store = Store.builder().build();
//        order = new Order(user, store, "ord123");
//        dto = new ReviewRequestDto(1L, "맛있어요!", 4.0f, AnonymousStatus.NON_ANONYMOUS);
//        ReflectionTestUtils.setField(user, "id", 1L);
//        ReflectionTestUtils.setField(owner, "id", 2L);
//        ReflectionTestUtils.setField(order, "id", 1L);
//    }
//
//
//        //given
//        @Test
//        void saveReview_성공() {
//
//            Review review = Review.from(user, order, dto);
//            ReflectionTestUtils.setField(review, "id", 1L); // ID를 수동으로 세팅
//
//            given(userRepository.findByIdOrElseThrow(user.getId())).willReturn(user);
//            given(orderRepository.findByIdOrElseThrow(order.getId())).willReturn(order);
//            given(reviewRepository.save(any())).willReturn(review);
//            given(reviewLikeRepository.countLikesByReviewId(review.getId())).willReturn(0L);
//
//            // when
//            ReviewResponseDto result = reviewService.saveReview(user.getId(), dto);
//
//            // then
//            assertEquals("맛있어요!", result.contents());
//            assertEquals("맛있어요!", result.contents());
//            assertEquals(4.0f, result.rate());
//            assertEquals(0L, result.likeCount());
//        }
//
//
//}
