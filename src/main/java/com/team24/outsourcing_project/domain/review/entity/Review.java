package com.team24.outsourcing_project.domain.review.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
