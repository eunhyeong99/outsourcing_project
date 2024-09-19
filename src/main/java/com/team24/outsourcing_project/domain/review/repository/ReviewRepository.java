package com.team24.outsourcing_project.domain.review.repository;

import com.team24.outsourcing_project.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long>{
}
