package com.team24.outsourcing_project.domain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.order.entity.OrderStatus;
import com.team24.outsourcing_project.domain.order.repository.OrderRepository;
import com.team24.outsourcing_project.domain.review.dto.ReviewCreateRequestDto;
import com.team24.outsourcing_project.domain.review.dto.ReviewResponseDto;
import com.team24.outsourcing_project.domain.review.entity.Review;
import com.team24.outsourcing_project.domain.review.repository.ReviewRepository;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.entity.StoreStatus;
import com.team24.outsourcing_project.domain.store.repository.StoreRepository;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.entity.UserRole;
import com.team24.outsourcing_project.domain.user.repository.UserRepository;
import com.team24.outsourcing_project.exception.ApplicationException;
import com.team24.outsourcing_project.exception.ErrorCode;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    StoreRepository storeRepository;

    @Nested
    @DisplayName("리뷰 생성 시")
    class CreateReview {

        @Test
        @DisplayName("리뷰가 정상적으로 생성된다.")
        void createReview() throws NoSuchFieldException, IllegalAccessException {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.USER);
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, "content", 3);

            User owner = createUser(authUser.getId());
            Store store = createStore(owner);
            Menu menu = createMenu(store);

            User customer = createUser(authUser.getId() + 1L);
            Order order = createOrder(customer, store, menu, 1L);

            given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
            given(userRepository.findById(anyLong())).willReturn(Optional.of(customer));

            // when
            reviewService.createReview(authUser, requestDto);

            // then
            verify(reviewRepository, times(1)).save(any(Review.class));
        }
        
        @Test
        @DisplayName("주문이 완료 상태가 아니라면 리뷰를 생성할 수 없다.")
        void createReviewWhenOrderStatusIsNotCompleted() throws NoSuchFieldException, IllegalAccessException {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.USER);
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, "content", 3);

            User owner = createUser(authUser.getId());
            User customer = createUser(owner.getId() + 1L);
            Store store = createStore(owner);
            Menu menu = createMenu(store);
            Order order = Order.create(customer, store, menu, OrderStatus.DELIVERING);

            given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> reviewService.createReview(authUser, requestDto))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_ORDER_STATUS.message());

            verify(reviewRepository, never()).save(any(Review.class));
        }

        @Test
        @DisplayName("본인의 주문이 아니라면 리뷰를 생성할 수 없다.")
        void createReviewOnAnotherPersonOrder() throws NoSuchFieldException, IllegalAccessException {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.USER);
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, "content", 3);

            User owner = createUser(authUser.getId());
            Store store = createStore(owner);
            Menu menu = createMenu(store);

            User customer = createUser(owner.getId() + 1L);
            Order order = Order.create(customer, store, menu, OrderStatus.COMPLETED);

            given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
            given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));

            // when & then
            assertThatThrownBy(() -> reviewService.createReview(authUser, requestDto))
                    .isInstanceOf(ApplicationException.class);

            verify(reviewRepository, never()).save(any(Review.class));
        }

        @Test
        @DisplayName("하나의 주문에 리뷰는 한번만 남길 수 있다.")
        void createReviewOnDuplicatedOrder() throws NoSuchFieldException, IllegalAccessException {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.USER);
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, "content", 3);

            User owner = createUser(authUser.getId());
            Store store = createStore(owner);
            Menu menu = createMenu(store);

            User consumer = createUser(owner.getId() + 1L);
            Order order = createOrder(consumer, store, menu, 1L);

            given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
            given(userRepository.findById(anyLong())).willReturn(Optional.of(consumer));
            given(reviewRepository.existsByUserIdAndOrderId(anyLong(), anyLong())).willReturn(true);

            // when
            assertThatThrownBy(() -> reviewService.createReview(authUser, requestDto))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.REVIEW_DUPLICATED.message());

            // then
            verify(reviewRepository, never()).save(any(Review.class));
        }

        @ParameterizedTest
        @CsvSource({"-1", "0", "6", "7"})
        @DisplayName("리뷰 생성 시 별점이 유효하지 않다면 생성할 수 없다.")
        void createReviewWithInvalidScore(int score) throws NoSuchFieldException, IllegalAccessException {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.USER);
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, "content", score);

            User user = createUser(authUser.getId());
            Store store = createStore(user);
            Menu menu = createMenu(store);
            Order order = Order.create(user, store, menu, OrderStatus.DELIVERING);

            given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> reviewService.createReview(authUser, requestDto))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_ORDER_STATUS.message());

            verify(reviewRepository, never()).save(any(Review.class));
        }

        @Test
        @DisplayName("자신의 가게에는 리뷰를 남길 수 없다.")
        void createReviewOnOwnStore() throws NoSuchFieldException, IllegalAccessException {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.USER);
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, "content", 3);

            User owner = createUser(authUser.getId());
            Store store = createStore(owner);
            Menu menu = createMenu(store);

            Order order = createOrder(owner, store, menu, 1L);

            given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
            given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));

            // when
            assertThatThrownBy(() -> reviewService.createReview(authUser, requestDto))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.REVIEW_CANNOT_BE_LEFT_ON_OWN_STORE.message());

            // then
            verify(reviewRepository, never()).save(any(Review.class));
        }

    }

    @Nested
    @DisplayName("리뷰 조회 시")
    class GetReview {

        @Test
        @DisplayName("리뷰가 정상적으로 조회된다.")
        void getReviews() throws NoSuchFieldException, IllegalAccessException {
            // given
            User user = createUser(1L);
            Store store = createStore(user);
            List<Review> reviews = createReviews(10, 3);

            given(storeRepository.existsById(anyLong())).willReturn(true);
            given(reviewRepository.findAllByStoreId(anyLong(), anyInt(), anyInt()))
                    .willReturn(reviews);

            // when
            List<ReviewResponseDto> responseDtos = reviewService.getReviews(store.getId(), 3, 3);

            // then
            assertThat(responseDtos).hasSize(10);
        }


        @ParameterizedTest
        @CsvSource(value = {"0,3","3,6"}, delimiter = ',')
        @DisplayName("유효하지 않은 별점 범위인 경우 리뷰를 조회할 수 없다.")
        void createReviewWithInvalidScoreRange(final int scoreLowerBound, final int scoreUpperBound) throws Exception {
            // given
            given(storeRepository.existsById(anyLong())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> reviewService.getReviews(1L, scoreLowerBound, scoreUpperBound));
        }
    }

    private Order createOrder(User user, Store store, Menu menu, long orderId) throws NoSuchFieldException, IllegalAccessException {
        Order order = Order.create(user, store, menu, OrderStatus.COMPLETED);
        Field targetField = order.getClass().getDeclaredField("id");
        targetField.setAccessible(true);
        targetField.set(order, orderId);
        return order;
    }

    private User createUser(long userId) throws NoSuchFieldException, IllegalAccessException {
        User user = User.create("test@email.com", "password", UserRole.USER);
        Field targetField = user.getClass().getDeclaredField("id");
        targetField.setAccessible(true);
        targetField.set(user, userId);
        return user;
    }

    private Menu createMenu(Store store) {
        Menu menu = new Menu();
        menu.setMenuName("menuName");
        menu.setStore(store);
        menu.setMenuPrice(10000);
        return menu;
    }

    private Store createStore(User user) throws NoSuchFieldException, IllegalAccessException {
        Store store = Store.createStore(
                "name",
                10000,
                StoreStatus.OPEN,
                LocalTime.now(),
                LocalTime.now().plusHours(1),
                user);
        Field targetField = store.getClass().getDeclaredField("id");
        targetField.setAccessible(true);
        targetField.set(store, 1L);
        return store;
    }

    private List<Review> createReviews(final int count, final int score) throws IllegalAccessException, NoSuchFieldException {
        List<Review> createdReviews = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = createUser(i + 1L);
            Store store = createStore(user);
            Menu menu = createMenu(store);
            Order order = createOrder(user, store, menu, i + 1L);

            Review review = Review.create(order, user, "content", score);
            Field targetField = review.getClass().getDeclaredField("id");
            targetField.setAccessible(true);
            targetField.set(review, i + 1L);
            createdReviews.add(review);
        }
        return createdReviews;
    }
}
