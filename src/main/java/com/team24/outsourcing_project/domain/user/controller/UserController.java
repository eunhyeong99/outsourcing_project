package com.team24.outsourcing_project.domain.user.controller;

import com.team24.outsourcing_project.domain.common.annotation.Auth;
import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.user.dto.request.DeleteUserRequestDto;
import com.team24.outsourcing_project.domain.user.dto.request.SigninRequestDto;
import com.team24.outsourcing_project.domain.user.dto.request.SignupRequestDto;
import com.team24.outsourcing_project.domain.user.dto.response.SigninResponseDto;
import com.team24.outsourcing_project.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/signup")
    public void signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<SigninResponseDto> signin(@Valid @RequestBody SigninRequestDto signinRequestDto) {
        return ResponseEntity.ok(userService.signin(signinRequestDto));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(
            @Auth AuthUser authUser,
            @PathVariable Long id,
            @RequestBody DeleteUserRequestDto deleteUserRequestDto) {
        userService.deleteUser(authUser,id,deleteUserRequestDto);
    }
}
