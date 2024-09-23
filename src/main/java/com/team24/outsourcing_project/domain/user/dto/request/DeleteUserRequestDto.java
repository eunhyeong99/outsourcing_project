package com.team24.outsourcing_project.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteUserRequestDto {

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
