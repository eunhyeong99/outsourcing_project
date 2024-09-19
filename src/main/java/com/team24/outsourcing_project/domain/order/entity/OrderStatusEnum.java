package com.team24.outsourcing_project.domain.order.entity;

public enum OrderStatusEnum {
    ACCEPTED(Authority.ACCEPTED),
    DELIVERING(Authority.DELIVERING),
    COMPLETED(Authority.COMPLETED);

    private final String authority;

    OrderStatusEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class {
        public static final String ACCEPTED = "ROLE_ACCEPTED";
        public static final String DELIVERING = "ROLE_DELIVERING";
        public static final String COMPLETED = "ROLE_COMPLETED";
    }
}
