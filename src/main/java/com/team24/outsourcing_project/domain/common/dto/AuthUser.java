package com.team24.outsourcing_project.domain.common.dto;

import com.team24.outsourcing_project.domain.user.entity.UserRole;
import lombok.Getter;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final UserRole role;

    public AuthUser(Long id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
