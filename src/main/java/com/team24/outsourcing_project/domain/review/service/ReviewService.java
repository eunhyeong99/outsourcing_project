package com.team24.outsourcing_project.domain.review.service;

import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.order.entity.OrderStatus;
import com.team24.outsourcing_project.domain.order.repository.OrderRepository;
import com.team24.outsourcing_project.domain.review.dto.ReviewCreateRequestDto;
import com.team24.outsourcing_project.domain.review.dto.ReviewResponseDto;
import com.team24.outsourcing_project.domain.review.entity.Review;
import com.team24.outsourcing_project.domain.review.repository.ReviewRepository;
import com.team24.outsourcing_project.domain.store.repository.StoreRepository;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.repository.UserRepository;
import com.team24.outsourcing_project.exception.ApplicationException;
import com.team24.outsourcing_project.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    public static final int SCORE_LOWER_BOUND = 1;
    public static final int SCORE_UPPER_BOUND = 5;

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createReview(final AuthUser user, final ReviewCreateRequestDto request) {
        Order foundOrder = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND));
        if (!foundOrder.isSameStatus(OrderStatus.COMPLETED)) {
            throw new ApplicationException(ErrorCode.INVALID_ORDER_STATUS);
        }

        User consumer = userRepository.findById(user.getId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        if (!foundOrder.getUser().getId().equals(consumer.getId())) {
            throw new ApplicationException(ErrorCode.REVIEW_MUST_BE_MY_ORDER);
        }

        if (consumer.getId().equals(foundOrder.getStore().getUser().getId())) {
            throw new ApplicationException(ErrorCode.REVIEW_CANNOT_BE_LEFT_ON_OWN_STORE);
        }
        if (reviewRepository.existsByUserIdAndOrderId(consumer.getId(), foundOrder.getId())) {
            throw new ApplicationException(ErrorCode.REVIEW_DUPLICATED);
        }

        Review review = Review.create(foundOrder, consumer, request.getContent(), request.getScore());
        reviewRepository.save(review);
    }

    public List<ReviewResponseDto> getReviews(final long storeId, final int lowerBound, final int upperBound) {
        if (!storeRepository.existsById(storeId)) {
            throw new ApplicationException(ErrorCode.STORE_NOT_FOUND);
        }
        validateScoreLowerBound(lowerBound);
        validateScoreUpperBound(upperBound);

        return reviewRepository.findAllByStoreId(storeId, lowerBound, upperBound).stream()
                .map(ReviewResponseDto::from)
                .toList();
    }


    private void validateScoreUpperBound(final int upperBound) {
        if (SCORE_UPPER_BOUND < upperBound) {
            throw new ApplicationException(ErrorCode.INVALID_REVIEW_SCORE_RANGE);
        }
    }

    private void validateScoreLowerBound(final int lowerBound) {
        if (lowerBound < SCORE_LOWER_BOUND) {
            throw new ApplicationException(ErrorCode.INVALID_REVIEW_SCORE_RANGE);
        }
    }
}
