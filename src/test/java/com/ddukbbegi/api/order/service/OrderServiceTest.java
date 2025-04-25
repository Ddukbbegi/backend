package com.ddukbbegi.api.order.service;

import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.repository.OrderMenuRepository;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.*;
import java.util.List;

import static com.ddukbbegi.common.component.ResultCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrderMenuRepository orderMenuRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private User user;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2024, 1, 1, 10, 0).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
        );
        orderService = new OrderService(orderRepository, userRepository, menuRepository, storeRepository, orderMenuRepository,reviewRepository,fixedClock);

        user = User.of("test@email.com", "pw", "홍길동", "010-1234-5678", UserRole.USER);
        given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);
    }

    @Test
    @DisplayName("정상적인 메뉴 리스트로 주문 생성에 성공한다")
    void createOrder_success() {
        //given
        OrderCreateRequestDto request = new OrderCreateRequestDto(
                List.of(
                        new OrderCreateRequestDto.MenuOrderDto(1L, 1),
                        new OrderCreateRequestDto.MenuOrderDto(2L, 1)
                ),
                "문 앞에 놔주세요"
        );

        Menu menu1 = Menu.builder().name("짜장면").price(7000).isOption(false).storeId(1L).build();
        Menu menu2 = Menu.builder().name("짬뽕").price(8000).isOption(false).storeId(1L).build();

        ReflectionTestUtils.setField(menu1,"id",1L);
        ReflectionTestUtils.setField(menu2,"id",2L);

        Store store = Store.builder()
                .minDeliveryPrice(1000)
                .weekdayWorkingStartTime(LocalTime.of(0, 0))
                .weekdayWorkingEndTime(LocalTime.of(23, 59))
                .build();

        given(menuRepository.findAllByIdOrElseThrow(anyList())).willReturn(List.of(menu1, menu2));
        given(storeRepository.findByIdOrElseThrow(1L)).willReturn(store);
        given(orderRepository.save(any())).willAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ReflectionTestUtils.setField(order,"id",100L);
            return order;
        });

        //when
        Long orderId = orderService.createOrder(request, 1L);

        //then
        assertThat(orderId).isEqualTo(100L);
    }

    @Test
    @DisplayName("삭제된 메뉴가 포함되어 있으면 예외가 발생한다")
    void createOrder_fail_dueToDeletedMenu() {
        //given
        OrderCreateRequestDto request = new OrderCreateRequestDto(
                List.of(
                        new OrderCreateRequestDto.MenuOrderDto(1L, 1),
                        new OrderCreateRequestDto.MenuOrderDto(2L, 1)
                ),
                "문 앞에 놔주세요"
        );

        Menu menu1 = Menu.builder().name("짜장면").price(7000).isOption(false).storeId(1L).build();
        ReflectionTestUtils.setField(menu1,"id",1L);
        menu1.delete();

        given(menuRepository.findAllByIdOrElseThrow(anyList())).willReturn(List.of(menu1));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(MENU_IS_DELETED.getDefaultMessage());;
    }

    @Test
    @DisplayName("서로 다른 가게의 메뉴가 섞여 있으면 예외가 발생한다")
    void createOrder_fail_dueToMixedStoreMenus() {
        OrderCreateRequestDto request = new OrderCreateRequestDto(
                List.of(
                        new OrderCreateRequestDto.MenuOrderDto(1L, 1),
                        new OrderCreateRequestDto.MenuOrderDto(2L, 1)
                ),
                "문 앞에 놔주세요"
        );

        Menu menu1 = Menu.builder().name("짜장면").price(7000).isOption(false).storeId(1L).build();
        Menu menu2 = Menu.builder().name("피자").price(15000).isOption(false).storeId(2L).build();

        given(menuRepository.findAllByIdOrElseThrow(anyList())).willReturn(List.of(menu1, menu2));

        assertThatThrownBy(() -> orderService.createOrder(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(CONTAIN_DIFFERENT_STORE_MENU.getDefaultMessage());
    }

    @Test
    @DisplayName("최소 주문 금액 미만이면 예외가 발생한다")
    void createOrder_fail_dueToUnderMinimumAmount() {
        //given
        OrderCreateRequestDto request = new OrderCreateRequestDto(
                List.of(new OrderCreateRequestDto.MenuOrderDto(1L, 1)),
                "적은 금액"
        );

        Menu menu = Menu.builder().name("미니샐러드").price(1000).isOption(false).storeId(1L).build();
        ReflectionTestUtils.setField(menu,"id",1L);

        Store store = Store.builder()
                .minDeliveryPrice(12000)
                .weekdayWorkingStartTime(LocalTime.of(0, 0))
                .weekdayWorkingEndTime(LocalTime.of(23, 59))
                .build();

        given(menuRepository.findAllByIdOrElseThrow(anyList())).willReturn(List.of(menu));
        given(storeRepository.findByIdOrElseThrow(1L)).willReturn(store);

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(UNDER_MIN_DELIVERY_PRICE.getDefaultMessage());
    }

    @Test
    @DisplayName("가게가 영업 시간이 아니면 예외가 발생한다")
    void createOrder_fail_dueToStoreClosed() {
        //given
        OrderCreateRequestDto request = new OrderCreateRequestDto(
                List.of(new OrderCreateRequestDto.MenuOrderDto(1L, 1)),
                "닫은 가게"
        );

        Menu menu = Menu.builder().name("라면").price(6000).isOption(false).storeId(1L).build();

        Store store = Store.builder()
                .minDeliveryPrice(1000)
                .weekdayWorkingStartTime(LocalTime.of(23, 0))
                .weekdayWorkingEndTime(LocalTime.of(23, 30))
                .build();

        given(menuRepository.findAllByIdOrElseThrow(anyList())).willReturn(List.of(menu));
        given(storeRepository.findByIdOrElseThrow(1L)).willReturn(store);

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(STORE_NOT_WORKING.getDefaultMessage());
    }
}
