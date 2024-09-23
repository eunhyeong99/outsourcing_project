package com.team24.outsourcing_project.domain.order.service;

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
  public Order createOrder(Long userId, Long storeId, Long menuId) {
    Store store = storeRepository.findById(storeId).orElseThrow();
    Menu menu = menuRepository.findById(menuId).orElseThrow();
    User user = userRepository.findById(userId).orElseThrow();

    // TODO 스토어와 메뉴에서 확인 후 수정 필요
    if (store.getMinOrderPrice() > menu.getMenuPrice()) {
      throw new ApplicationException(null); // TODO 최소 주문 금액 예외처리 필요
    }

    // TODO 스토어 오픈시간, 클로징시간 확인 후 수정 필요
    LocalTime now = LocalTime.now();
    if (now.isBefore(store.getOpenTime().toLocalTime()) || now.isAfter(
        store.getCloseTime().toLocalTime())) {
      throw new ApplicationException(null); // TODO 영업 시간 예외처리 필요
    }

    Order order = Order.create(user, store, menu, OrderStatus.PENDING);
    return orderRepository.save(order);
  }

  // 주문 수락
  @Transactional
  public OrderAcceptResponseDto acceptOrder(Long orderId, User user) {
    if (!UserRole.OWNER.equals(user.getRole())) {
      throw new ApplicationException(null); // TODO 사장이 맞는지 예외처리 필요
    }

    Order order = orderRepository.findById(orderId).orElseThrow();

    if (!order.getStatus().equals(OrderStatus.PENDING)) {
      throw new ApplicationException(null); // TODO 주문 상태가 PENDING인지 예외처리 필요
    }

    Order acceptedOrder = order.accept();
    orderRepository.save(acceptedOrder);

    return new OrderAcceptResponseDto(acceptedOrder.getId(), acceptedOrder.getStatus());
  }

  // 주문 상태
  @Transactional
  public OrderStatusResponseDto statusOrder(Long orderId, User user, OrderStatus status) {
    if (!UserRole.OWNER.equals(user.getRole())) {
      throw new ApplicationException(null); // TODO 사장이 맞는지 예외처리 필요
    }

    Order order = orderRepository.findById(orderId).orElseThrow();

    if (status.equals(OrderStatus.DELIVERING) && !order.getStatus().equals(OrderStatus.ACCEPTED)) {
      throw new ApplicationException(null); // TODO 주문 수락 상태여야 배달중으로 변경 가능 예외처리 필요
    }

    if (status.equals(OrderStatus.COMPLETED) && !order.getStatus().equals(OrderStatus.DELIVERING)) {
      throw new ApplicationException(null); // TODO 배달중 상태여야 배달완료로 변경 가능 예외처리 필요
    }

    Order updatedOrder = Order.status(order.getUser(), order.getStore(), order.getMenu(), status);
    orderRepository.save(updatedOrder);

    return new OrderStatusResponseDto(updatedOrder.getId(), updatedOrder.getStatus());
  }
}
