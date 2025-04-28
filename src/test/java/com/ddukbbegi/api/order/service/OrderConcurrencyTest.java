package com.ddukbbegi.api.order.service;

import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.MenuStatus;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreCategory;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OrderConcurrencyTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private MenuRepository menuRepository;
//
//    @Autowired
//    private OrderService orderService;
//
//    private User user;
//
//    private User owner;
//
//    private Store store;
//
//    private String uuid;
//
//    private Menu menu;
//
//    private Order order;
//
//    private AtomicInteger count;
//
//    private int threadCount;
//
//    ExecutorService executorService;
//
//    CountDownLatch latch;
//
//    private final String REQUEST_COMMENT = "문 앞에 놔주세요";
//
//    @BeforeEach
//    void setUp() {
//        user = userRepository.save(
//                User.of("user@email.com", "pw", "홍길동", "010-1234-5678", UserRole.USER)
//        );
//        owner = userRepository.save(
//                User.of("owner@email.com", "pw", "사장님", "010-5678-5678", UserRole.OWNER)
//        );
//
//        store = Store.builder().user(owner).name("김밥천국")
//                .category(StoreCategory.KOREAN).phoneNumber("010-1234-5678")
//                .description("맛있는 김밥").closedDays(List.of())
//                .weekdayWorkingStartTime(LocalTime.of(0, 0)).weekdayWorkingEndTime(LocalTime.of(23, 59))
//                .weekdayBreakStartTime(LocalTime.of(15, 0)).weekdayBreakEndTime(LocalTime.of(17, 0))
//                .weekendWorkingStartTime(LocalTime.of(0, 0)).weekendWorkingEndTime(LocalTime.of(23, 59))
//                .weekendBreakStartTime(LocalTime.of(15, 0)).weekendBreakEndTime(LocalTime.of(17, 0))
//                .minDeliveryPrice(10000).deliveryTip(3000)
//                .build();
//        store = storeRepository.save(store);
//
//        menu  = Menu.builder().name("짜장면").price(15000).isOption(false).store(store).status(MenuStatus.ON_SALE).build();
//        menuRepository.save(menu);
//
//        uuid = UUID.randomUUID().toString();
//
//        order = Order.builder().user(user).store(store).requestId(UUID.randomUUID().toString()).build();
//        order.setTotalPrice(5000L);
//        orderRepository.save(order);
//
//        threadCount = 100;
//        executorService = Executors.newFixedThreadPool(threadCount);
//        latch = new CountDownLatch(threadCount);
//
//        count = new AtomicInteger(0);
//    }
//
//
//    @Test
//    @DisplayName("동일한 requestId로 동시에 주문 생성 시 둘 다 생성될 수 있는 문제를 테스트한다")
//    void createOrder_duplicateRequestId_concurrent() throws InterruptedException {
//        // given
//        OrderCreateRequestDto request = new OrderCreateRequestDto(
//                List.of(
//                        new OrderCreateRequestDto.MenuOrderDto(menu.getId(), 1)
//                ),
//                REQUEST_COMMENT,
//                uuid,
//                true
//        );
//
//        // when
//        for (int i = 0; i < threadCount; i++) {
//            executorService.execute(() -> {
//                try {
//                    orderService.createOrder(request, user.getId());
//                } catch (Exception e) {
//                    System.err.println("주문 생성 실패: " + e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        //then
//        long reservationCount = orderRepository.count();
//        assertThat(reservationCount).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("동시에 주문 상태 변경 시 하나만 성공한다.")
//    void changeOrderStatus_concurrencyIssue() throws InterruptedException {
//        // when
//        for (int i = 0; i < threadCount/2; i++) {
//            executorService.execute(() -> {
//                try {
//                    orderService.cancelOrder(order.getId(), user.getId());
//                    count.incrementAndGet();
//                    System.out.println("주문 취소 성공");
//                } catch (Exception e) {
//                    System.err.println("주문 취소 실패: " + e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        for (int i = 0; i < threadCount/2; i++) {
//            executorService.execute(() -> {
//                try {
//                    orderService.updateOrderStatus(order.getId(), OrderStatus.ACCEPTED, owner.getId());
//                    count.incrementAndGet();
//                    System.out.println("주문 상태 변경 성공");
//                } catch (Exception e) {
//                    System.err.println("주문 상태 변경 실패: " + e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        // then
//        assertThat(count.get()).isEqualTo(1);
//    }
}
