package com.team24.outsourcing_project.domain.review.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewCreateRequestDto {

    @NotNull(message = "주문 ID는 필수 값입니다.")
    private final Long orderId;

    @NotEmpty(message = "내용은 필수 값입니다.")
    private final String content;

    @NotNull(message = "별점은 필수 값입니다.")
    private final Integer score;
}
