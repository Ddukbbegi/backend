package com.ddukbbegi.api.order.service;

import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.MenuStatus;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;
import com.ddukbbegi.api.order.dto.response.OrderCreateResponseDto;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.order.repository.OrderMenuRepository;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.*;
import java.util.List;
import java.util.UUID;

import static com.ddukbbegi.common.component.ResultCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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
    private OrderMenuRepository orderMenuRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private User user;

    private Store store;

    private String uuid;

    private OrderService orderService;

    private final String REQUEST_COMMENT = "문 앞에 놔주세요";

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2024, 1, 1, 10, 0).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
        );
        orderService = new OrderService(orderRepository, userRepository, menuRepository, orderMenuRepository,reviewRepository,fixedClock);

        user = User.of("test@email.com", "pw", "홍길동", "010-1234-5678", UserRole.USER);
        ReflectionTestUtils.setField(user,"id",1L);

        store = Store.builder()
                .minDeliveryPrice(12000)
                .weekdayWorkingStartTime(LocalTime.of(0, 0))
                .weekdayWorkingEndTime(LocalTime.of(23, 59))
                .build();
        ReflectionTestUtils.setField(store,"id",1L);

        uuid = UUID.randomUUID().toString();
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
                REQUEST_COMMENT,
                uuid
        );

        Menu menu1 = Menu.builder().name("짜장면").price(7000).isOption(false).store(store).status(MenuStatus.ON_SALE).build();
        Menu menu2 = Menu.builder().name("짬뽕").price(8000).isOption(false).store(store).status(MenuStatus.ON_SALE).build();

        ReflectionTestUtils.setField(menu1,"id",1L);
        ReflectionTestUtils.setField(menu2,"id",2L);

        given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);
        given(menuRepository.findAllByIdInAndStatusNot(anyList(), eq(MenuStatus.DELETED))).willReturn(List.of(menu1, menu2));
        given(orderRepository.save(any())).willAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ReflectionTestUtils.setField(order,"id",100L);
            return order;
        });

        //when
        OrderCreateResponseDto responseDto = orderService.createOrder(request, 1L);

        //then
        assertThat(responseDto.id()).isEqualTo(100L);
    }

    @Test
    @DisplayName("동일한 requestId로 주문을 생성하면 예외가 발생한다")
    void createOrder_fail_dueToDuplicateRequestId() {
        // given
        OrderCreateRequestDto request = new OrderCreateRequestDto(
                List.of(
                        new OrderCreateRequestDto.MenuOrderDto(1L, 1),
                        new OrderCreateRequestDto.MenuOrderDto(2L, 1)
                ),
                REQUEST_COMMENT,
                uuid
        );

        given(orderRepository.existsByRequestId(uuid)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request, user.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(DUPLICATE_REQUEST_ID.getDefaultMessage());
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
                REQUEST_COMMENT,
                uuid
        );

        Menu menu1 = Menu.builder().name("짜장면").price(7000).isOption(false).store(store).build();
        ReflectionTestUtils.setField(menu1,"id",1L);
        menu1.delete();

        given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);
        given(menuRepository.findAllByIdInAndStatusNot(anyList(), eq(MenuStatus.DELETED))).willReturn(List.of());

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
                REQUEST_COMMENT,
                uuid
        );

        Store anotherStore = Store.builder()
                .build();
        ReflectionTestUtils.setField(anotherStore,"id",2L);

        Menu menu1 = Menu.builder().name("짜장면").price(7000).isOption(false).store(store).build();
        Menu menu2 = Menu.builder().name("피자").price(15000).isOption(false).store(anotherStore).build();

        given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);
        given(menuRepository.findAllByIdInAndStatusNot(anyList(), eq(MenuStatus.DELETED))).willReturn(List.of(menu1, menu2));

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
                REQUEST_COMMENT,
                uuid
        );

        Menu menu = Menu.builder().name("미니샐러드").price(1000).isOption(false).store(store).build();
        ReflectionTestUtils.setField(menu,"id",1L);

        given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);
        given(menuRepository.findAllByIdInAndStatusNot(anyList(), eq(MenuStatus.DELETED))).willReturn(List.of(menu));

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
                REQUEST_COMMENT,
                uuid
        );

        Store closedStore = Store.builder()
                .minDeliveryPrice(12000)
                .weekdayWorkingStartTime(LocalTime.of(23, 58))
                .weekdayWorkingEndTime(LocalTime.of(23, 59))
                .build();
        ReflectionTestUtils.setField(closedStore,"id",2L);

        Menu menu = Menu.builder().name("라면").price(12000).isOption(false).store(closedStore).build();
        ReflectionTestUtils.setField(menu,"id",1L);

        given(menuRepository.findAllByIdInAndStatusNot(anyList(), eq(MenuStatus.DELETED))).willReturn(List.of(menu));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(STORE_NOT_WORKING.getDefaultMessage());
    }

    @Test
    @DisplayName("WAITING 상태의 자신의 주문을 성공적으로 취소한다")
    void cancelOrder_success() {
        // given
        Order order = Order.builder().user(user).store(store).requestComment(REQUEST_COMMENT).build();
        ReflectionTestUtils.setField(order, "id", 1L);
        given(orderRepository.findByIdForUpdateOrElseThrow(1L)).willReturn(order);

        // when
        orderService.cancelOrder(1L, user.getId());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("본인의 주문이 아닌 경우 취소할 수 없다")
    void cancelOrder_fail_dueToWrongUser() {
        //given
        User otherUser = User.of("other@email.com", "pw", "고길동", "010-0000-0000", UserRole.USER);
        ReflectionTestUtils.setField(otherUser,"id",2L);
        Order order = Order.builder().user(otherUser).store(store).requestComment(REQUEST_COMMENT).build();
        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.findByIdForUpdateOrElseThrow(1L)).willReturn(order);

        // when & then
        assertThatThrownBy(() -> orderService.cancelOrder(1L, user.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ORDER_USER_MISMATCH.getDefaultMessage());
    }

    @ParameterizedTest
    @DisplayName("상태가 WAITING이 아닌 주문은 취소할 수 없다")
    @ValueSource(strings = {"ACCEPTED","COOKING","DELIVERING","DELIVERED","REJECTED", "CANCELED"})
    void cancelOrder_fail_dueToStatusIsNotWaiting(String status) {
        //given
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        Order order = Order.builder().user(user).store(store).requestComment(REQUEST_COMMENT).build();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "orderStatus", orderStatus);

        given(orderRepository.findByIdForUpdateOrElseThrow(1L)).willReturn(order);

        //when & then
        assertThatThrownBy(() -> orderService.cancelOrder(1L, user.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ORDER_CANNOT_BE_CANCELED.getDefaultMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "WAITING,ACCEPTED",
            "ACCEPTED,COOKING",
            "COOKING,DELIVERING",
            "DELIVERING,DELIVERED",
            "WAITING,REJECTED"
    })
    @DisplayName("주문 상태 변경 성공에 성공한다.")
    void updateOrderStatus_success(String from, String to) {
        //given
        User owner = User.of("test@email.com", "pw", "사장님", "010-1234-5678", UserRole.OWNER);
        ReflectionTestUtils.setField(owner,"id",2L);
        ReflectionTestUtils.setField(store, "user", owner);

        Order order = Order.builder().user(user).store(store).requestComment(REQUEST_COMMENT).build();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.valueOf(from));

        given(orderRepository.findByIdWithStoreForUpdateOrElseThrow(1L)).willReturn(order);

        //when
        orderService.updateOrderStatus(1L, OrderStatus.valueOf(to), owner.getId());

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.valueOf(to));
    }

    @ParameterizedTest
    @CsvSource({
            "WAITING,COOKING",
            "DELIVERING,ACCEPTED"
    })
    @DisplayName("잘못된 상태 흐름은 예외가 발생한다.")
    void updateOrderStatus_fail_dueToInvalidTransition(String from, String to) {
        //given
        User owner = User.of("test@email.com", "pw", "사장님", "010-1234-5678", UserRole.OWNER);
        ReflectionTestUtils.setField(owner, "id", 2L);
        ReflectionTestUtils.setField(store, "user", owner);

        Order order = Order.builder().user(user).store(store).requestComment(REQUEST_COMMENT).build();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.valueOf(from));

        given(orderRepository.findByIdWithStoreForUpdateOrElseThrow(1L)).willReturn(order);

        // when & then
        assertThatThrownBy(() -> orderService.updateOrderStatus(1L, OrderStatus.valueOf(to), owner.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ORDER_STATUS_FLOW_INVALID.getDefaultMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "REJECTED,COOKING",
            "DELIVERED,REJECTED",
            "CANCELED,ACCEPTED"
    })
    @DisplayName("REJECTED 혹은 CANCELED 혹은 DELIVERED 이후에는 상태 변경 시 예외가 발생한다.")
    void updateOrderStatus_fail_dueToTerminalStates(String from, String to) {
        //given
        User owner = User.of("test@email.com", "pw", "사장님", "010-1234-5678", UserRole.OWNER);
        ReflectionTestUtils.setField(owner, "id", 2L);
        ReflectionTestUtils.setField(store, "user", owner);

        Order order = Order.builder().user(new User()).store(store).requestComment(REQUEST_COMMENT).build();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.valueOf(from));

        given(orderRepository.findByIdWithStoreForUpdateOrElseThrow(1L)).willReturn(order);

        // when & then
        assertThatThrownBy(() -> orderService.updateOrderStatus(1L, OrderStatus.valueOf(to), owner.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ORDER_ALREADY_TERMINATED.getDefaultMessage());
    }

    @Test
    @DisplayName("가게 소유자가 아닌 경우 주문 상태 변경 시 예외가 발생한다.")
    void updateOrderStatus_fail_dueToWrongOwner() {
        //given
        User owner = User.of("test@email.com", "pw", "사장님", "010-1234-5678", UserRole.OWNER);
        User other = User.of("other@email.com", "pw", "다른사람", "010-0000-1111", UserRole.OWNER);
        ReflectionTestUtils.setField(owner, "id", 2L);
        ReflectionTestUtils.setField(other, "id", 3L);
        ReflectionTestUtils.setField(store, "user", owner);

        Order order = Order.builder().user(user).store(store).requestComment(REQUEST_COMMENT).build();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.WAITING);

        given(orderRepository.findByIdWithStoreForUpdateOrElseThrow(1L)).willReturn(order);

        assertThatThrownBy(() -> orderService.updateOrderStatus(1L, OrderStatus.ACCEPTED, other.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(STORE_OWNER_MISMATCH.getDefaultMessage());
    }
}
