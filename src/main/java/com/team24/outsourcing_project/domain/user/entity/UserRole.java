package com.team24.outsourcing_project.domain.user.entity;

import com.team24.outsourcing_project.exception.ApplicationException;
import com.team24.outsourcing_project.exception.ErrorCode;
import java.util.Arrays;
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

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.ROLE_INVALID));
    }
}