package com.team24.outsourcing_project.domain.user.service;

import com.team24.outsourcing_project.config.JwtUtil;
import com.team24.outsourcing_project.config.PasswordEncoder;
import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.user.dto.request.DeleteUserRequestDto;
import com.team24.outsourcing_project.domain.user.dto.request.SigninRequestDto;
import com.team24.outsourcing_project.domain.user.dto.request.SignupRequestDto;
import com.team24.outsourcing_project.domain.user.dto.response.SigninResponseDto;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.entity.UserRole;
import com.team24.outsourcing_project.domain.user.repository.UserRepository;
import com.team24.outsourcing_project.exception.ApplicationException;
import com.team24.outsourcing_project.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        if (signupRequestDto.getEmail() == null) {
            throw new ApplicationException(ErrorCode.EMAIL_NULL);
        }

        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new ApplicationException(ErrorCode.EMAIL_DUPLICATE);
        }

        String encodePassword = passwordEncoder.encode(signupRequestDto.getPassword());

        UserRole userRole = UserRole.of(signupRequestDto.getRole());

        User newUser = User.create(signupRequestDto.getEmail(), encodePassword, userRole);

        userRepository.save(newUser);
    }

    @Transactional
    public SigninResponseDto signin(SigninRequestDto signinRequestDto) {

        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.PASSWORD_MISMATCH);
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());

        return new SigninResponseDto(bearerToken);
    }

    @Transactional
    public void deleteUser(AuthUser authUser, Long id, DeleteUserRequestDto deleteUserRequestDto) {
        User user = userRepository.findByEmail(authUser.getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(id)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_DELETE_USER);
        }

        if (!user.isDeleted() == false) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETED);
        }

        if (passwordEncoder.matches(deleteUserRequestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.PASSWORD_MISMATCH);
        }

        user.changeIsDeleted(user.isDeleted());

        userRepository.save(user);
    }
}
