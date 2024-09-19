package com.team24.outsourcing_project.domain.review.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
