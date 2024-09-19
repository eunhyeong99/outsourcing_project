package com.team24.outsourcing_project.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("유저"),
    OWNER("사장");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getUserRole() {
        return this.description;
    }
}