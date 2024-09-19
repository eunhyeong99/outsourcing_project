package com.team24.outsourcing_project.domain.order.entity;

public enum OrderStatusEnum {
    ACCEPTED(OrderStatus.ACCEPTED),
    DELIVERING(OrderStatus.DELIVERING),
    COMPLETED(OrderStatus.COMPLETED);

    private final String orderStatus;

    OrderStatusEnum(String OrderStatus) {
        this.orderStatus = OrderStatus;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public static class OrderStatus{
        public static final String ACCEPTED = "ROLE_ACCEPTED";
        public static final String DELIVERING = "ROLE_DELIVERING";
        public static final String COMPLETED = "ROLE_COMPLETED";
    }
}
