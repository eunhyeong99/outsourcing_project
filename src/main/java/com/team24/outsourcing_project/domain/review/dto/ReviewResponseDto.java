package com.team24.outsourcing_project.domain.review.dto;

import com.team24.outsourcing_project.domain.review.entity.Review;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewResponseDto {

    private final long id;
    private final long orderId;
    private final long userId;
    private final String content;
    private final int score;

    private ReviewResponseDto(
            final long id,
            final long orderId,
            final long userId,
            final String content,
            final int score
    ) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.content = content;
        this.score = score;
    }

    public static ReviewResponseDto from(final Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getOrder().getId(),
                review.getUser().getId(),
                review.getContent(),
                review.getScore());
    }
}
