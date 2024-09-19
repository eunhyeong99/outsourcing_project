package com.team24.outsourcing_project.domain.review.controller;

import com.team24.outsourcing_project.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
}
