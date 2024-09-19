package com.team24.outsourcing_project.domain.user.entity;

import jakarta.persistence.*;

@Entity
@Table
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
