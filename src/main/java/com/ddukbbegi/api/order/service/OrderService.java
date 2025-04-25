package com.ddukbbegi.api.order.service;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;
import com.ddukbbegi.api.order.dto.response.OrderCreateResponseDto;
import com.ddukbbegi.api.order.dto.response.OrderHistoryOwnerResponseDto;
import com.ddukbbegi.api.order.dto.response.OrderHistoryUserResponseDto;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.entity.OrderMenu;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.order.repository.OrderMenuRepository;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final ReviewRepository reviewRepository;

    private final Clock clock;

    protected LocalTime now() {
        return LocalTime.now(clock);
    }

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto request, long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        List<Long> menuIds = request.menus().stream()
                .map(OrderCreateRequestDto.MenuOrderDto::menuId)
                .toList();

        List<Menu> menus = menuRepository.findAllByIdInAndIsDeletedFalse(menuIds);
        checkIsAllNotDeleted(menuIds.size(),menus.size());

        long storeId = menus.get(0).getStoreId();

        checkIsAllSameStore(menus, storeId);
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        LocalTime now = now();
        checkStoreIsWorking(now,store);

        checkIsTotalPriceOverMinDeliveryPrice(menus,request,store.getMinDeliveryPrice());

        Order order = Order.builder()
                .user(user)
                .store(store)
                .requestComment(request.requestComment())
                .build();

        Order savedOrder = orderRepository.save(order);

        for (OrderCreateRequestDto.MenuOrderDto item : request.menus()) {
            Menu menu = menus.stream()
                    .filter(m -> m.getId().equals(item.menuId()))
                    .findFirst()
                    .orElseThrow();

            OrderMenu orderMenu = OrderMenu.builder()
                    .order(savedOrder)
                    .menu(menu)
                    .count(item.count())
                    .build();

            orderMenuRepository.save(orderMenu);
        }

        return new OrderCreateResponseDto(savedOrder.getId());
    }

    private void checkIsAllNotDeleted(int expectedSize, int realSize) {
        if (expectedSize != realSize) {
            throw new BusinessException(ResultCode.MENU_IS_DELETED);
        }
    }

    private int checkIsTotalPriceOverMinDeliveryPrice(List<Menu> menus, OrderCreateRequestDto request, Integer minDeliveryPrice) {
        int totalPrice = 0;
        for (OrderCreateRequestDto.MenuOrderDto item : request.menus()) {
            Menu menu = menus.stream()
                    .filter(m -> m.getId().equals(item.menuId()))
                    .findFirst()
                    .orElseThrow();

            totalPrice += menu.getPrice() * item.count();
        }

        if (totalPrice < minDeliveryPrice) {
            throw new BusinessException(ResultCode.UNDER_MIN_DELIVERY_PRICE);
        }

        return totalPrice;
    }

    private void checkStoreIsWorking(LocalTime now,Store store) {
        if (now.isBefore(store.getWeekdayWorkingStartTime()) || now.isAfter(store.getWeekdayWorkingEndTime())) {
            throw new BusinessException(ResultCode.STORE_NOT_WORKING);
        }
    }

    private void checkIsAllSameStore(List<Menu> menus,long storeId) {
        boolean allSameStore = menus.stream()
                .allMatch(menu -> menu.getStoreId()==(storeId));
        if (!allSameStore) {
            throw new BusinessException(ResultCode.CONTAIN_DIFFERENT_STORE_MENU);
        }
    }

    @Transactional(readOnly = true)
    public PageResponseDto<OrderHistoryUserResponseDto> getOrdersForUser(long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        List<Long> orderIds = orders.getContent().stream()
                .map(Order::getId)
                .toList();

        List<OrderMenu> orderMenus = orderMenuRepository.findByOrderIdInWithMenu(orderIds);
        Map<Long, List<OrderMenu>> orderMenuMap = orderMenus.stream()
                .collect(Collectors.groupingBy(om -> om.getOrder().getId()));

        Page<OrderHistoryUserResponseDto> result = orders.map(order ->
                OrderHistoryUserResponseDto.from(
                        order,
                        orderMenuMap.getOrDefault(order.getId(), List.of()),
                        false
                        //todo: Review에 Order 참조되면 주석 해제
                        //isReviewed(orderIds, order)
                )
        );

        return PageResponseDto.toDto(result);
    }

//    private boolean isReviewed(List<Long> orderIds, Order order) {
//        List<Long> reviewedOrderIds = reviewRepository.findReviewedOrderIds(orderIds);
//        Set<Long> reviewedOrderIdSet = new HashSet<>(reviewedOrderIds);
//
//        return reviewedOrderIdSet.contains(order.getId());
//    }

    @Transactional(readOnly = true)
    public PageResponseDto<OrderHistoryOwnerResponseDto> getOrdersForOwner(long storeId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByStoreId(storeId, pageable);
        List<Long> orderIds = orders.getContent().stream()
                .map(Order::getId)
                .toList();

        List<OrderMenu> orderMenus = orderMenuRepository.findByOrderIdInWithMenu(orderIds);
        Map<Long, List<OrderMenu>> orderMenuMap = orderMenus.stream()
                .collect(Collectors.groupingBy(om -> om.getOrder().getId()));

        Page<OrderHistoryOwnerResponseDto> result = orders.map(order ->
                OrderHistoryOwnerResponseDto.from(
                        order,
                        orderMenuMap.getOrDefault(order.getId(), List.of())
                )
        );

        return PageResponseDto.toDto(result);
    }

    @Transactional
    public void cancelOrder(long orderId, long userId) {
        Order order = orderRepository.findByIdOrElseThrow(orderId);
        checkOrderIsUserOrder(order, userId);
        checkCancelIsAvailable(order);

        order.cancel();
    }

    private void checkOrderIsUserOrder(Order order, long userId) {
        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_USER_MISMATCH);
        }
    }

    private void checkCancelIsAvailable(Order order) {
        if (order.getOrderStatus() != OrderStatus.WAITING) {
            throw new BusinessException(ResultCode.ORDER_CANNOT_BE_CANCELED,
                    "취소할 수 없는 주문입니다. (주문상태: " + order.getOrderStatus() + ")" );
        }
    }

}
