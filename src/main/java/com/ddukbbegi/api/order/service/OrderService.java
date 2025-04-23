package com.ddukbbegi.api.order.service;

import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.order.dto.request.OrderCreateRequestDto;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.entity.OrderMenu;
import com.ddukbbegi.api.order.repository.OrderMenuRepository;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final OrderMenuRepository orderMenuRepository;

    private final Clock clock;

    protected LocalTime now() {
        return LocalTime.now(clock);
    }

    @Transactional
    public Long createOrder(OrderCreateRequestDto request, long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        List<Long> menuIds = request.menus().stream()
                .map(OrderCreateRequestDto.MenuOrderDto::menuId)
                .toList();

        List<Menu> menus = menuRepository.findAllByIdOrElseThrow(menuIds);
        checkIsAllNotDeleted(menus);

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

        return savedOrder.getId();
    }

    private void checkIsAllNotDeleted(List<Menu> menus) {
        for(Menu menu : menus) {
            if (menu.isDeleted()) {
                throw new BusinessException(ResultCode.MENU_IS_DELETED);
            }
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
}
