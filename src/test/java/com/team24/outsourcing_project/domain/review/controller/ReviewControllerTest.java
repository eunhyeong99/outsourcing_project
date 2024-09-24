package com.team24.outsourcing_project.domain.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.order.entity.OrderStatus;
import com.team24.outsourcing_project.domain.review.dto.ReviewCreateRequestDto;
import com.team24.outsourcing_project.domain.review.dto.ReviewResponseDto;
import com.team24.outsourcing_project.domain.review.entity.Review;
import com.team24.outsourcing_project.domain.review.service.ReviewService;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.entity.StoreStatus;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.entity.UserRole;
import com.team24.outsourcing_project.exception.ErrorResponse;
import com.team24.outsourcing_project.exception.GlobalExceptionHandler;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(controllers = ReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ContextConfiguration(classes = {ReviewController.class, GlobalExceptionHandler.class})
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewService reviewService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Nested
    @DisplayName("리뷰 생성 요청 시")
    class CreateReview {

        @Test
        @DisplayName("리뷰가 정상적으로 생성된다.")
        void createReview() throws Exception {
            // given
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, "content", 3);

            // when & then
            mockMvc.perform(post("/api/reviews")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());

            verify(reviewService, times(1))
                    .createReview(any(AuthUser.class), any(ReviewCreateRequestDto.class));
        }

        @Test
        @DisplayName("주문 ID가 없으면 리뷰를 생성할 수 없다.")
        void createReviewWithoutOrderId() throws Exception {
            // given
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(null, "content", 3);

            ErrorResponse errorResponse = ErrorResponse.of(400, "잘못된 요청입니다.");
            errorResponse.addValidation("orderId", "주문 ID는 필수 값입니다.");

            // when & then
            mockMvc.perform(post("/api/reviews")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));

            verify(reviewService, never())
                    .createReview(any(AuthUser.class), any(ReviewCreateRequestDto.class));
        }

        @Test
        @DisplayName("리뷰 내용이 없으면 리뷰를 생성할 수 없다.")
        void createReviewWithoutReviewContent() throws Exception {
            // given
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, null, 3);

            ErrorResponse errorResponse = ErrorResponse.of(400, "잘못된 요청입니다.");
            errorResponse.addValidation("content", "내용은 필수 값입니다.");

            // when & then
            mockMvc.perform(post("/api/reviews")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));

            verify(reviewService, never())
                    .createReview(any(AuthUser.class), any(ReviewCreateRequestDto.class));
        }

        @Test
        @DisplayName("별점이 없으면 리뷰를 생성할 수 없다.")
        void createReviewWithInvalidScore() throws Exception {
            // given
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, "content", null);

            ErrorResponse errorResponse = ErrorResponse.of(400, "잘못된 요청입니다.");
            errorResponse.addValidation("score", "별점은 필수 값입니다.");

            // when & then
            mockMvc.perform(post("/api/reviews")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));

            verify(reviewService, never())
                    .createReview(any(AuthUser.class), any(ReviewCreateRequestDto.class));
        }
    }

    @Nested
    @DisplayName("리뷰 조회 요청 시")
    class GetReview {

        @Test
        @DisplayName("별점 범위에 따라 리뷰를 조회할 수 있다.")
        void getReview() throws Exception {
            // given
            int score = 3;
            List<Review> createdReviews = createReviews(10, score);
            List<ReviewResponseDto> responseDtos = createdReviews.stream()
                    .map(ReviewResponseDto::from)
                    .toList();

            int scoreLowerBound = 3;
            int scoreUpperBound = 3;

            given(reviewService.getReviews(anyLong(), anyInt(), anyInt())).willReturn(responseDtos);

            // when && then
            mockMvc.perform(get("/api/reviews")
                            .param("storeId", "1")
                            .param("lowerBound", String.valueOf(scoreLowerBound))
                            .param("upperBound", String.valueOf(scoreUpperBound)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(responseDtos)));
        }
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

    private User createUser(final long userId) throws NoSuchFieldException, IllegalAccessException {
        User user = User.create("test@email.com", "password", UserRole.USER);
        Field targetField = user.getClass().getDeclaredField("id");
        targetField.setAccessible(true);
        targetField.set(user, userId);
        return user;
    }

    private Order createOrder(User user, Store store, Menu menu, long orderId) throws NoSuchFieldException, IllegalAccessException {
        Order order = Order.create(user, store, menu, OrderStatus.COMPLETED);
        Field targetField = order.getClass().getDeclaredField("id");
        targetField.setAccessible(true);
        targetField.set(order, orderId);
        return order;
    }

    private Menu createMenu(Store store) {
        Menu menu = new Menu();
        menu.setMenuName("menuName");
        menu.setStore(store);
        menu.setMenuPrice(10000);
        return menu;
    }

    private Store createStore(User user) {
        return Store.createStore(
                "name",
                10000,
                StoreStatus.OPEN,
                LocalTime.now(),
                LocalTime.now().plusHours(1L),
                user);
    }
}
