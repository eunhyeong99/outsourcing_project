package com.team24.outsourcing_project.domain.review.controller;

import com.team24.outsourcing_project.domain.common.annotation.Auth;
import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.review.dto.ReviewCreateRequestDto;
import com.team24.outsourcing_project.domain.review.dto.ReviewResponseDto;
import com.team24.outsourcing_project.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public void createReview(@Auth final AuthUser user, @Valid @RequestBody final ReviewCreateRequestDto request) {
        reviewService.createReview(user, request);
    }

    @GetMapping("/reviews")
    public List<ReviewResponseDto> getReview(
            @RequestParam final long storeId,
            @RequestParam int lowerBound,
            @RequestParam int upperBound
    ) {
        lowerBound = Math.min(lowerBound, upperBound);
        upperBound = Math.max(upperBound, lowerBound);

        return reviewService.getReviews(storeId, lowerBound, upperBound);
    }
}
