package com.team24.outsourcing_project.domain.order.service;

import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import com.team24.outsourcing_project.domain.menu.repository.MenuRepository;
import com.team24.outsourcing_project.domain.order.dto.OrderAcceptResponseDto;
import com.team24.outsourcing_project.domain.order.dto.OrderStatusResponseDto;
import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.order.entity.OrderStatus;
import com.team24.outsourcing_project.domain.order.repository.OrderRepository;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.repository.StoreRepository;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.entity.UserRole;
import com.team24.outsourcing_project.domain.user.repository.UserRepository;
import com.team24.outsourcing_project.exception.ApplicationException;
import com.team24.outsourcing_project.exception.ErrorCode;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    // 주문 생성
    @Transactional
    public void createOrder(Long userId, Long storeId, Long menuId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없습니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없습니다."));

        if (store.getMinOrderPrice() >= menu.getMenuPrice()) {
            throw new ApplicationException(ErrorCode.MIN_ORDER_PRICE);
        }

        LocalTime now = LocalTime.now();
        if (now.isBefore(store.getOpenTime()) || now.isAfter(
                store.getCloseTime())) {
            throw new ApplicationException(ErrorCode.STORE_TIME);
        }

        Order order = Order.create(user, store, menu, OrderStatus.PENDING);
        orderRepository.save(order);
    }

    // 주문 수락
    @Transactional
    public OrderAcceptResponseDto acceptOrder(Long orderId, AuthUser authUser) {

        if (!UserRole.OWNER.equals(authUser.getRole())) {
            throw new ApplicationException(ErrorCode.OWNER_ROLE);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없습니다."));

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new ApplicationException(ErrorCode.PENDING_STATUS);
        }

        order.accept();

        return new OrderAcceptResponseDto(order.getId(), order.getStatus());
    }

    // 주문 상태
    @Transactional
    public OrderStatusResponseDto statusOrder(Long orderId, AuthUser authUser) {
        if (!UserRole.OWNER.equals(authUser.getRole())) {
            throw new ApplicationException(ErrorCode.OWNER_ROLE);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없습니다."));

        if (order.getStatus().equals(OrderStatus.ACCEPTED)) {
            order.statusDelivering();
        } else if (order.getStatus().equals(OrderStatus.DELIVERING)) {
            order.statusCompleted();
        } else {
            throw new ApplicationException(ErrorCode.INVALID_ORDER_STATUS);
        }

        return new OrderStatusResponseDto(order.getId(), order.getStatus());
    }
}
