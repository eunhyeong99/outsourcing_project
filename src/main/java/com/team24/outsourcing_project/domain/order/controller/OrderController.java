package com.team24.outsourcing_project.domain.order.controller;

import com.team24.outsourcing_project.domain.common.annotation.Auth;
import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.order.dto.OrderAcceptResponseDto;
import com.team24.outsourcing_project.domain.order.dto.OrderRequestDto;
import com.team24.outsourcing_project.domain.order.dto.OrderStatusResponseDto;
import com.team24.outsourcing_project.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderRequestDto requestDto, @Auth AuthUser authUser) {
        orderService.createOrder(authUser.getId(), requestDto.getStoreId(),
                requestDto.getMenuId());
    }

    // 주문 수락
    @PatchMapping("/orders/{id}/accept")
    public OrderAcceptResponseDto acceptOrder(@PathVariable Long id, @Auth AuthUser authUser) {
        return orderService.acceptOrder(id, authUser);
    }

    // 주문 상태
    @PatchMapping("/orders/{id}/status")
    public OrderStatusResponseDto statusOrder(@PathVariable Long id, @Auth AuthUser authUser) {
        return orderService.statusOrder(id, authUser);
    }
}
