package com.team24.outsourcing_project.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ACCEPTED("주문 수락"),
    DELIVERING("배달 중"),
    COMPLETED("배달 완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getOrderStatus() {
        return this.description;
    }
}
