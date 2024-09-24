package com.team24.outsourcing_project.domain.user.service;


import static com.team24.outsourcing_project.domain.user.entity.UserRole.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("회원 가입 테스트")
    class Signup {

        @Test
        public void 회원가입_성공() {
            // given
            SignupRequestDto signupRequestDto = new SignupRequestDto("test1212@example.com",
                    "password",
                    "USER");
            given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(false);
            given(passwordEncoder.encode(signupRequestDto.getPassword())).willReturn(
                    "encodedPassword");

            // when
            userService.signup(signupRequestDto);

            // then
            verify(userRepository).existsByEmail(signupRequestDto.getEmail());
        }

        @Test
        public void 이메일값이_null인_경우() {
            // given
            SignupRequestDto signupRequestDto = new SignupRequestDto(null, "password", "USER");

            // when
            ApplicationException exception = assertThrows(
                    ApplicationException.class, () -> userService.signup(signupRequestDto));

            // then
            assertEquals("잘못된 이메일입니다.", exception.getMessage());
        }

        @Test
        public void 이미_존재하는_이메일_경우() {
            //given
            SignupRequestDto signupRequestDto = new SignupRequestDto("test1212@example.com",
                    "password", "USER");

            given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(true);

            //when
            ApplicationException exception = assertThrows(ApplicationException.class,
                    () -> userService.signup(signupRequestDto));

            //then
            assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class Signin {

        @Test
        public void 로그인_성공() {
            // given
            User user = User.create("test1212@example.com", "password1212@", USER);

            SigninRequestDto signinRequestDto = new SigninRequestDto("test1212@example.com",
                    "password1212@");

            given(userRepository.findByEmail(signinRequestDto.getEmail())).willReturn(
                    Optional.of(user));
            given(passwordEncoder.matches(signinRequestDto.getPassword(),
                    user.getPassword())).willReturn(true);

            // when
            SigninResponseDto ResponseDto = userService.signin(signinRequestDto);

            // then
            assertNotNull(ResponseDto);
            verify(userRepository, times(1)).findByEmail(signinRequestDto.getEmail());
            verify(passwordEncoder, times(1)).matches(signinRequestDto.getPassword(),
                    user.getPassword());
            verify(jwtUtil, times(1)).createToken(user.getId(), user.getEmail(), user.getRole());

        }

        @Test
        public void 회원가입_안한_회원이_로그인한_경우() {
            // given
            String email = "test12@example.com";
            String password = "Password";
            SigninRequestDto signinRequestDto = new SigninRequestDto(email, password);

            given(userRepository.findByEmail(email)).willReturn(Optional.empty());

            // when
            Exception exception = assertThrows(ApplicationException.class, () -> {
                userService.signin(signinRequestDto);
            });
            // then
            assertEquals("존재하지 않는 유저입니다.", exception.getMessage());
            verify(userRepository).findByEmail(email);
        }

        @Test
        public void 비밀번호가_불일치_로그인_실패() {
            // given
            String email = "test@example.com";
            String password = "Password";
            String wrongPassword = "Password2";
            // 잘못된 비밀번호
            User user = User.create("test@example.com", passwordEncoder.encode(password), USER);
            SigninRequestDto signinRequestDto = new SigninRequestDto(email, wrongPassword);

            given(userRepository.findByEmail(signinRequestDto.getEmail())).willReturn(
                    Optional.of(user));

            given(passwordEncoder.matches(wrongPassword, user.getPassword())).willReturn(false);

            // when
            ApplicationException exception = assertThrows(ApplicationException.class,
                    () -> userService.signin(signinRequestDto));

            // then
            assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
            verify(passwordEncoder, times(1)).matches(wrongPassword, user.getPassword());
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class deleteUser {

        @Test
        public void 회원탈퇴_성공() {
            //given
            Long userId = 1L;
            String email = "test12@example.com";
            String password = "correctPassword";
            AuthUser authUser = new AuthUser(userId, email, UserRole.USER);
            DeleteUserRequestDto deleteUserRequestDto = new DeleteUserRequestDto(password);
            User user = User.create("test12@example.com", passwordEncoder.encode(password), USER);
            ReflectionTestUtils.setField(user, "id", userId);

            given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(password, user.getPassword())).willReturn(true);

            //when
            userService.deleteUser(authUser, user.getId(), deleteUserRequestDto);

            //then
            assertTrue(user.isDeleted());
            verify(userRepository).findByEmail(email);
            verify(passwordEncoder).matches(password, user.getPassword());
            verify(userRepository).save(user);
        }

        @Test
        public void ID값이_다를경우_회원탈퇴_실패() {
            // given
            Long validUserId = 1L; // 인증된 사용자 ID
            Long deleteUserId = 2L; // 삭제 요청 ID (다른 사용자)
            String email = "test@example.com";
            AuthUser authUser = new AuthUser(validUserId, email, UserRole.USER);
            DeleteUserRequestDto deleteUserRequestDto = new DeleteUserRequestDto("somePassword");

            User user = User.create(email, passwordEncoder.encode("somePassword"), UserRole.USER);
            ReflectionTestUtils.setField(user, "id", deleteUserId); // 삭제할 사용자 ID 설정

            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

            // when
            Exception exception = assertThrows(ApplicationException.class, () -> {
                userService.deleteUser(authUser, validUserId, deleteUserRequestDto);
            });

            // then
            assertEquals("삭제 권한이 없습니다.", exception.getMessage()); // 예외 코드 검증
        }

        @Test
        public void 유저가_이미_탈퇴한_경우_탈퇴_실패() {
            // given
            Long userId = 1L; // 인증된 사용자 ID
            String email = "test@example.com";
            AuthUser authUser = new AuthUser(userId, email, UserRole.USER);
            DeleteUserRequestDto deleteUserRequestDto = new DeleteUserRequestDto("somePassword");

            User user = User.create(email, passwordEncoder.encode("somePassword"), UserRole.USER);
            ReflectionTestUtils.setField(user, "id", userId);
            user.changeIsDeleted(false);

            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

            // when
            Exception exception = assertThrows(ApplicationException.class, () -> {
                userService.deleteUser(authUser, userId, deleteUserRequestDto);
            });

            // then
            assertEquals("이미 탈퇴한 회원입니다.", exception.getMessage()); // 예외 코드 검증
        }

        @Test
        public void 비밀번호가_불일치_회원_탈퇴_실패() {
            Long userId = 1L; // 인증된 사용자 ID
            String email = "test12@example.com";
            AuthUser authUser = new AuthUser(userId, email, USER);
            DeleteUserRequestDto deleteUserRequestDto = new DeleteUserRequestDto("wrongPassword");

            // 올바른 비밀번호로 사용자 생성
            User user = User.create(email, passwordEncoder.encode("correctPassword"), USER);
            ReflectionTestUtils.setField(user, "id", userId);
            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

            // when
            Exception exception = assertThrows(ApplicationException.class, () -> {
                userService.deleteUser(authUser, userId, deleteUserRequestDto);
            });

            // then
            assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
            verify(passwordEncoder).matches(deleteUserRequestDto.getPassword(), user.getPassword());
        }
    }
}