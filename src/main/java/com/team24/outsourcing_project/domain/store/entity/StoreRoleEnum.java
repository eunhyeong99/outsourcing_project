package com.team24.outsourcing_project.domain.store.entity;

import lombok.Getter;

@Getter
public enum StoreRoleEnum{
    OPEN(StoreRole.OPEN),
    OUT(StoreRole.OUT);

    private final String authority;

    StoreRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class StoreRole {
        public static final String OPEN = "ROLE_OPEN";
        public static final String OUT = "ROLE_OUT";
    }
}
