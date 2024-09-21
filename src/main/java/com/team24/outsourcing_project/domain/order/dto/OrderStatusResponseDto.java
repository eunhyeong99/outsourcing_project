package com.team24.outsourcing_project.domain.order.dto;

import com.team24.outsourcing_project.domain.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusResponseDto {

  private final Long orderId;
  private final String status;

  public OrderStatusResponseDto(Long orderId, OrderStatus status) {
    this.orderId = orderId;
    this.status = String.valueOf(status);
  }
}
