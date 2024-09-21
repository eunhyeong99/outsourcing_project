package com.team24.outsourcing_project.domain.order.controller;

import com.team24.outsourcing_project.domain.common.annotation.Auth;
import com.team24.outsourcing_project.domain.order.dto.OrderRequestDto;
import com.team24.outsourcing_project.domain.order.dto.OrderAcceptResponseDto;
import com.team24.outsourcing_project.domain.order.dto.OrderStatusResponseDto;
import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.order.entity.OrderStatus;
import com.team24.outsourcing_project.domain.order.service.OrderService;
import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  // 주문 생성
  @PostMapping("/orders")
  public Order createOrder(@RequestBody OrderRequestDto requestDto, @Auth AuthUser authUser) {
    return orderService.createOrder(authUser.getId(), requestDto.getStoreId(),
        requestDto.getMenuId());
  }

  // 주문 수락
  @PatchMapping("/orders/{id}/accept")
  public OrderAcceptResponseDto acceptOrder(@PathVariable Long id, User user) {
    return orderService.acceptOrder(id, user);
  }

  // 주문 상태
  @PatchMapping("/orders/{id}/status")
  public OrderStatusResponseDto statusOrder(@PathVariable Long id, User user,
      @RequestParam OrderStatus status) {
    return orderService.statusOrder(id, user, status);
  }
}
