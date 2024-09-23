package com.team24.outsourcing_project.domain.order.dto;

import com.team24.outsourcing_project.domain.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderAcceptResponseDto {

  private final Long orderId;
  private final String accept;

  public OrderAcceptResponseDto(Long orderId, OrderStatus accept) {
    this.orderId = orderId;
    this.accept = String.valueOf(accept);
  }
}
