package com.team24.outsourcing_project.domain.store.entity;

import jakarta.persistence.*;

@Table
@Entity
public class Store {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
